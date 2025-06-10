package com.Server.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Component
public class CorsConfig {

    @Value("${cors.allowedOrigins}")
    private String allowedOrigins;

    @Value("${cors.allowedMethods}")
    private String allowedMethods;

    @Value("${cors.allowedHeaders}")
    private String allowedHeaders;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        config.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
        config.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        List<String> paths = Arrays.asList(
                "/employees/user",
                "/employees/update/*",
                "/employees/delete-user/*",
                "/employees/subsidiary-employees/*",

                "/feedback/add/*",
                "/feedback/pageable-all",
                "/feedback/get-organization-feedbacks/*",

                "/organizations/add",
                "/organizations/remove/*",
                "/organizations/update/*",
                "/organizations/get/*",
                "/organizations/pageable-all",
                "/organizations/all",
                "/organizations/subsidiaries/*",
                "/organizations/get-all-organization-names",

                "/prediction-data/predict",

                "/registration-requests/admin/*",
                "/registration-requests/accept/*",
                "/registration-requests/decline/*",

                "/subsidiaries/add",
                "/subsidiaries/remove/*",
                "/subsidiaries/update/*",
                "/subsidiaries/get/*",
                "/subsidiaries/all",

                "/register",
                "/login",
                "/log-out",
                "/send-otp",
                "/verify-otp",
                "/change-password"
        );

        for (String path : paths) {
            source.registerCorsConfiguration(path, config);
        }

        return new CorsFilter(source);
    }
}
