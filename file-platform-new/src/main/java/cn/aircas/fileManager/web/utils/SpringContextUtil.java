package cn.aircas.fileManager.web.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 通过spring上下文获取bean
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static  ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    private static ApplicationContext getApplicationContext(){
        return SpringContextUtil.applicationContext;
    }

    public static synchronized Object getBean(String beanName){
        return getApplicationContext().getBean(beanName);
    }
}
