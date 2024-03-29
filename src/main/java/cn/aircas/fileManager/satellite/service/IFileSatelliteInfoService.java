package cn.aircas.fileManager.satellite.service;

import cn.aircas.fileManager.satellite.entity.FileSatelliteInfo;
import cn.aircas.fileManager.satellite.entity.FileSatelliteSensorRelation;

import java.util.List;

public interface IFileSatelliteInfoService {

    boolean saveSatellite(FileSatelliteInfo fileSatelliteInfo);


    boolean saveFullSatellite(FileSatelliteInfo fileSatelliteInfo,List<Long> sensorList);

    boolean saveRelation(FileSatelliteSensorRelation relation);

    boolean updateSatellite(FileSatelliteInfo fileSatelliteInfo);

    List<FileSatelliteInfo> querySatelliteList();

    List<FileSatelliteInfo> querySatelliteListBySensor( List<Long> sensorIds);

    boolean deleteSatellite(Long id);
}
