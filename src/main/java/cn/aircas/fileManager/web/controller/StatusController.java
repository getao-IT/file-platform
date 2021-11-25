package cn.aircas.fileManager.web.controller;


import cn.aircas.fileManager.commons.entity.common.CommonResult;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "服务管理接口")
@RequestMapping("/status")
@RestController
public class StatusController {

    @Value("${value.app.version}")
    private String version;

    @Value("${value.app.build.time}")
    private String buildTime;

    @GetMapping()
    @ApiOperation("获取服务状态")
    public CommonResult<String> status() {
        return new CommonResult<String>().data("文件服务运行正常").success().message("文件服务运行正常");
    }

    @GetMapping("/version")
    @ApiOperation("获取服务版本号")
    public CommonResult<JSONObject> version(){
        JSONObject result = new JSONObject();
        result.put("version",version);
        result.put("buildTime",buildTime);
        return new CommonResult<JSONObject>().data(result).success().message("文件服务运行正常");
    }
}
