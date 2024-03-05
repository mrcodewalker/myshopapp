package com.project.shopapp.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public ObjectMapper objectMapper() { return new ObjectMapper(); }
}
