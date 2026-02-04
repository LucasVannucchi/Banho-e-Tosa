package br.com.gihpet.banhoetosa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BanhoetosaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanhoetosaApplication.class, args);
	}

}
