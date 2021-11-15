package cn.aircas.fileManager.web.controller;


import cn.aircas.fileManager.commons.entity.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "服务管理接口")
@RequestMapping("/status")
@RestController
public class StatusController {

    @GetMapping()
    @ApiOperation("服务")
    public CommonResult<String> status() {

        return new CommonResult<String>().success().message("***服务运行正常");
    }
}
