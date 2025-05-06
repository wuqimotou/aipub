package mt.aipub.bot.impl;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.*;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import mt.aipub.bot.Waiter;
import mt.aipub.functionCall.WaiterFunctionCall;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;


public class WaiterImpl implements Waiter {

    private ChatMemoryProvider chatMemoryProvider;

    private ChatModel chatModel;

    private StreamingChatModel streamingChatModel;

    private static final String SystemPrompt;


    /***
     * 读取waiter的prompt模板
     */
    static {
        ClassPathResource classPathResource = new ClassPathResource("promptTemplate/waiterSystemPrompt.txt");
        try {
            SystemPrompt = new String(classPathResource.getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public WaiterImpl(ChatMemoryProvider chatMemoryProvider, ChatModel chatModel, StreamingChatModel streamingChatModel) {
        this.chatMemoryProvider = chatMemoryProvider;
        this.chatModel = chatModel;
        this.streamingChatModel = streamingChatModel;
    }

    /***
     * 聊天
     * @param memoryId
     * @param message
     * @return
     */
    @Override
    public String chat(Object memoryId, String message) {
        ChatMemory chatMemory = chatMemoryProvider.get(memoryId);

        // 设置系统prompt，若已设置过则忽略，若未设置过或有新的prompt则更新
        chatMemory.add(SystemMessage.from(SystemPrompt));

        UserMessage  userMessage = UserMessage.from(message);
        chatMemory.add(userMessage);
        List<ChatMessage> sendMessages = chatMemory.messages();
        // 调用大模型
        ChatResponse chatResponse = chatModel.chat(buildChatRequest(sendMessages));
        AiMessage aiMessage = chatResponse.aiMessage();
        chatMemory.add(aiMessage);
        if (!aiMessage.hasToolExecutionRequests()) {
            return aiMessage.text();
        } else {
            //处理tool调用
            List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();
            List<ToolExecutionResultMessage> toolExecutionResultMessages = WaiterFunctionCall.excuteTools(toolExecutionRequests);
            if (CollectionUtils.isEmpty(toolExecutionResultMessages)) {
                return "貌似出了点问题呢！";
            }
            toolExecutionResultMessages.forEach(chatMemory::add);
            List<ChatMessage> secondSendMessages = chatMemory.messages();
            secondSendMessages.addAll(toolExecutionResultMessages);
            ChatResponse secondChatResponse = chatModel.chat(secondSendMessages);
            AiMessage aiMessage2 = secondChatResponse.aiMessage();
            chatMemory.add(aiMessage2);
            return aiMessage2.text();
        }


    }

    /***
     * 流式聊天
     * @param memoryId
     * @param message
     * @return
     */
    @Override
    public Flux<String> streamChat(Object memoryId, String message) {
        ChatMemory  chatMemory = chatMemoryProvider.get(memoryId);

        chatMemory.add(SystemMessage.from(SystemPrompt));

        UserMessage userMessage = UserMessage.from(message);
        chatMemory.add(userMessage);
        List<ChatMessage> sendMessages = chatMemory.messages();
        return Flux.create(emitter -> {
            streamingChatModel.chat(sendMessages, new StreamingChatResponseHandler() {

                @Override
                public void onPartialResponse(String partialResponse) {
                    emitter.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    emitter.complete();
                    AiMessage aiMessage = completeResponse.aiMessage();
                    chatMemory.add(aiMessage);
                    //todo 处理tool调用
                }


                @Override
                public void onError(Throwable error) {
                    emitter.error(error);
                }
            });
        });
    }


    /***
     * 清空会话
     * @param memoryId
     */
    @Override
    public void clearMemory(Object memoryId) {
        ChatMemory chatMemory = chatMemoryProvider.get(memoryId);
        chatMemory.clear();
    }

    private static ChatRequest buildChatRequest(List<ChatMessage> messages) {

        // 获取工具
        List<ToolSpecification> tools = WaiterFunctionCall.getTools();

        return ChatRequest.builder()
                .messages(messages)
                .toolSpecifications(tools)
                .build();
    }

}
