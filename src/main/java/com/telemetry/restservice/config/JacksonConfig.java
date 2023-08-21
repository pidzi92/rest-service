package com.telemetry.restservice.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.telemetry.restservice.model.TelemetryItemDTO;
import com.telemetry.restservice.util.TelemetryItemDTOSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(TelemetryItemDTO.class, new TelemetryItemDTOSerializer());
        return Jackson2ObjectMapperBuilder.json().modules(module);
    }
}
