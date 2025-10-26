package com.foodify.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MultipartJsonConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
                .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                .map(MappingJackson2HttpMessageConverter.class::cast)
                .forEach(converter -> {
                    List<MediaType> supportedTypes = new ArrayList<>(converter.getSupportedMediaTypes());
                    if (!supportedTypes.contains(MediaType.APPLICATION_OCTET_STREAM)) {
                        supportedTypes.add(MediaType.APPLICATION_OCTET_STREAM);
                        converter.setSupportedMediaTypes(supportedTypes);
                    }
                });
    }
}
