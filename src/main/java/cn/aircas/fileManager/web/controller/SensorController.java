package cn.aircas.fileManager.web.controller;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.satellite.entity.*;
import cn.aircas.fileManager.satellite.service.IFileSatelliteInfoService;
import cn.aircas.fileManager.satellite.service.IFileSatelliteSensorInfoService;
import cn.aircas.fileManager.web.config.aop.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "传感器信息管理接口")
@RequestMapping("/sensor")
@Slf4j
public class SensorController {

    @Autowired
    private IFileSatelliteSensorInfoService sensorInfoService;

    @Log(value = "查询所有的传感器列表")
    //@OperationLog(value = "分页查询影像信息")
    @GetMapping("/list")
    @ApiOperation("查询所有的卫星列表")
    public CommonResult<List<FileSatelliteSensorInfo>> querySatelliteList(){
        return new CommonResult<List<FileSatelliteSensorInfo>>().success().data(sensorInfoService.querySensorList());
    }

    @Log(value = "查询相关传感器的卫星列表")
    //@OperationLog(value = "分页查询影像信息")
    @GetMapping("/listBySatellite")
    @ApiOperation("查询相关传感器的卫星列表")
    public CommonResult<List<FileSatelliteSensorInfo>> querySatelliteList(Long[] satelliteIds){
        if (satelliteIds==null || satelliteIds.length ==0){
            return new CommonResult<List<FileSatelliteSensorInfo>>().fail().message("卫星id不可为空");
        }
        return new CommonResult<List<FileSatelliteSensorInfo>>().success().data(sensorInfoService.querySensorListBySatelliteIds(Arrays.asList(satelliteIds)));
    }


    @Log(value = "添加传感器")
    //@OperationLog(value = "分页查询影像信息")
    @PostMapping
    @ApiOperation("添加传感器")
    public CommonResult<Boolean> add(@RequestBody SensorParam param){
        FileSatelliteSensorInfo sensorInfo = new FileSatelliteSensorInfo();
        sensorInfo.setSensorNo(param.getSensorNo());
        sensorInfo.setSensorName(param.getSensorName());
        sensorInfo.setCreateTime(new Date());
        sensorInfo.setUpdateTime(new Date());
        return new CommonResult<Boolean>().success().data(sensorInfoService.saveSensor(sensorInfo));
    }

    @Log(value = "更新传感器信息")
    //@OperationLog(value = "分页查询影像信息")
    @PutMapping
    @ApiOperation("更新传感器信息")
    public CommonResult<Boolean> update(@RequestBody SensorParam param){
        if (param.getId() ==null){
            return new CommonResult<Boolean>().fail().message("缺少传感器id信息");
        }
        FileSatelliteSensorInfo sensorInfo = new FileSatelliteSensorInfo();
        sensorInfo.setId(param.getId());
        sensorInfo.setSensorNo(param.getSensorNo());
        sensorInfo.setSensorName(param.getSensorName());
        sensorInfo.setUpdateTime(new Date());
        return new CommonResult<Boolean>().success().data(sensorInfoService.updateSensor(sensorInfo));
    }
}
