package com.terrasi.terrasiapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String secretKey;

    public WebSecurityConfig(@Value("${secret.key}") String secretKey){
        this.secretKey = secretKey;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users").authenticated()
                .antMatchers("/news").permitAll()
                .antMatchers(HttpMethod.POST,"/login").permitAll()
                .and()
                .addFilter(new JwtFilter(authenticationManager(),secretKey))
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();

    }
}
