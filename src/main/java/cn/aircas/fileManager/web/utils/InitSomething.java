//package cn.aircas.fileManager.web.utils;
//
//import cn.aircas.catalog.ServiceRegister;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PreDestroy;
//import java.util.concurrent.ExecutionException;
//
///**
// * 初始化部分数据
// * @author wxh
// */
//
//@Component
//public class InitSomething implements CommandLineRunner {
//
//    @Value("${catalog.host}")
//    private String catalogHost;
//
//    /**
//     * 服务启动完成之后调用服务注册接口完成服务注册
//     * @param args
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    @Override
//    public void run(String... args) throws ExecutionException, InterruptedException {
//        //初始化服务注册类，其中catalogHost为服务目录后端地址，多个地址中间用,隔开
//        ServiceRegister serviceRegister = ServiceRegister.getInstance(catalogHost);
//        serviceRegister.register();
//    }
//
//    @PreDestroy
//    public void preDestroy() {
//        ServiceRegister serviceRegister = ServiceRegister.getInstance(catalogHost);
//        serviceRegister.unregisterByServiceName();
//    }
//}
