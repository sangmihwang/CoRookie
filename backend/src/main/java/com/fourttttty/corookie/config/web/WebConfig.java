package com.fourttttty.corookie.config.web;

import com.fourttttty.corookie.issue.util.IssueFilterTypeConverter;
import com.fourttttty.corookie.issue.util.IssueProgressConverter;
import com.fourttttty.corookie.thread.util.EmojiConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(clientUrl, "*")
                .allowedMethods("GET", "POST", "PUT")
                .maxAge(3000);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new IssueFilterTypeConverter());
        registry.addConverter(new IssueProgressConverter());
        registry.addConverter(new EmojiConverter());
    }
}
