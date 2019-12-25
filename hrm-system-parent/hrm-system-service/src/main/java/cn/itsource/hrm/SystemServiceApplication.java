package cn.itsource.hrm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description TODO
 * @Author Top Of The World
 * @Date 2019/12/24 20:04
 * @Version v1.0
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@MapperScan("cn.itsource.hrm.mapper")
public class SystemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemServiceApplication.class, args);
    }
}
