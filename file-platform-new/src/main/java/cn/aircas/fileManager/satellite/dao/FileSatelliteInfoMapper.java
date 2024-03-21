package cn.aircas.fileManager.satellite.dao;

import cn.aircas.fileManager.satellite.entity.FileSatelliteInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//@MapperScan
public interface FileSatelliteInfoMapper extends BaseMapper<FileSatelliteInfo> {

    @Select("<script>"+
            "SELECT * FROM file_satellite_info where id in(select satellite_id from file_satellite_sensor_relation where sensor_id in (" +
            "<foreach collection='sensorIds' separator=',' item='id'>"+
             "#{id}" +
            "</foreach>"+
            "))"+
            "</script>"
    )
    List<FileSatelliteInfo> querySatelliteListBySensor(@Param("sensorIds") List<Long> sensorIds);
}
