package co.pragma.scaffold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ScaffoldSkillnestBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaffoldSkillnestBoardApplication.class, args);
	}

}
