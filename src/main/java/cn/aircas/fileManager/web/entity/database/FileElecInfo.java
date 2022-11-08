package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "file_elec_info")
public class FileElecInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial4")
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 用户id
     */
    @Column(nullable = true)
    private int userId;

    /**
     * 是否公开
     */
    @Column(nullable = true)
    private boolean isPublic;

    /**
     * 影像名称
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String elecName;

    /**
     * 存放路径
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String path;

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
     * 标签
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String keywords;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp(6)")
    private Date createTime;

    /**
     * 大小
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String size;

    /**
     * 上传批次
     */
    @Column(nullable = true)
    private int batchNumber;

    @Column(nullable = true)
    private int count;
}
