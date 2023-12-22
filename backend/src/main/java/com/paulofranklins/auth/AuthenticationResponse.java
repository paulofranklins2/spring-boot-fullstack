package com.paulofranklins.auth;

import com.paulofranklins.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {
}
