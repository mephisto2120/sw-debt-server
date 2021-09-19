package com.tryton.small_world.server.config;

import com.tryton.small_world.server.auth.JwtFilter;
import com.tryton.small_world.server.auth.UsernamePasswordAuthenticationCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static java.util.Objects.requireNonNull;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsernamePasswordAuthenticationCreator authenticationCreator;

    public WebSecurityConfig(UsernamePasswordAuthenticationCreator authenticationCreator) {
        this.authenticationCreator = requireNonNull(authenticationCreator);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test2").authenticated()
                .antMatchers("/test3").hasAuthority("ADMIN")
//                .antMatchers("/test3").hasRole("ADMIN")
                .and()
                .addFilter(jwtFilter());
    }

    @Bean
    public JwtFilter jwtFilter() throws Exception {
        return new JwtFilter(authenticationManager(), authenticationCreator);
    }
}
