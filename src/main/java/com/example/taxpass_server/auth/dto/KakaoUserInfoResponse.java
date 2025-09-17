package com.example.taxpass_server.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@NoArgsConstructor
@ToString
public class KakaoUserInfoResponse {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @ToString
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @NoArgsConstructor
        @ToString
        public static class Profile {
            private String nickname;
            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
    public String getNickname() {
        return kakaoAccount.getProfile().getNickname();
    }


    public String getEmail() {
        return kakaoAccount.getEmail();
    }

        public String getProfileImageUrl() {
        return kakaoAccount.getProfile().getProfileImageUrl();
    }
    }
