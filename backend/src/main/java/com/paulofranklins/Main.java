package com.paulofranklins;


import com.github.javafaker.Faker;
import com.paulofranklins.customer.Customer;
import com.paulofranklins.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            var random = new Random();
            var firstName = faker.name().firstName();
            var lastName = faker.name().lastName();

            var customer = new Customer(
                    firstName + " " + lastName,
                    firstName.toLowerCase() +
                            "." +
                            lastName.toLowerCase() +
                            "@gmail.com",
                    random.nextInt(21, 80));

            customerRepository.save(customer);
        };
    }
}
