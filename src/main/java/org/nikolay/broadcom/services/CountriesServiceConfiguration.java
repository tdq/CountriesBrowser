package org.nikolay.broadcom.services;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CountriesServiceConfiguration {

    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public CountriesService countriesService() {
        return new CountriesServiceImpl();
    }
}
