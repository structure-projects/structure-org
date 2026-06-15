package cn.structured.org.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus配置类
 *
 * @author chuck
 * @since 2024-01-01
 */
@Configuration
@MapperScan("cn.structured.org.mapper")
public class MyBatisPlusConfig {


}
