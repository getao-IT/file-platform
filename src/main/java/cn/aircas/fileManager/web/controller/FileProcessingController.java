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

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

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
    public CommonResult<String> formatConverter(HttpServletRequest request , int fileId, String format,String source,String keywords, boolean isPublic) throws AuthException {
        String adminLevel = request.getParameter("adminLevel");
        if (!"0".equals(adminLevel)) {
            throw new AuthException("抱歉，当前功能暂未对普通用户开放");
        }
        Integer code = this.fileProcessingService.formatConverter(fileId,format,source,keywords,isPublic);
        return new CommonResult<String>().success().data(String.valueOf(code)).message("格式转换成功");
    }
}
