package com.paulofranklins.customer;

import com.paulofranklins.AbstractTestContainers;
import com.paulofranklins.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        var customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.MALE);

        underTest.save(customer);

        // When
        var actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        // Given
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        var customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.save(customer);

        var id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        // Given
        var id = -1;

        // When
        var actual = underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void canUpdateProfileImageId() {
        // Given
        var email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        var customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);

        underTest.save(customer);

        var id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        underTest.updateProfileImageId("2222", id);

        var customerOptional = underTest.findById(id);
        assertThat(customerOptional)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(
                            c.getProfileImageId())
                            .isEqualTo("2222");
                });
    }
}