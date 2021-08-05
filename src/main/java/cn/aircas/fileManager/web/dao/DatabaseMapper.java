package cn.aircas.fileManager.web.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DatabaseMapper {
    List<String>  selectTableNames();


}
