package com.paulofranklins.customer.journey;

import com.github.javafaker.Faker;
import com.paulofranklins.customer.*;
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
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {
    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private static final String PATH = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {
        //create registration request
        var faker = new Faker();
        var name = faker.name().fullName();
        var email = UUID.randomUUID() + "@gmail.com";
        var age = RANDOM.nextInt(1, 100);
        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        var request = new CustomerRegistrationRequest(
                name, email, "password", age, gender);

        //send a post request
        var jwtToken = webTestClient.post()
                .uri(PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //get all customers
        var allCustomers = webTestClient.get()
                .uri(PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk().expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();


        var id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        //make sure that customer is present
        var expected = new CustomerDTO(
                id, name, email, gender, age, List.of("ROLE_USER"), email
        );

        assertThat(allCustomers).contains(expected);

        //get customer by id
        webTestClient.get()
                .uri(PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .isEqualTo(expected);
    }

    @Test
    void canDeleteCustomer() {
        //create registration request
        var faker = new Faker();
        var name = faker.name().fullName();
        var email = UUID.randomUUID() + "@gmail.com";
        var age = RANDOM.nextInt(1, 100);
        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        var request = new CustomerRegistrationRequest(
                name, email, "password", age, gender);
        var request2 = new CustomerRegistrationRequest(
                name, email + "@@" + UUID.randomUUID(), "password", age, gender);

        //POST tCustomer to be deleted
        webTestClient.post()
                .uri(PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //POST Customer 2
        var jwtToken = webTestClient.post()
                .uri(PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //get all customers
        var allCustomers = webTestClient.get()
                .uri(PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        //customer 2 delete Customer 1
        webTestClient.delete()
                .uri(PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        //Customer 2 get Customer 1 by id
        webTestClient.get()
                .uri(PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus();
    }

    @Test
    void canUpdateCustomer() {
        //create registration request
        var faker = new Faker();
        var name = faker.name().fullName();
        var email = UUID.randomUUID() + "@gmail.com";
        var age = RANDOM.nextInt(1, 100);
        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        var request = new CustomerRegistrationRequest(
                name, email, "password", age, gender);

        //send a post request
        var jwtToken = webTestClient.post()
                .uri(PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //get all customers
        var allCustomers = webTestClient.get()
                .uri(PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        //update customer
        var newName = "Paulo";
        var updateRequest = new CustomerUpdateRequest(
                newName, null, null);

        webTestClient.put()
                .uri(PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .contentType(APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customer by id
        var updatedCustomer = webTestClient.get()
                .uri(PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        var expected = new CustomerDTO(
                id, newName, email, gender, age, List.of("ROLE_USER"), email);
        assertThat(updatedCustomer).isEqualTo(expected);
    }
}
