package com.book.springboot.config.auth;

import com.book.springboot.config.auth.dto.OAuthAttributes;
import com.book.springboot.config.auth.dto.SessionUser;
import com.book.springboot.domain.user.User;
import com.book.springboot.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * registrationId
         * 현재 로그인 진행 중인 서비스를 구분하는 코드. 구글/네이버/카카오 로그인 등을 구분함.
         */
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        /**
         * userNameAttributeName
         * OAuth2 로그인 진행 시 키가 되는 필드값. Primary key와 같은 의미
         * 구글의 경우 기본적으로 코드를 지원하지만, 네이버/카카오는 지원하지 않음. 구글 기본 코드("sub")
         * 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용 예정
         */
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        /**
         * OAuthAttributes
         * OAuth2UserService를 통해 가져온 OAuth2User의 attribute(속성)를 담을 클래스
         * 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용함.
         */
        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);  // 구글 사용자 정보가 업데이트 되었을 때를 대비한 update 기능.

        /**
         * SessionUser
         * 세션에 사용자 정보를 저장하기 위한 Dto 클래스
         */
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())  // email로 user 검색
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))  // 검색한 user를 업데이트.
                .orElse(attributes.toEntity());  // email로 유저 찾아봤는데 없으면 게스트

        return userRepository.save(user);
    }
}
