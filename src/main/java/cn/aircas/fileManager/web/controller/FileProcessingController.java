package cn.aircas.fileManager.web.controller;


import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.web.config.aop.annotation.Log;
import cn.aircas.fileManager.web.service.FileProcessingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件转换接口
 */
@RestController
@Api(tags = "文件转换接口")
@RequestMapping("/formatConverter")
@Slf4j
public class FileProcessingController {

    @Autowired
    FileProcessingService fileProcessingService;

    @Log(value = "图片格式转换")
    @ApiOperation("图片格式转换")
    @PostMapping()
    public CommonResult<String> formatConverter(int fileId, String format,String source,String keywords, boolean isPublic) {
        this.fileProcessingService.formatConverter(fileId,format,source,keywords,isPublic);
        return new CommonResult<String>().success().message("格式转换成功");
    }
}
