package mt.aipub.service;

import mt.aipub.entity.ChatResponse;
import reactor.core.publisher.Flux;

public interface WaiterChatService {

    ChatResponse<String> chatById(String memoryId, String message);

    void clearMemory(String memoryId);

    Flux<String> streamChat(String memoryId, String message);
}
