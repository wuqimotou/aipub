package mt.aipub.bot;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;


public interface Waiter {

    @SystemMessage("You are a helpful assistant.")
    String chat(@MemoryId Object memoryId, @UserMessage String message);

//    String chat(String message);

}
