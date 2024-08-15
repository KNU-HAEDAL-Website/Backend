package com.haedal.haedalweb.config;

import com.haedal.haedalweb.constants.LoginConstants;
import com.haedal.haedalweb.exception.FilterExceptionHandler;
import com.haedal.haedalweb.jwt.CustomLogoutFilter;
import com.haedal.haedalweb.jwt.JWTFilter;
import com.haedal.haedalweb.jwt.JWTUtil;
import com.haedal.haedalweb.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) throws Exception {

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList(LoginConstants.ACCESS_TOKEN));

                        return configuration;
                    }
                })));


        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/**").hasAnyRole("WEB_MASTER", "ADMIN")
                        .requestMatchers("/boards/generate-presigned-url").hasAnyRole("WEB_MASTER", "ADMIN", "TEAM_LEADER")
                        .requestMatchers(HttpMethod.GET, "/posts/generate-presigned-url").hasAnyRole("WEB_MASTER", "ADMIN", "TEAM_LEADER", "MEMBER")
                        .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole("WEB_MASTER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/posts/{postId}").hasAnyRole("WEB_MASTER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/activities/{activityId}/boards").hasAnyRole("WEB_MASTER", "ADMIN", "TEAM_LEADER")
                        .requestMatchers(HttpMethod.POST, "/boards/{boardId}/posts").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/boards/{boardId}/posts/{postId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/activities/{activityId}/boards/{boardId}").hasAnyRole("WEB_MASTER", "ADMIN", "TEAM_LEADER")
                        .requestMatchers(HttpMethod.PATCH, "/activities/{activityId}/boards/{boardId}/**").hasAnyRole("WEB_MASTER", "ADMIN", "TEAM_LEADER")
                        .requestMatchers("/private/users").authenticated()
                        .requestMatchers("/posts/slider", "/posts/{postId}","/boards/{boardId}/posts", "/posts","/activities/{activityId}/boards","/activities/{activityId}/boards/{boardId}","/login", "/", "/join/**", "/reissue", "/swagger-ui/**", "/v3/api-docs/**", "/users/**","/semesters/**").permitAll()
                        .anyRequest().authenticated());

        //JWTFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new FilterExceptionHandler(), LogoutFilter.class);

        http
                .logout((auth) -> auth.disable());

        http
                .addFilterAfter(new CustomLogoutFilter(jwtUtil), LogoutFilter.class);


        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}