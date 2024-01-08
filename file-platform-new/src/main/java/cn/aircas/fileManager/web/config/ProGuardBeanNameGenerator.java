package cn.aircas.fileManager.web.config;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import java.beans.Introspector;

public class ProGuardBeanNameGenerator extends AnnotationBeanNameGenerator {

    /**
     * 重写buildDefaultBeanName
     * 其他情况(如自定义BeanName)还是按原来的生成策略,只修改默认(非其他情况)生成的BeanName带上 包名
     *
     */
    @Override
    public String buildDefaultBeanName(BeanDefinition definition) {
        if (definition != null ) {
            return definition.getBeanClassName();
        }
        return null;

        /*String beanClassName = definition.getBeanClassName();
        Assert.state(beanClassName != null, "No bean class name set");
        String shortClassName = ClassUtils.getShortName(beanClassName);
        return Introspector.decapitalize(shortClassName);*/
    }

}
