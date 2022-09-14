package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "file_image_info")
public class FileImageInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial2")
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * 影像名称
     */
    @Column(nullable = false, columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String imageName;

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
     * 缩略图
     */
    @Column(columnDefinition = "text COLLATE \"pg_catalog\".\"default\"")
    private String thumb;

    /**
     * 影像宽度
     */
    @Column(nullable = true, columnDefinition = "int4 default 0")
    private int width;

    /**
     * 影像高度
     */
    @Column(nullable = true, columnDefinition = "int4 default 0")
    private int height;

    /**
     * 投影信息
     */
    @Column(columnDefinition = "text COLLATE \"pg_catalog\".\"default\"")
    private String projection;

    /**
     * 最小经度
     */
    @Column(columnDefinition = "float4 default 0")
    private float minLon;

    /**
     * 最小纬度
     */
    @Column(columnDefinition = "float4 default 0")
    private float minLat;

    /**
     * 最大经度
     */
    @Column(columnDefinition = "float4 default 0")
    private float maxLon;

    /**
     * 最大纬度
     */
    @Column(columnDefinition = "float4 default 0")
    private float maxLat;

    /**
     * 批次
     */
    @Column(nullable = true)
    private int batchNumber;

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
     * 波段数
     */
    @Column(nullable = true, columnDefinition = "int2")
    private int bands;

    /**
     * 数据类型
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String bit;

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
     * 分辨率
     */
    @Column(columnDefinition = "float4 default 0")
    private float resolution;

    /**
     * 最小投影X
     */
    @Column(name = "min_projection_x", columnDefinition = "float4")
    private float minProjectionX;

    /**
     * 最小投影Y
     */
    @Column(name = "min_projection_y", columnDefinition = "float4")
    private float minProjectionY;

    /**
     * 最大投影X
     */
    @Column(name = "max_projection_x", columnDefinition = "float4")
    private float maxProjectionX;

    /**
     * 最大投影Y
     */
    @Column(name = "max_projection_y", columnDefinition = "float4")
    private float maxProjectionY;

    /**
     * 坐标类型
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String coordinateSystemType;

    /**
     * 文件长度
     */
    @Column(nullable = true, columnDefinition = "int8")
    private int fileLength;

    /**
     * 传感器类型
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String sensorType;

    /**
     * 卫星名称
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String satelliteName;

    /**
     * 是否删除字段
     */
    @Column(nullable = true)
    private boolean delete;

    /**
     * 国家
     */
    @Column(columnDefinition = "varchar(255) COLLATE \"pg_catalog\".\"default\"")
    private String country;
}
