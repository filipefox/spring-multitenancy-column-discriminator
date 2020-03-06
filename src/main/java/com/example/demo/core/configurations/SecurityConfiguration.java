package com.example.demo.core.configurations;

import com.example.demo.core.filters.TenantAuthorizationFilter;
import com.example.demo.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TenantAuthorizationFilter tenantAuthorizationFilter;

    @Value("${cors.allowed.origins}")
    private String corsAllowedOrigins;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/signIn/**").permitAll()
                .antMatchers("/api/tenants/**").hasAuthority(Constants.SUPER_ADMIN_ROLE_NAME)
                .antMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority(Constants.SUPER_ADMIN_ROLE_NAME, Constants.ADMIN_ROLE_NAME, Constants.USER_ROLE_NAME)
                .antMatchers("/api/**").hasAnyAuthority(Constants.SUPER_ADMIN_ROLE_NAME, Constants.ADMIN_ROLE_NAME)
                .and()
                .addFilterBefore(tenantAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins.split(",")));
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PATCH", "DELETE"));
        corsConfiguration.applyPermitDefaultValues();

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}