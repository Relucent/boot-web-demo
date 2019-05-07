package yyl.demo.common.context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class SpringBeanNameGenerator extends AnnotationBeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        // 如果有设置了value，则用value，如果没有则是用全类名
        if (definition instanceof AnnotatedBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.isEmpty(beanName)) {
                return beanName;
            } else {
                // 全限定类名
                return definition.getBeanClassName();
            }
        }
        // 使用默认类名
        return buildDefaultBeanName(definition, registry);
    }
}