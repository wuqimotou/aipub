package mt.aipub.functionCall;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import mt.aipub.tool.DateTool;
import mt.aipub.tool.MathTool;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.util.CollectionUtils;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;


public class WaiterFunctionCall {

    /***
     * 获取工具列表
     * @return
     */
    public static List<ToolSpecification> getTools() {
        List<ToolSpecification> tools = new ArrayList<>();
        // 添加需要使用到的工具
        tools.addAll(ToolSpecifications.toolSpecificationsFrom(MathTool.class));
        tools.addAll(ToolSpecifications.toolSpecificationsFrom(DateTool.class));

        return tools;
    }

    /***
     * 获取工具方法名称与对应的方法体
     * @return
     */
    public static Map<String, Method> getToolsMap() {
        Map<String, Method> toolsMap = new HashMap<>();

        Class<?>[] classes = new Class[]{MathTool.class, DateTool.class};

        Arrays.stream(classes).toList().forEach(clazz -> {
            Arrays.stream(clazz.getDeclaredMethods()).toList().forEach(method -> {
                toolsMap.put(method.getName(), method);
            });
        });
        return toolsMap;
    }

    /***
     * 执行工具
     * @param toolExecutionRequests
     * @return
     */
    public static List<ToolExecutionResultMessage> excuteTools(List<ToolExecutionRequest> toolExecutionRequests) {
        if (CollectionUtils.isEmpty(toolExecutionRequests)) {
            return new ArrayList<>();
        }
        List<ToolExecutionResultMessage> toolExecutionResultMessages = new ArrayList<>();
        toolExecutionRequests.forEach(toolExecutionRequest -> {
            String toolName = toolExecutionRequest.name();
            String arguments = toolExecutionRequest.arguments();
            GsonJsonParser gsonJsonParser = new GsonJsonParser();
            //  解析参数
            Map<String, Object> stringObjectMap = gsonJsonParser.parseMap(arguments);
            // 获取工具方法
            Map<String, Method> toolsMap = getToolsMap();
            Method method = toolsMap.get(toolName);
            if (Objects.isNull(method)) {
                toolExecutionResultMessages.add(ToolExecutionResultMessage.from(toolExecutionRequest, "你所调用的"+toolName+"的方法不存在。请忽略这个tool调用。"));
            } else {
                try {
                    Parameter[] parameters = method.getParameters();
                    Object[] params = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        params[i] = stringObjectMap.get(parameters[i].getName());
                    }
                    // 调用工具方法
                    Object invoke = method.invoke(null, params);
                    toolExecutionResultMessages.add(ToolExecutionResultMessage.from(toolExecutionRequest, invoke.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return toolExecutionResultMessages;
    }

}
