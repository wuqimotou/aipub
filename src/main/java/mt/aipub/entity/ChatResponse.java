package mt.aipub.entity;

import lombok.Data;

@Data
public class ChatResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> ChatResponse<T> success(T data) {
        ChatResponse<T> response = new ChatResponse<>();
        response.setStatus("success!");
        response.setData(data);
        return response;
    }

    public static <T> ChatResponse<T> error(String status) {
        ChatResponse<T> response = new ChatResponse<>();
        response.setStatus(status);
        return response;
    }

    public static <T> ChatResponse<T> error(String status, String message) {
        ChatResponse<T> response = new ChatResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }
}
