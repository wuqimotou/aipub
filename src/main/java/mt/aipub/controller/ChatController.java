package mt.aipub.controller;


import jakarta.annotation.Resource;
import mt.aipub.entity.ChatRequest;
import mt.aipub.entity.ChatResponse;
import mt.aipub.service.WaiterChatService;
import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private WaiterChatService waiterChatService;
    @PostMapping("/waiter/chat")
    public ChatResponse<String> chat(@RequestBody ChatRequest chatRequest) {
        if (Objects.isNull(chatRequest)) {
            return ChatResponse.error("参数为空！");
        }
        try {
            return waiterChatService.chatById(chatRequest.getMemoryId(), chatRequest.getMessage());
        } catch (Exception e) {
            return ChatResponse.error("对话错误！", e.getMessage());
        }
    }
    @PostMapping(value = "/waiter/streamChat", produces = "text/event-stream;charset=utf-8")
    public Flux<String> streamChat(@RequestBody ChatRequest chatRequest) {
        if (Objects.isNull(chatRequest)) {
            return Flux.just("参数为空！");
        }
        try {
            return waiterChatService.streamChat(chatRequest.getMemoryId(), chatRequest.getMessage());
        } catch (Exception e) {
            return Flux.just("对话错误！", e.getMessage());
        }
    }
    @DeleteMapping("/waiter/clear")
    public ChatResponse<String> clear(@RequestParam String memoryId) {
        if (Objects.isNull(memoryId)) {
            return ChatResponse.error("要清除的ID为空！");
        }
        try {
            waiterChatService.clearMemory(memoryId);
            return ChatResponse.success("清除成功！");
        } catch (Exception e) {
            return ChatResponse.error("清除失败！", e.getMessage());
        }
    }


}
