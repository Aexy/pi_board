/**
 * PiBoardApplication class
 * Responsible for starting the Application
 */

package dev.krc.piboard;

import dev.krc.piboard.service.IssueService;
import dev.krc.piboard.service.UserService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@SpringBootApplication
public class PiBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiBoardApplication.class, args);
	}

    @Bean
    public List<ToolCallback> tools(UserService userService , IssueService issueService) {
        return Stream.of(
                        ToolCallbacks.from(userService),
                        ToolCallbacks.from(issueService)
                ).flatMap(Stream::of).collect(toList());
    }
}
