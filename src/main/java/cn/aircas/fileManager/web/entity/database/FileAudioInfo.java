package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "file_audio_info")
public class FileAudioInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial2")
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 影像名称
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String audioName;

    /**
     * 存放路径
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String path;

    /**
     * 用户id
     */
    @Column(nullable = true)
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
    @Column(name = "is_public", nullable = true)
    private boolean isPublic;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp(6)")
    private String createTime;

    /**
     * 标签
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String keywords;

    /**
     * 大小
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String size;

    /**
     * 音频时长
     */
    @Column(columnDefinition = "int8", nullable = true)
    private int duration;

    /**
     * 文件长度
     */
    @Column(columnDefinition = "int8", nullable = true)
    private int fileLength;

    /**
     * 上传批次
     */
    @Column(columnDefinition = "int4", nullable = true)
    private int batchNumber;

    /**
     * 字符串格式时长
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String durationStr;

    /**
     * 格式
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String format;
}
