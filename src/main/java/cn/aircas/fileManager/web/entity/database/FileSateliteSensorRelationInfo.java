package cn.aircas.fileManager.web.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "file_satellite_sensor_relation")
public class FileSateliteSensorRelationInfo {

    @Id
    @Column(name = "id", unique = true, columnDefinition = "serial2")
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    /**
     * 卫星id
     */
    @Column(nullable = false, columnDefinition = "int4")
    private int satelliteId;

    /**
     * 传感器id
     */
    @Column(nullable = false, columnDefinition = "int4")
    private int sensorId;

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
