package com.paulofranklins.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
