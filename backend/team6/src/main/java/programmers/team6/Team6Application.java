package programmers.team6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import programmers.team6.domain.auth.config.JwtConfiguration;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(JwtConfiguration.class)
public class Team6Application {

	public static void main(String[] args) {
		SpringApplication.run(Team6Application.class, args);
	}

}
