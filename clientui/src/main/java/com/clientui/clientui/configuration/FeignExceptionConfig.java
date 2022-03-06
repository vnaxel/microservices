package com.clientui.clientui.configuration;

import com.clientui.clientui.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignExceptionConfig {

    @Bean
    public CustomErrorDecoder CustomErrorDecoder() {
        return new CustomErrorDecoder();
    }
}
