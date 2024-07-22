package com.fourbao.bookbao.backend.common.config;

import com.fourbao.bookbao.backend.filter.JwtAuthorizationFilter;
import com.fourbao.bookbao.backend.repository.UserRepository;
import com.fourbao.bookbao.backend.service.BookBaoPrincipalService;
import com.fourbao.bookbao.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BookBaoPrincipalService bookBaoPrincipalService;

    // spring security 보안 규칙 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf((csrf) -> csrf.disable())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
//                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 resources 접근 허용 설정
                                .requestMatchers("/api/v1/login").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
                )
                .addFilterBefore(new JwtAuthorizationFilter(userRepository, jwtUtils), BasicAuthenticationFilter.class)
                .userDetailsService(bookBaoPrincipalService);


        return httpSecurity.build();
    }

//    // NON_AUTHENTICATED에 담긴 패턴은 ignore
//    @Bean
//    public WebSecurityCustomizer securityCustomizer() {
//        final String[] NON_AUTHENTICATED = {
//                "/api/v1/login", "/swagger-ui/**", "/v3/**","/swagger-ui.html"
//        };
//        return web -> {
//            web.ignoring().requestMatchers(NON_AUTHENTICATED);
//        };
//    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // e.g. http://domain1.com
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}