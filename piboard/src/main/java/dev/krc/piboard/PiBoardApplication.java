/**
 * PiBoardApplication class
 * Responsible for starting the Application
 */

package dev.krc.piboard;

import dev.krc.piboard.service.UserService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PiBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiBoardApplication.class, args);
	}

    @Bean
    public List<ToolCallback> tools(UserService userService) {
        return List.of(ToolCallbacks.from(userService));
    }
}
