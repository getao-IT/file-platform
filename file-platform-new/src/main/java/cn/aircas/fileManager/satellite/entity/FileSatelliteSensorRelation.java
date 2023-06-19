package cn.aircas.fileManager.satellite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class FileSatelliteSensorRelation {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private Long satelliteId;

    private Long sensorId;

    private Date createTime;

    private Date updateTime;
}
