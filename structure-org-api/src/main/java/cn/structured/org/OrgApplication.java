
package cn.structured.org;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "cn.structured.org")
@MapperScan("cn.structured.org.mapper")
@EnableFeignClients
public class OrgApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrgApplication.class, args);
    }
}
