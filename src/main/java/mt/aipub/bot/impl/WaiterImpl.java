package mt.aipub.bot.impl;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import mt.aipub.bot.Waiter;
import org.springframework.core.io.ClassPathResource;

import java.util.Collections;
import java.util.List;

public class WaiterImpl implements Waiter {

    private ChatMemoryProvider chatMemoryProvider;
    private ChatModel chatModel;
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

    public WaiterImpl(ChatMemoryProvider chatMemoryProvider, ChatModel chatModel) {
        this.chatMemoryProvider = chatMemoryProvider;
        this.chatModel = chatModel;
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
        //todo 处理tool调用
        return aiMessage.text();
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
        return ChatRequest.builder()
                .messages(messages)
                .build();
    }

}
