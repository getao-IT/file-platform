package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "file_transfer_info")
public class FileTransferInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial2")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String fileSaveDir;

    @Column(nullable = true)
    private boolean isPublic;

    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String keywords;

    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String source;

    @Column(nullable = true, columnDefinition = "int4")
    private int userId;

    @Column(nullable = true, columnDefinition = "int4")
    private int batchNumber;

    @Column(columnDefinition = "timestamp(0)")
    private Timestamp createTime;

    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String userName;

    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String fileType;

    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String sensorType;

    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String satelliteName;
}
