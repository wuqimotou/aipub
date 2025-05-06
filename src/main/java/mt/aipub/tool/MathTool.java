package mt.aipub.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class MathTool {

    @Tool(name = "square", value = "计算给定数的平方根")
    public static double square(@P("被开方的数") double x) {
        return Math.sqrt(x);
    }

    @Tool(name = "mod", value = "计算两个数的余数")
    public static long mod(@P("被除数") double x, @P("除数") double y) {
        return (long) x % (long) y;
    }

}
