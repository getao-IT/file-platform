package cn.aircas.fileManager.web.controller;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.web.config.aop.annotation.Log;
import cn.aircas.fileManager.web.entity.lab.TextRetrieveParam;
import cn.aircas.fileManager.web.service.LabService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/lab")
public class LabController {

    @Autowired
    LabService labService;

    @Log(value = "以文搜图")
    @GetMapping("/textRetrieve")
    @ApiOperation("以文搜图")
    public CommonResult<String> textRetrieve(TextRetrieveParam textRetrieveParam) {
        this.labService.retrieveImage(textRetrieveParam);
        return new CommonResult<String>().success().message("以文搜图");
    }


}
