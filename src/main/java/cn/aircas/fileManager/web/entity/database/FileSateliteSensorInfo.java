package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "file_satellite_sensor_info")
public class FileSateliteSensorInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial4")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    /**
     * 传感器名称
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COLLATE \"pg_catalog\".\"default\"")
    private String sensorName;

    /**
     * 传感器编号
     */
    @Column(nullable = false, columnDefinition = "varchar(128) COLLATE \"pg_catalog\".\"default\"")
    private String sensorNo;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "timestamp(6)")
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @Column(columnDefinition = "timestamp(6)")
    private Timestamp updateTime;
}
