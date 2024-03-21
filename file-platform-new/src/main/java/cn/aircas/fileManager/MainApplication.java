package cn.aircas.fileManager;

import cn.aircas.fileManager.web.config.ProGuardBeanNameGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@ServletComponentScan
@MapperScan("cn.aircas.fileManager.satellite.dao")
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MainApplication.class)
                .beanNameGenerator(new ProGuardBeanNameGenerator())
                .run(args);
    }

    /*public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }*/


}
