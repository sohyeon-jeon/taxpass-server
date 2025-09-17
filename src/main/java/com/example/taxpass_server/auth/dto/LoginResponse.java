package com.example.taxpass_server.auth.dto;

import com.example.taxpass_server.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final UserInfo userInfo;

    @Builder
    public LoginResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.userInfo = UserInfo.from(user);
    }

    @Getter
    @ToString
    private static class UserInfo {
        private final Long id;
        private final String nickname;
        private final String email;
        private final String profileImageUrl;

        private UserInfo(Long id, String nickname, String email, String profileImageUrl) {
            this.id = id;
            this.nickname = nickname;
            this.email = email;
            this.profileImageUrl = profileImageUrl;
        }

        public static UserInfo from(User user) {
            return new UserInfo(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImageUrl());
        }
    }
}
