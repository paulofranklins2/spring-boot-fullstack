package com.paulofranklins;


import com.github.javafaker.Faker;
import com.paulofranklins.customer.Customer;
import com.paulofranklins.customer.CustomerRepository;
import com.paulofranklins.customer.Gender;
import com.paulofranklins.s3.S3Buckets;
import com.paulofranklins.s3.S3Service;
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
            newRandomCustomer (customerRepository, passwordEncoder);
//            testBucketUploadAndDownload(s3Service, s3Buckets);
        };
    }

    private static void testBucketUploadAndDownload(S3Service s3Service,
                                                    S3Buckets s3Buckets) {
        s3Service.putObject(
                s3Buckets.getCustomer(),
                "foo",
                "Hello World".getBytes()
        );

        var obj = s3Service.getObject(
                s3Buckets.getCustomer(),
                "foo"
        );

        System.out.println("Hooray: " + new String(obj));
    }

    private static void newRandomCustomer(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        var faker = new Faker();
        var random = new Random();
        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();
        int age = random.nextInt(21, 80);
        var gender = age % 2 == 0 ? "MALE" : "FEMALE";
        var password = UUID.randomUUID().toString();
        var name = firstName + " " + lastName;
        var email = firstName.toLowerCase() + "." +
                lastName.toLowerCase() + "@gmail.com";

        var customer = new Customer(
                name,
                email,
                passwordEncoder
                        .encode(password),
                age,
                Gender.valueOf(gender));
        customerRepository
                .save(customer);

        System.out.println(email);
        System.out.println(password);
    }
}
