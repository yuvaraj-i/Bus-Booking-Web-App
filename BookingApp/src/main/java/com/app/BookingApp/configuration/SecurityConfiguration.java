package com.app.BookingApp.configuration;

import com.app.BookingApp.services.MyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private MyUserService userService;
    private RequestFilter requstFilter;

    @Autowired
    public SecurityConfiguration(MyUserService userService, RequestFilter requstFilter) {
        this.userService = userService;
        this.requstFilter = requstFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests()
                    .antMatchers("/auth/signup").permitAll()
                    .antMatchers("/auth/login").permitAll()
                    .antMatchers("/booking/**", "/bus/**").hasAnyAuthority("user", "admin")
                    .antMatchers("/user/**").hasAnyAuthority("user")
                    .antMatchers("/admin/**").hasAnyAuthority("admin")
                    .anyRequest().authenticated().and()
                    .exceptionHandling().and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(requstFilter, UsernamePasswordAuthenticationFilter.class);

    }
}