package cn.aircas.fileManager.satellite.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class FileSatelliteInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String satelliteName;

    private String satelliteNo;
//    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
//    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
