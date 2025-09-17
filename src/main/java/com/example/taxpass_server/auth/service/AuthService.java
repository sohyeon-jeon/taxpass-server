package com.example.taxpass_server.auth.service;

import com.example.taxpass_server.auth.dto.KakaoLoginRequest;
import com.example.taxpass_server.auth.dto.KakaoUserInfoResponse;
import com.example.taxpass_server.auth.dto.LoginResponse;
import com.example.taxpass_server.auth.util.JwtUtil;
import com.example.taxpass_server.entity.User;
import com.example.taxpass_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final WebClient webClient;

    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestApiKey;

    @Value("${app.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${app.kakao.user-info-uri}")
    private String kakaoUserInfoUri;

    public LoginResponse login(KakaoLoginRequest request) {
        String kakaoAccessToken;

//        카카오 accessToken 발급
        if ("web".equalsIgnoreCase(request.getPlatform())) {
            kakaoAccessToken = getAccessTokenFromKakao(request.getCode());
        } else {
            kakaoAccessToken = request.getAccessToken();
        }

//        발급받은 accessToken으로 사용자 정보 조회
        KakaoUserInfoResponse userInfo = getUserInfoFromKakao(kakaoAccessToken);


//        해당 카카오 id가 taxPass앱에 가입된 사용자인지 확인
//        가입되지 않는 신규 사용자일 경우, 카카오 정보 데이터베이스에 저장
        User user = userRepository.findByKakaoId(userInfo.getId())
                .orElseGet(() -> saveNewUser(userInfo));

//        위 과정이 모두 성공하면, jwt 발급
        String serviceToken = jwtUtil.generateToken(user.getId());

        return LoginResponse.builder()
                .accessToken(serviceToken)
                .user(user)
                .build();
    }

    private String getAccessTokenFromKakao(String code) {
        Map<String, Object> response = webClient.post()
                .uri(kakaoTokenUri)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue("grant_type=authorization_code&client_id=" + kakaoRestApiKey + "&code=" + code)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return (String) response.get("access_token");
    }

    private KakaoUserInfoResponse getUserInfoFromKakao(String accessToken) {
        return webClient.get()
                .uri(kakaoUserInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }

    private User saveNewUser(KakaoUserInfoResponse userInfo) {
//        System.out.println("userInfo.getNickname() = " + userInfo.getNickname());
//        System.out.println("userInfo.getProfileImageUrl() = " + userInfo.getProfileImageUrl());
        User user = User.builder()
                .kakaoId(userInfo.getId())
                .nickname(userInfo.getNickname())
                .email(userInfo.getEmail())
                .profileImageUrl(userInfo.getProfileImageUrl())
                .build();
        return userRepository.save(user);
    }
}
