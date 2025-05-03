package mt.aipub.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class ChatMemoryAutoJobHandle {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @XxlJob("AutoGlobalUpdateMemory")
    public void autoGlobalUpdateMemory(String memoryId) {


    }
}
