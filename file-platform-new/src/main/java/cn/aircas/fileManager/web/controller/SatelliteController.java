package cn.aircas.fileManager.web.controller;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.satellite.entity.*;
import cn.aircas.fileManager.satellite.service.IFileSatelliteInfoService;
import cn.aircas.fileManager.web.config.aop.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "衛星信息管理接口")
@RequestMapping("/satellite")
@Slf4j
public class SatelliteController {

    @Autowired
    private IFileSatelliteInfoService satelliteInfoService;

    @Log(value = "查詢所有的衛星列表")
    //@OperationLog(value = "分页查询影像信息")
    @GetMapping("/list")
    @ApiOperation("查詢所有的衛星列表")
    public CommonResult querySatelliteList(){
        return new CommonResult().success().data(satelliteInfoService.querySatelliteList());
    }

    @Log(value = "查詢相關傳感器的衛星列表")
    //@OperationLog(value = "分页查询影像信息")
    @GetMapping("/listBySensor")
    @ApiOperation("查詢相關傳感器的衛星列表")
    public CommonResult querySatelliteList(Long[] sensorIds){
        if (sensorIds==null || sensorIds.length==0){
            return new CommonResult().fail().message("傳感器id不可爲空");
        }
        return new CommonResult().success().data(satelliteInfoService.querySatelliteListBySensor(Arrays.asList(sensorIds)));
    }


    @Log(value = "添加衛星")
    //@OperationLog(value = "分页查询影像信息")
    @PostMapping
    @ApiOperation("查詢相關傳感器的衛星列表")
    public CommonResult add(@RequestBody SatelliteParam param){
        FileSatelliteInfo satelliteInfo = new FileSatelliteInfo();
        satelliteInfo.setSatelliteNo(param.getSatelliteNo());
        satelliteInfo.setSatelliteName(param.getSatelliteName());
        satelliteInfo.setCreateTime(new Date());
        satelliteInfo.setUpdateTime(new Date());
        return new CommonResult().success().data(satelliteInfoService.saveSatellite(satelliteInfo));
    }

    @Log(value = "添加完整的衛星信息")
    //@OperationLog(value = "分页查询影像信息")
    @PostMapping("/addFull")
    @ApiOperation("添加完整的衛星信息")
    public CommonResult addFull(@RequestBody SatelliteFullParam param){
        FileSatelliteInfo satelliteInfo = new FileSatelliteInfo();
        satelliteInfo.setSatelliteNo(param.getSatelliteNo());
        satelliteInfo.setSatelliteName(param.getSatelliteName());
        satelliteInfo.setCreateTime(new Date());
        satelliteInfo.setUpdateTime(new Date());
        return new CommonResult().success().data(satelliteInfoService.saveFullSatellite(satelliteInfo,param.getSensorList()));
    }


    @Log(value = "添加給衛星添加傳感器關係信息")
    //@OperationLog(value = "分页查询影像信息")
    @PostMapping("/addsensorRelation")
    @ApiOperation("添加完整的衛星信息")
    public CommonResult addSensorRelation(@RequestBody SatelliteSensorRelationParam param){
        FileSatelliteSensorRelation relation = new FileSatelliteSensorRelation();
        relation.setSensorId(param.getSensorId());
        relation.setSatelliteId(param.getSatelliteId());
        relation.setCreateTime(new Date());
        relation.setUpdateTime(new Date());
        return satelliteInfoService.saveRelation(relation)?new CommonResult().success():new CommonResult().fail();
    }

    @Log(value = "更新衛星信息")
    //@OperationLog(value = "分页查询影像信息")
    @PutMapping
    @ApiOperation("查詢相關傳感器的衛星列表")
    public CommonResult update(@RequestBody SatelliteParam param){
        if (param.getId() ==null){
            return new CommonResult().fail().message("缺少衛星id信息");
        }
        FileSatelliteInfo satelliteInfo = new FileSatelliteInfo();
        satelliteInfo.setId(param.getId());
        satelliteInfo.setSatelliteNo(param.getSatelliteNo());
        satelliteInfo.setSatelliteName(param.getSatelliteName());
        satelliteInfo.setUpdateTime(new Date());
        return new CommonResult().success().data(satelliteInfoService.updateSatellite(satelliteInfo));
    }
}
