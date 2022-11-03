package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "file_text_info")
public class FileTextInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial2")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    /**
     * 影像名称
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String textName;

    /**
     * 存放路径
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String path;

    /**
     * 用户id
     */
    @Column(columnDefinition = "int4")
    private int userId;

    /**
     * 来源
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String source;

    /**
     * 用户名称
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String userName;

    /**
     * 是否公开
     */
    @Column(name = "is_public", columnDefinition = "bool")
    private boolean isPublic;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp(6)")
    private Timestamp createTime;

    /**
     * 标签
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String keywords;

    /**
     * 文件大小
     */
    @Column(columnDefinition = "varchar(50) COLLATE \"pg_catalog\".\"default\"")
    private String size;

    /**
     * 文件行数
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int lineCount;

    /**
     * 文件长度
     */
    @Column(nullable = true, columnDefinition = "int8")
    private int fileLength;

    /**
     * 上传批次
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int batchNumber;
}
