package mt.aipub.bot;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;


public interface Waiter {


    String chat(Object memoryId, String message);

    Flux<String> streamChat(Object memoryId, String message);

    void clearMemory(Object memoryId);

}
