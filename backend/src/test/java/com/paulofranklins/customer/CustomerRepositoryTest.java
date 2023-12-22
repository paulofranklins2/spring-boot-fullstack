package com.paulofranklins.customer;

import com.paulofranklins.AbstractTestContainers;
import com.paulofranklins.TestConfig;
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


    @Test
    void existsCustomerByEmail() {
        //Given
        var name = faker.name().fullName();
        var email = faker.internet().emailAddress() + "_" + UUID.randomUUID();
        Customer c = new Customer(name, email, "password", 28, Gender.MALE);
        underTest.save(c);

        //When
        var actual = underTest.existsCustomerByEmail(c.getEmail());

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailWillReturnFalseWhenItDoesNot() {
        //Given
        var email = faker.internet().emailAddress() + "_" + UUID.randomUUID();

        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        var name = faker.name().fullName();
        var email = faker.internet().emailAddress() + "_" + UUID.randomUUID();
        Customer c = new Customer(name, email, "password", 28, Gender.MALE);
        underTest.save(c);

        //When
        var actual = underTest.existsCustomerById(c.getId());
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdWillReturnFalseWhenItDoesNot() {
        //Given
        var id = -1;

        //When
        var actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isFalse();
    }
}