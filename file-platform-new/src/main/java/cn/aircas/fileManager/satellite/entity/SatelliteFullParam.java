package cn.aircas.fileManager.satellite.entity;

import lombok.Data;

import java.util.List;

@Data
public class SatelliteFullParam {

    private Long id;
    private String satelliteName;
    private String satelliteNo;
    private List<Long> sensorList;
}
