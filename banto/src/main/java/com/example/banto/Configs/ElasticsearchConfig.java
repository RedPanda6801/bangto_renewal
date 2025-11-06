package com.example.banto.Configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Value("${spring.elasticsearch.uris}")
    private String esHost;

    @Override
    public ClientConfiguration clientConfiguration() {
        System.out.printf("name : %s / pw : %s / url : %s \n", username, password, esHost);
        return ClientConfiguration.builder()
            .connectedTo(esHost)
            .usingSsl(false)  // SSL 비활성화 명시
            .withBasicAuth(username, password)
            .build();
    }
}