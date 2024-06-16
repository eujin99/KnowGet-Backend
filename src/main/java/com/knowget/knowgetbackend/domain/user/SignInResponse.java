package com.knowget.knowgetbackend.domain.user;

public record SignInResponse(String username, String role, String token) {
}
