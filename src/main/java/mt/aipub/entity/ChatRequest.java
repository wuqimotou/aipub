package mt.aipub.entity;

import lombok.Data;

@Data
public class ChatRequest {

    private String memoryId;

    private String message;

    @Override
    public String toString() {
        return "ChatRequest{" +
                "memoryId='" + memoryId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
