package de.vayd.sebastianbrunnert.authentication.jackson;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new JacksonViewContextModule());
        return mapper;
    }
}
