package com.paulofranklins;


import com.github.javafaker.Faker;
import com.paulofranklins.customer.Customer;
import com.paulofranklins.customer.CustomerRepository;
import com.paulofranklins.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            var faker = new Faker();
            var random = new Random();
            var firstName = faker.name().firstName();
            var lastName = faker.name().lastName();
            int age = random.nextInt(21, 80);
            var gender = age % 2 == 0 ? "MALE" : "FEMALE";
            var password = UUID.randomUUID().toString();

            var customer = new Customer(
                    firstName + " " + lastName,
                    firstName.toLowerCase() + "." +
                            lastName.toLowerCase() + "@gmail.com",
                    passwordEncoder.encode(password),
                    age, Gender.valueOf(gender));
            customerRepository.save(customer);
        };
    }
}
