package com.know_wave.comma.comma_backend.util.config;

import com.know_wave.comma.comma_backend.util.FIeldNameMessageInterpolator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class UtilBeanConfig {

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();

        factoryBean.setMessageInterpolator(new FIeldNameMessageInterpolator());

        return factoryBean;
    }
}
