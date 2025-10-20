package com.tvvina.tvvina.config;
import com.tvvina.tvvina.domain.ChatMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cho phép truy cập ảnh qua đường dẫn: http://localhost:8080/imageFiles/abc.jpg
        registry.addResourceHandler("/imageFiles/**")
                .addResourceLocations("file:/static/images/");
    }


}

