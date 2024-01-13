package com.book.springboot.config.auth;

import com.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf()~.disable(): h2-console 화면 사용을 위해 해당 옵션들을 disable 함

                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .authorizeHttpRequests(auth -> auth // URL별 권한 관리를 설정하는 옵션의 시작점
                        // requestMatchers
                        // 권한 관리 대상을 지정하는 옵션. URL, HTTP 메소드 별로 관리가 가능하며 "/" 등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한을 주었다.
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/images/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/h2-console/**"),
                                new AntPathRequestMatcher("/profile")
                        ).permitAll()
                        // "/api/v1/**"의 주소일 경우 USER 권한을 가진 사람만 열람 가능하도록 했다.
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/**")).hasRole(Role.USER.name())
                        // anyRequest()
                        // 설정된 값들 이외 나머지 URL을 나타냄.
                        // 여기서는 authenticated()를 추가하여 나머지 URL일 경우 모두 인증된 사용자들(로그인한 사용자)에게만 허용하게 함.
                        .anyRequest().authenticated())

                .logout((logout) -> logout
                        .logoutSuccessUrl("/")) // 로그아웃 성공시 이동할 주소

                .oauth2Login((oauth2) -> oauth2 // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                                // userService()
                                // 소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록.
                                // 리소스 서버(소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.
                                .userService(customOAuth2UserService))
                        .defaultSuccessUrl("/", true));

        return http.build();
    }
}
