package mt.aipub.service;

import mt.aipub.entity.ChatResponse;

public interface WaiterChatService {

    ChatResponse<String> chatById(String memoryId, String message);

    void clearMemory(String memoryId);
}
