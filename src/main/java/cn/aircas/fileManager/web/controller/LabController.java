package cn.aircas.fileManager.web.controller;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import cn.aircas.fileManager.commons.entity.common.PageResult;
import cn.aircas.fileManager.web.config.aop.annotation.Log;
import cn.aircas.fileManager.web.entity.FileTransferParam;
import cn.aircas.fileManager.web.entity.lab.ImageRetrieveParam;
import cn.aircas.fileManager.web.entity.lab.TextRetrieveParam;
import cn.aircas.fileManager.web.service.LabService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/lab")
public class LabController {

    @Autowired
    LabService labService;

    @Log(value = "以文搜图")
    @GetMapping("/textRetrieve")
    @ApiOperation("以文搜图")
    public CommonResult<PageResult<JSONObject>> textRetrieve(TextRetrieveParam textRetrieveParam) {
        PageResult<JSONObject> pageResult = this.labService.retrieveImage(textRetrieveParam);
        return new CommonResult<PageResult<JSONObject>>().success().data(pageResult).message("以文搜图完成");
    }

    @Log(value = "以图搜图")
    @PostMapping(value = "/imageRetrieve")
    @ApiOperation("以图搜图")
    public CommonResult<PageResult<JSONObject>> imageRetrieve(ImageRetrieveParam imageRetrieveParam) throws IOException { ;
        if (imageRetrieveParam.getFile().isEmpty())
            return new CommonResult<PageResult<JSONObject>>().data(null).fail().message("请选择上传文件");

        PageResult<JSONObject> pageResult = this.labService.retrieveImageByImage(imageRetrieveParam);
        return new CommonResult<PageResult<JSONObject>>().success().data(pageResult).message("以图搜图完成");
    }

}
