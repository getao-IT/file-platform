package cn.aircas.fileManager.web.config;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@ControllerAdvice
public class ExcetpionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResult<String> handlerIllegalArgumentException(IllegalArgumentException e){
        String message = "请检查请求参数";
        if(StringUtils.isNotBlank(e.getMessage()))
            message = e.getMessage();
        return new CommonResult<String>().fail().message(message);
    }

    /**
     * 处理请求参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<String> handlerBindException(BindException e) {
        String message = "请检查请求参数";
        if (e.getBindingResult().getAllErrors().size()!=0)
            message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new CommonResult<String>().message(message).fail();
    }


}
