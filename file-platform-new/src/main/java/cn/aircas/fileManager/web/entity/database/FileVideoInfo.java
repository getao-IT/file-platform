package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "file_video_info")
public class FileVideoInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial4")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    /**
     * 影像名称
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String videoName;

    /**
     * 存放路径
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String path;

    /**
     * 用户id
     */
    @Column(nullable = true, columnDefinition = "int4")
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
    @Column(nullable = true, name = "is_public")
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
     * 视频时长
     */
    @Column(nullable = true, columnDefinition = "int8")
    private int duration;

    /**
     * 文件长度
     */
    @Column(nullable = true, columnDefinition = "int8")
    private int fileLength;

    /**
     * 分辨率
     */
    @Column(nullable = true, columnDefinition = "float4")
    private float resolution;

    /**
     * 上传批次
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int batchNumber;

    /**
     * 格式化
     */
    @Column(columnDefinition = "varchar(50) COLLATE \"pg_catalog\".\"default\"")
    private String format;

    /**
     * 格式化日期
     */
    @Column(columnDefinition = "varchar(50) COLLATE \"pg_catalog\".\"default\"")
    private String frameRate;

    /**
     * 宽度
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int width;

    /**
     * 高度
     */
    @Column(nullable = true, columnDefinition = "int4")
    private int height;

    /**
     * 时长字符串描述
     */
    @Column(columnDefinition = "varchar(50) COLLATE \"pg_catalog\".\"default\"")
    private String durationStr;
}
