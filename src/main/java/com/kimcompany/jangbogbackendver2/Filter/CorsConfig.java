package com.kimcompany.jangbogbackendver2.Filter;

import com.kimcompany.jangbogbackendver2.Text.BasicText;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsfilter() {
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration=new CorsConfiguration();
        configuration.setAllowCredentials(true);
        //configuration.addAllowedOrigin(front_domain);
        //configuration.addAllowedOrigin(foword_front);
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader(BasicText.AuthenticationText);
        configuration.addExposedHeader(BasicText.refreshTokenHeaderName);
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
