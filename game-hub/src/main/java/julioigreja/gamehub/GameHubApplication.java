package julioigreja.gamehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GameHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameHubApplication.class, args);
	}

}
