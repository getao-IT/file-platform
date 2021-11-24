package cn.aircas.fileManager.satellite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class FileSatelliteSensorInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String sensorName;
    private String sensorNo;
    private Date createTime;
    private Date updateTime;
}
