package com.example.sumativaPosts.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EntityScan("com.example.sumativaPosts.model")
@EnableJpaRepositories("com.example.sumativaPosts.repository")
public class TestSecurityConfig {
} 