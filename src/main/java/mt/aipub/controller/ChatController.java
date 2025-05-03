package mt.aipub.controller;


import jakarta.annotation.Resource;
import mt.aipub.entity.ChatRequest;
import mt.aipub.entity.ChatResponse;
import mt.aipub.service.WaiterChatService;
import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private WaiterChatService waiterChatService;
    @PostMapping("/waiter")
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

}
