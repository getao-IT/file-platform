package cn.aircas.fileManager.satellite.service;

import cn.aircas.fileManager.satellite.entity.FileSatelliteSensorInfo;

import java.util.List;

public interface IFileSatelliteSensorInfoService {

    boolean saveSensor(FileSatelliteSensorInfo sensorInfo);


    boolean updateSensor(FileSatelliteSensorInfo sensorInfo);

    List<FileSatelliteSensorInfo> querySensorList();

    List<FileSatelliteSensorInfo> querySensorListBySatelliteIds( List<Long> satelliteids);

    boolean deleteSensor(Long id);
}
