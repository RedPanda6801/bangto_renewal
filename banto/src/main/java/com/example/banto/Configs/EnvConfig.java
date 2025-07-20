package com.example.banto.Configs;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class EnvConfig {
	private final Dotenv dotenv;
	
	public EnvConfig() {
		this.dotenv = Dotenv.configure().ignoreIfMissing().load();
		System.out.println("Loaded JWT_SECRET: " + dotenv.get("JWT_SECRET"));
	}
	
	public String get(String key) {
		return dotenv.get(key);
	}
}
