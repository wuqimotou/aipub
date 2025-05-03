package mt.aipub.bot;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface Waiter {

    String chat(@MemoryId String memoryId, @UserMessage String message);

//    String chat(String message);

}
