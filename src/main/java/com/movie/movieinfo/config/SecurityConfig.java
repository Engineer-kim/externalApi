package com.movie.movieinfo.config;

import com.movie.movieinfo.service.user.OAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA256PasswordEncoder();
    }
    @Bean
    public OAuth2Service customOAuth2Service(){
        return new OAuth2Service();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 기본적으로 oauth2Login 성공시 / 루트 URL 로 리턴됨 커스텀해서 사용해야함
                //ex) .successHandler(new SimpleUrlAuthenticationSuccessHandler("/api/auth/loginSuccess"))
                .authorizeHttpRequests(authorize -> authorize
                        //하위 엔드포인트는 미인증이여도 접근가능하게끔
                        .requestMatchers("/api/user/v1/findUserId",
                                "/api/user/v1/duplicateCheckId", "/api/user/v1/register" ,"/api/auth/commonLogin"
                                ,"/api/auth/login",
                                "/api/auth/loginPoc",
                                "/movieInfo/**" , "/movieInfoMain/**"  ,"/userManagement/**"
                        ,"/api/user/v1/resetPassword/request" ,"/api/user/v1/resetPassword"
                       )
                        .permitAll()
                        .requestMatchers(request ->
                                "XMLHttpRequest".equals(request.getHeader("X-Requested-With")))
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/api/auth/commonLogin")
                        .loginProcessingUrl("/api/auth/loginProc")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .successHandler(
                                new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        System.out.println("authentication::::::: " + authentication.getName());
                                        response.sendRedirect("/api/auth/loginSuccess");
                                    }
                                })
                        .failureHandler(
                                new AuthenticationFailureHandler() {
                                    @Override
                                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                        System.out.println("exception::::::::::::: " + exception.getMessage());
                                        response.sendRedirect("/api/auth/commonLogin");
                                    }
                                })
                        .permitAll()

                )
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/api/auth/commonLogin")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2Service()))
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/api/auth/loginSuccess"))
                )
                .rememberMe(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/api/auth/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                );
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/login.html")
        );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 spring security 대상에서 제외
        return (web) -> {
            web
                .ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
        };
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }



}
