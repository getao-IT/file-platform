package cn.aircas.fileManager.satellite.dao;

import cn.aircas.fileManager.satellite.entity.FileSatelliteSensorInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FileSatelliteSensorInfoMapper extends BaseMapper<FileSatelliteSensorInfo> {

    @Select("<script>"+
            "SELECT * FROM file_satellite_sensor_info where id in (SELECT sensor_id from file_satellite_sensor_relation where satellite_id in (" +
            "<foreach collection='satelliteIds' separator=',' item='id'>"+
             "#{id}" +
            "</foreach>"+
            "))"+
            "</script>"
    )
    List<FileSatelliteSensorInfo> querySensorBySatellite(@Param("satelliteIds") List<Long> satelliteIds);
}
