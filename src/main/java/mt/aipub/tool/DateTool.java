package mt.aipub.tool;

import dev.langchain4j.agent.tool.Tool;

public class DateTool {

    /**
     * 获取当前时间
     * @return
     */
    @Tool(name = "getCurrentTime", value = "获取当前时间")
    public static String getCurrentTime() {
        return java.time.LocalDateTime.now().toString();
    }

}
