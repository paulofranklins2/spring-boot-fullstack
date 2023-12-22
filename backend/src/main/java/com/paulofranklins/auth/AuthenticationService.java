package com.paulofranklins.auth;

import com.paulofranklins.customer.Customer;
import com.paulofranklins.customer.CustomerDTOMapper;
import com.paulofranklins.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 CustomerDTOMapper customerDTOMapper,
                                 JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest r) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        r.username(),
                        r.password()
                )
        );
        Customer principal = (Customer) authentication.getPrincipal();
        var customerDTO = customerDTOMapper.apply(principal);
        var token = jwtUtil.issueToken(customerDTO.username(), customerDTO.roles());
        return new AuthenticationResponse(token, customerDTO);
    }
}
