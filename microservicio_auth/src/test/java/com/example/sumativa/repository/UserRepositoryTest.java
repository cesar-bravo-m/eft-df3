package com.example.sumativa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.config.TestSecurityConfig;

@DataJpaTest(
    excludeAutoConfiguration = TestSecurityConfig.class,
    properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=always",
    }
)
@ActiveProfiles({"test", "web-test"})
@EntityScan(basePackages = {"com.example.sumativa.model"})
public class UserRepositoryTest {
    @Test
    public void testFindByUsername() {
    }
}