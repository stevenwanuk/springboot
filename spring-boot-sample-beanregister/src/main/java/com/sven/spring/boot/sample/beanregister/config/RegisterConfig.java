package com.sven.spring.boot.sample.beanregister.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Configuration;

import com.sven.spring.boot.sample.beanregister.controller.SimpleServiceController;
import com.sven.spring.boot.sample.beanregister.service.SimpleService;

@Configuration
public class RegisterConfig implements BeanFactoryAware
{

    private final static Logger log = LoggerFactory.getLogger(RegisterConfig.class);

    @PostConstruct
    public void registerProviders()
    {

        DefaultListableBeanFactory configurableBeanFactory =
                (DefaultListableBeanFactory) beanFactory;

        SimpleService serviceA =
                configurableBeanFactory.createBean(SimpleService.class);
        serviceA.setName("serviceA");
        
        configurableBeanFactory.registerSingleton(
                serviceA.getName(),
                serviceA);

        addToDependency(
                serviceA.getName(),
                configurableBeanFactory);
    }

    public void addToDependency(String providerName,
            DefaultListableBeanFactory configurableBeanFactory)
    {

        
        /*
         * There are 2 way to let auto-register and programmely-register beans autowired as a List
         * 1, register dependency and refresh simpleServiceController
         * 2, add @Lazy to simpleServiceController
         */
        
        BeanDefinition beanDefinition =
                configurableBeanFactory.getBeanDefinition("simpleServiceController");
        if (beanDefinition != null)
        {

            String[] dependsOn = beanDefinition.getDependsOn();

            String[] newDependsOn = org.springframework.util.StringUtils.addStringToArray(
                    dependsOn,
                    providerName);
            beanDefinition.setDependsOn(
                    org.springframework.util.StringUtils.removeDuplicateStrings(
                            newDependsOn));
        }

        SimpleServiceController controller =
                configurableBeanFactory.getBean(SimpleServiceController.class);
        if (controller != null)
        {
            configurableBeanFactory.autowireBeanProperties(
                    controller,
                    RootBeanDefinition.AUTOWIRE_BY_TYPE,
                    true);
        }
    }

    /*
     * @Order(1)
     * 
     * @PostConstruct public void registerProviders() {
     * 
     * ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)
     * beanFactory;
     * 
     * for (Map.Entry<String, ProviderProperties> entry : providerProperties.entrySet()) {
     * 
     * ProviderProperties properties = entry.getValue(); if (properties != null &&
     * properties.isEnabled()) { Errors errors = new BeanPropertyBindingResult(properties,
     * "objectName"); validator.validate(properties, errors); if (errors.hasErrors()) {
     * log.error( "unable to register provider " + properties.getName()); throw new
     * SsoException(errors.toString(), SsoError.PROVIDER_CONFIG_ERROR, null); } else {
     * 
     * OidcProvider provider = new OidcProvider();
     * provider.setProviderProperties(entry.getValue());
     * 
     * if (StringUtils.isNotBlank(provider.getDiscoveryUri())) { provider.discovery(); }
     * 
     * configurableBeanFactory.registerSingleton(
     * provider.getProviderProperties().getName(), provider); log.info(
     * "register provider " + provider.getProviderProperties().getName()); } } } }
     */
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;

    }
}
