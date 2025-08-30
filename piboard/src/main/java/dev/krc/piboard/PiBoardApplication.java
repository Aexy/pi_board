/**
 * PiBoardApplication class
 * Responsible for starting the Application
 */

package dev.krc.piboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PiBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiBoardApplication.class, args);
	}

}
