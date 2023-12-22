package com.paulofranklins.customer.journey;

import com.github.javafaker.Faker;
import com.paulofranklins.auth.AuthenticationRequest;
import com.paulofranklins.auth.AuthenticationResponse;
import com.paulofranklins.customer.CustomerRegistrationRequest;
import com.paulofranklins.customer.Gender;
import com.paulofranklins.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIT {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JWTUtil jwtUtil;
    private static final Random RANDOM = new Random();
    private static final String AUTHENTICATION_PATH = "/api/v1/auth/login";
    private static final String CUSTOMER_PATH = "/api/v1/customers";

    @Test
    void canLogin() {
        //create registration request
        var faker = new Faker();
        var name = faker.name().fullName();
        var email = UUID.randomUUID() + "@gmail.com";
        var age = RANDOM.nextInt(1, 100);
        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        var password = "password";

        var customerRegistrationRequest = new CustomerRegistrationRequest(
                name, email, password, age, gender);
        var authenticationRequest = new AuthenticationRequest(email, password);

        webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(
                                authenticationRequest),
                        AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        //send a post request
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        var result = webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();

        var jwtToken = result
                .getResponseHeaders()
                .getFirst(AUTHORIZATION);

        var authenticationResponse = result.getResponseBody();
        var customerDTO = authenticationResponse.customerDTO();

        assertThat(jwtUtil.isTokenValid(jwtToken, customerDTO.username())).isTrue();
        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.name()).isEqualTo(name);
        assertThat(customerDTO.username()).isEqualTo(email);
        assertThat(customerDTO.gender()).isEqualTo(gender);
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));
    }
}
