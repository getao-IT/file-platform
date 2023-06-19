package cn.aircas.fileManager.satellite.service.impl;

import cn.aircas.fileManager.satellite.dao.FileSatelliteInfoMapper;
import cn.aircas.fileManager.satellite.dao.FileSatelliteSensorRelationMapper;
import cn.aircas.fileManager.satellite.entity.FileSatelliteInfo;
import cn.aircas.fileManager.satellite.entity.FileSatelliteSensorRelation;
import cn.aircas.fileManager.satellite.service.IFileSatelliteInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 衛星 信息維護和 查詢
 */
@Service
public class FileSatelliteInfoServiceImpl extends ServiceImpl<FileSatelliteInfoMapper, FileSatelliteInfo>
        implements IFileSatelliteInfoService {
    @Autowired
    private FileSatelliteSensorRelationMapper relationMapper;

    @Override
    public boolean saveSatellite(FileSatelliteInfo fileSatelliteInfo) {
        return this.save(fileSatelliteInfo);
    }

    @Transactional
    @Override
    public boolean saveFullSatellite(FileSatelliteInfo fileSatelliteInfo,List<Long> sensorList){
        boolean r = this.save(fileSatelliteInfo);
        if (!r)
        {
            return r;
        }
        for (Long sensorId : sensorList){
            FileSatelliteSensorRelation relation = new FileSatelliteSensorRelation();
            relation.setSatelliteId(fileSatelliteInfo.getId());
            relation.setSensorId(sensorId);
            relation.setCreateTime(new Date());
            relation.setUpdateTime(new Date());
            relationMapper.insert(relation);
        }
        return true;
    }

    public boolean saveRelation(FileSatelliteSensorRelation relation){
        return this.relationMapper.insert(relation)>0?true:false;
    }

    @Override
    public boolean updateSatellite(FileSatelliteInfo fileSatelliteInfo) {
        return this.updateById(fileSatelliteInfo);
    }

    @Override
    public List<FileSatelliteInfo> querySatelliteList() {
        return this.list();
    }

    @Override
    public List<FileSatelliteInfo> querySatelliteListBySensor( List<Long> sensorIds) {
        return this.baseMapper.querySatelliteListBySensor(sensorIds);
    }

    @Override
    public boolean deleteSatellite(Long id) {
        return this.removeById(id);
    }
}
