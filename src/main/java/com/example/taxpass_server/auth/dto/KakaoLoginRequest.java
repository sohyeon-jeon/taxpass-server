package com.example.taxpass_server.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest {
    private String platform;
    private String code; // for web
    private String accessToken; // for native
}
