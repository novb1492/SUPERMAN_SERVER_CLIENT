package com.kimcompany.jangbogbackendver2.Config;


import com.kimcompany.jangbogbackendver2.Filter.AuthorizationFilter;
import com.kimcompany.jangbogbackendver2.Filter.CorsConfig;
import com.kimcompany.jangbogbackendver2.Filter.LoginFilter;
import com.kimcompany.jangbogbackendver2.Util.AuthorizationService;
import com.kimcompany.jangbogbackendver2.Util.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration//빈등록: 스프링 컨테이너에서 객체에서 관리
@EnableWebSecurity/////필터를 추가해준다
@RequiredArgsConstructor
public class SecurityConfig  {

    private final CorsConfig corsConfig;
    private final AuthenticationConfiguration authenticationConfiguration;

    private final LoginService loginService;
    private final AuthorizationService authorizationService;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder pwdEncoder() {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()
                .addFilter(corsConfig.corsfilter())
                .addFilter(new LoginFilter(loginService,authenticationManager(authenticationConfiguration)))
                .addFilter(new AuthorizationFilter(authenticationManager(authenticationConfiguration), authorizationService))
                .formLogin().disable().httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/login","/token-expire/**","/login-fail/**","/ws/**","/kg/**").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/manage/**").hasAnyAuthority("MANAGE","ADMIN")
                .anyRequest().authenticated()
                ;

        return http.build();

    }
}
