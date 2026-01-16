package yyl.demo.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "custom.executors")
public class ExecutorProperties {

    private Map<String, ExecutorItem> executors = new HashMap<>();

    @Data
    public static class ExecutorItem {

        private Integer core;
        private Integer max;
        private Integer queue;

        public int getOrDefaultCore() {
            return core != null ? core : Runtime.getRuntime().availableProcessors() * 2;
        }

        public int getOrDefaultMax() {
            return max != null ? max : Runtime.getRuntime().availableProcessors() * 10;
        }

        public int getOrDefaultQueue() {
            return queue != null ? queue : 1000;
        }
    }
}
