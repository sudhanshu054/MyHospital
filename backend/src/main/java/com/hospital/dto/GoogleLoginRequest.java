package com.hospital.dto;

import lombok.Data;

@Data
public class GoogleLoginRequest {
    private String credential;
    private String accessToken;

    public boolean hasGoogleToken() {
        return (credential != null && !credential.isBlank())
                || (accessToken != null && !accessToken.isBlank());
    }
}
