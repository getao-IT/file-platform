package cn.aircas.fileManager.web.config.aop.aspect;

import cn.aircas.fileManager.commons.entity.common.CommonResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;


/**
 * @author vanishrain
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

//    @Autowired
//    OperationLogsService operationLogsService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Pointcut("@annotation(cn.aircas.fileManager.web.config.aop.annotation.Log)")
    public void pointcut(){
    }

//    @Pointcut("@annotation(cn.iecas.geoai.config.aop.annotation.OperationLog)")
//    public void log(){
//    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        CommonResult<?> result = null;
        String methodName = ((MethodSignature)point.getSignature()).getMethod().getAnnotation(cn.aircas.fileManager.web.config.aop.annotation.Log.class).value();
        log.info("进入 {} 的 {} 方法",point.getSignature().getDeclaringType().getName(),
                methodName);
        Object[] args = point.getArgs();
        for (Object arg : args)
            log.info("参数为: {}", arg);
        try{
            long begin = System.currentTimeMillis();
            result = (CommonResult<?>) point.proceed();
            long timeConsuming = System.currentTimeMillis() - begin;
            log.info("{} 方法执行完毕，返回参数 {}, 共耗时 {} 毫秒", methodName, result,timeConsuming);
        }catch (Exception e){
            result = new CommonResult<>().fail().message(e.getMessage());
            log.error("{} 方法执行异常，返回参数 {}, 异常栈: {}", methodName, result, e.getStackTrace()[0]);
            throw e;
        }


        return result;

    }

//    @Before("log()")
//    public void doBefore(JoinPoint joinPoint){
//        int userId;
//        String userName;
//        String contentType = httpServletRequest.getContentType();
//        if (contentType!=null && contentType.contains("application/json")){
//            JSONObject body = getJSONParam();
//            userId = body.getInteger("userId");
//            userName = body.getString("userName");
//        } else {
//            userName = httpServletRequest.getParameter("userName");
//            userId = Integer.parseInt(httpServletRequest.getParameter("userId"));
//        }
//
//        String operationName = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(cn.iecas.geoai.config.aop.annotation.OperationLog.class).value();
//
//        OperationLog operationLogs = new OperationLog(userId,userName,operationName);
//        this.operationLogsService.save(operationLogs);
//        log.info(operationLogs.toString());
//
//    }

    private JSONObject getJSONParam(){
        String line;
        StringBuilder content = new StringBuilder();
        try{
            BufferedReader bufferedReader = httpServletRequest.getReader();
            while((line = bufferedReader.readLine())!=null){
                content.append(line);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject body = StringUtils.isBlank(content) ? new JSONObject() : JSONObject.parseObject(content.toString());
        return body;
    }

}
