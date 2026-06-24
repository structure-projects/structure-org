package cn.structured.org.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestDatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String dbName = "testdb_" + UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> props = new HashMap<>();
        props.put("spring.datasource.url",
                "jdbc:h2:mem:" + dbName + ";MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE");
        applicationContext.getEnvironment().getPropertySources().addFirst(
                new MapPropertySource("test-db-props", props));
    }
}
