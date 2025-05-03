package mt.aipub.service.impl;

import jakarta.annotation.Resource;
import mt.aipub.bot.Waiter;
import mt.aipub.entity.ChatResponse;
import mt.aipub.service.WaiterChatService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class WaiterChatServiceImpl implements WaiterChatService {

    @Resource
    private Waiter waiter;

    @Override
    public ChatResponse<String> chatById(String memoryId, String message) {
        if (Objects.isNull(memoryId) || Objects.isNull(message)) {
            return ChatResponse.error("参数错误", "memoryId或message不能为空");
        }
        String chatResult = waiter.chat(memoryId, message);
        if (Objects.isNull(chatResult)) {
            return ChatResponse.error("模型返回内容为空");
        }
        return ChatResponse.success(chatResult);
    }

}
