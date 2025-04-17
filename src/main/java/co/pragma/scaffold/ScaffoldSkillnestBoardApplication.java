package co.pragma.scaffold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScaffoldSkillnestBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaffoldSkillnestBoardApplication.class, args);

		System.out.println("Hilos disponibles: " + Runtime.getRuntime().availableProcessors());
	}

}
