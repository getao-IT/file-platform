package cn.aircas.fileManager.web.service.impl;

import cn.aircas.fileManager.web.dao.DatabaseMapper;
import cn.aircas.fileManager.web.entity.DatabaseConfig;
import cn.aircas.fileManager.web.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
@EnableConfigurationProperties
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired
    private DatabaseMapper databaseMapper;


    @Override
    public void initDatabase(){
        List<String> tables = databaseConfig.getTables();

        List<String> tableNameList = databaseMapper.selectTableNames();
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        try {
            ScriptRunner runner=new ScriptRunner(dataSource.getConnection());
            runner.setAutoCommit(false);
            runner.setStopOnError(true);
            runner.setSendFullScript(false);
            runner.setLogWriter(null);
            runner.setFullLineDelimiter(false);
            runner.setDelimiter(";");
            for(String table:tables){
                if(tableNameList.indexOf(table)>-1){
                    log.info("表：{} 已经存在",table);
                }
                else {
                    inputStream = this.getClass().getClassLoader().getResourceAsStream("sql/"+table+".sql");
                    inputStreamReader=new InputStreamReader(inputStream,"utf-8");
                    runner.runScript(inputStreamReader);
                    log.info("创建表:{}",table);
                }
            }
            if (inputStreamReader!=null)
                inputStreamReader.close();
            if (inputStream!=null)
                inputStream.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
