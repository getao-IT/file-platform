package cn.aircas.fileManager.web.config.aop.aspect;


import cn.aircas.fileManager.image.entity.Image;
import cn.aircas.fileManager.web.service.LabService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
public class LabAspect {

    @Autowired
    LabService labService;

    /**
     * 删除影像编码切面
     */
    @Pointcut("execution(* cn.aircas.fileManager.image.service.ImageFileServiceImpl.deleteFileByIds(..))")
    public void dePointcut(){
    }

    @After("dePointcut()")
    public void deAfter(JoinPoint joinPoint){
        this.labService.decodeImage((List<Integer>) joinPoint.getArgs()[0]);
    }

    /**
     * 单张影像编码切面
     */
    @Pointcut("execution(* cn.aircas.fileManager.image.service.ImageTransferService.transferFromWeb(..))")
    public void enPointcut(){
    }

    @AfterReturning(pointcut = "enPointcut()",returning = "result")
    public void enAfter(JoinPoint joinPoint,Object result){
        log.info("进入切面");
        this.labService.encodeImage((Image) result);
    }

    /**
     * 多张影像编码切面
     */
    @Pointcut("execution(* cn.aircas.fileManager.image.service.ImageTransferService.transferFromBackend(..))")
    public void enlistPointcut(){
    }

    @AfterReturning(pointcut = "enlistPointcut()",returning = "result")
    public void enlistAfter(JoinPoint joinPoint,Object result){
        log.info("进入切面");
        this.labService.encodeImage((List<Image>) result);
    }


}
