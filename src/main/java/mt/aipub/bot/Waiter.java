package mt.aipub.bot;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;


public interface Waiter {


    String chat(Object memoryId, String message);

    void clearMemory(Object memoryId);

}
