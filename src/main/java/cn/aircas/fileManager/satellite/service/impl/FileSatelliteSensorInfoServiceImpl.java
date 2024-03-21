package cn.aircas.fileManager.satellite.service.impl;

import cn.aircas.fileManager.satellite.dao.FileSatelliteInfoMapper;
import cn.aircas.fileManager.satellite.dao.FileSatelliteSensorInfoMapper;
import cn.aircas.fileManager.satellite.dao.FileSatelliteSensorRelationMapper;
import cn.aircas.fileManager.satellite.entity.FileSatelliteInfo;
import cn.aircas.fileManager.satellite.entity.FileSatelliteSensorInfo;
import cn.aircas.fileManager.satellite.entity.FileSatelliteSensorRelation;
import cn.aircas.fileManager.satellite.service.IFileSatelliteInfoService;
import cn.aircas.fileManager.satellite.service.IFileSatelliteSensorInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 傳感器信息維護和查詢
 */
@Service
public class FileSatelliteSensorInfoServiceImpl extends ServiceImpl<FileSatelliteSensorInfoMapper, FileSatelliteSensorInfo>
        implements IFileSatelliteSensorInfoService {

    @Override
    public boolean saveSensor(FileSatelliteSensorInfo sensorInfo) {
        return this.save(sensorInfo);
    }

    @Override
    public boolean updateSensor(FileSatelliteSensorInfo sensorInfo) {
        return this.updateById(sensorInfo);
    }

    @Override
    public List<FileSatelliteSensorInfo> querySensorList() {
        return this.list();
    }

    @Override
    public List<FileSatelliteSensorInfo> querySensorListBySatelliteIds(List<Long> satelliteids) {
        return this.baseMapper.querySensorBySatellite(satelliteids);
    }

    @Override
    public boolean deleteSensor(Long id) {
        return this.removeById(id);
    }
}
