package cn.aircas.fileManager.web.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"cn.aircas.fileManager.audio.dao","cn.aircas.fileManager.image.dao",
        "cn.aircas.fileManager.video.dao","cn.aircas.fileManager.text.dao","cn.aircas.fileManager.web.dao"})
public class MyBatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        //page.setDbType(DbType.POSTGRE_SQL);
        return page;
    }
}
