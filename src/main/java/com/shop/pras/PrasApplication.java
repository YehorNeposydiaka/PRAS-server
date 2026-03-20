package com.shop.pras;
import com.shop.pras.Services.BaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.shop.pras.Models")
@EnableJpaRepositories("com.shop.pras.Repository")
public class PrasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrasApplication.class, args);
	}
}