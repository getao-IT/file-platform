package cn.aircas.fileManager.web.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "value.database")
public class DatabaseConfig {
    String dbName;
    List<String> tables;
}
