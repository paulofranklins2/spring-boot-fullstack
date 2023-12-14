//package com.paulofranklins.customer.journey;
//
//import com.github.javafaker.Faker;
//import com.paulofranklins.customer.Customer;
//import com.paulofranklins.customer.CustomerRegistrationRequest;
//import com.paulofranklins.customer.CustomerUpdateRequest;
//import com.paulofranklins.customer.Gender;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Mono;
//
//import java.util.Random;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
//
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//public class CustomerIntegrationTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//    private static final Random RANDOM = new Random();
//    private static final String PATH = "/api/v1/customers";
//
//    @Test
//    void canRegisterCustomer() {
//        //create registration request
//        var faker = new Faker();
//
//        var name = faker.name().fullName();
//        var email = UUID.randomUUID() + "@gmail.com";
//        var age = RANDOM.nextInt(1, 100);
//        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
//
//
//        var request = new CustomerRegistrationRequest(name, email, "password", age, gender);
//
//        //send a post request
//        webTestClient.post()
//                .uri(PATH)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), CustomerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        //get all customers
//        var allCustomers = webTestClient.get()
//                .uri(PATH)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Customer>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        //make sure that customer is present
//        var expected = new Customer(name, email, "password", age, gender);
//        var id = allCustomers.stream()
//                .filter(customer -> customer.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//        expected.setId(id);
//
//        assertThat(allCustomers)
//                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
//                .contains(expected);
//
//        //get customer by id
//        webTestClient.get()
//                .uri(PATH + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(new ParameterizedTypeReference<Customer>() {
//                })
//                .isEqualTo(expected);
//    }
//
//    @Test
//    void canDeleteCustomer() {
//        //create registration request
//        var faker = new Faker();
//
//        var name = faker.name().fullName();
//        var email = UUID.randomUUID() + "@gmail.com";
//        var age = RANDOM.nextInt(1, 100);
//        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
//
//        var request = new CustomerRegistrationRequest(name, email, "password", age, gender);
//
//        //send a post request
//        webTestClient.post()
//                .uri(PATH)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), CustomerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        //get all customers
//        var allCustomers = webTestClient.get()
//                .uri(PATH)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Customer>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        var id = allCustomers.stream()
//                .filter(customer -> customer.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        //delete customer
//        webTestClient.delete()
//                .uri(PATH + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        //get customer by id
//        webTestClient.get()
//                .uri(PATH + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isNotFound();
//    }
//
//    @Test
//    void canUpdateCustomer() {
//        //create registration request
//        var faker = new Faker();
//
//        var name = faker.name().fullName();
//        var email = UUID.randomUUID() + "@gmail.com";
//        var age = RANDOM.nextInt(1, 100);
//        var gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
//
//        var request = new CustomerRegistrationRequest(name, email, "password", age, gender);
//
//        //send a post request
//        webTestClient.post()
//                .uri(PATH)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), CustomerRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        //get all customers
//        var allCustomers = webTestClient.get()
//                .uri(PATH)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Customer>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//        var id = allCustomers.stream()
//                .filter(customer -> customer.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        //update customer
//        var newName = "Paulo";
//
//        var updateRequest = new CustomerUpdateRequest(newName, null, null);
//
//        webTestClient.put()
//                .uri(PATH + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        //get customer by id
//        var updatedCustomer = webTestClient.get()
//                .uri(PATH + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Customer.class)
//                .returnResult()
//                .getResponseBody();
//
//        var expected = new Customer(id, newName, email, "password", age, gender);
//
//        assertThat(updatedCustomer).isEqualTo(expected);
//    }
//}
