package com.bloodlink.security;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.time.Duration;

@Data
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailureHandler failureHandler;

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/csrf", "/api/register/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling((handle) -> handle.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
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
                        .rememberMeCookieName("REMEMBERME")
                        .alwaysRemember(true)
                );
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()

//                )
//                .exceptionHandling(handler ->
//                        handler.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return email -> {
//            User user = userRepository.findByEmail(email);
//            if (user == null) {
//                throw new UsernameNotFoundException("User not found");
//            }
//            return org.springframework.security.core.userdetails.User
//                    .withUsername(user.getEmail())
//                    .password(user.getPassword())
//                    .authorities(user.getRole().name())
//                    .passwordEncoder(password -> password)
//                    .build();
//        };
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("ramtim1@mail.ru")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Sha256PasswordEncoder();
    }
}
