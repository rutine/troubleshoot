package com.rutine.troubleshoot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 *
 *
 * @date 2017年4月7日
 * @author rutine
 */
public class LearnBean implements InitializingBean, BeanPostProcessor {
    private LearnBean learnBean;
    
    public static void test() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new LearnBean());
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("applicationContext.xml");
        
        beanFactory.getBean("learnBean");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(String.format("postProcessBeforeInitialization -- %s[%s]", bean, beanName));
        
        // 返回跟bean不一样的对象引起异常
        LearnBean newBean = new LearnBean();
        return newBean;
    } 
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(String.format("afterPropertiesSet -- %s[none]", learnBean, "afterSet"));
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(String.format("postProcessAfterInitialization -- %s[%s]", bean, beanName));
        
        return bean;
    }

    public LearnBean getLearnBean() {
        return learnBean;
    }

    public void setLearnBean(LearnBean learnBean) {
        this.learnBean = learnBean;
    }
    
}
