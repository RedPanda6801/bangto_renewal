package com.example.banto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.example.banto.ItemDocument")
public class BantoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BantoApplication.class, args);
	}

}
