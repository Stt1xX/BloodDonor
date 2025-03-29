package com.bloodlink.security;

import com.bloodlink.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.time.Duration;

@Data
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final UserService userService;

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/login", "/api/register/**", "/api/organizations/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/organizations/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handle -> handle
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .formLogin((form) -> {
                    form.loginPage("/login");
                    form.loginProcessingUrl("/api/auth/login");
                    form.usernameParameter("username");
                    form.passwordParameter("password");
                    form.successHandler(successHandler);
                    form.failureHandler(failureHandler);
                })
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds((int) Duration.ofHours(12).getSeconds())
                        .rememberMeCookieName("REMEMBER_ME")
                        .alwaysRemember(true)
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()

                );

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            com.bloodlink.entities.User user = userService.getByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("Пользователь не найден");
            }
            if (user.getIsDeleted()) {
                throw new DisabledException("Пользователь удален");
            }
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(user.getRole().toString())
                    .passwordEncoder(password -> password)
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sha256PasswordEncoder();
    }
}
