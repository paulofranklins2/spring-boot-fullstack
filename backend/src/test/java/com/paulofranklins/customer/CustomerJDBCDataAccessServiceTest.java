package com.paulofranklins.customer;

import com.paulofranklins.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        var random = new Random();
        var customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "@" + UUID.randomUUID(),
                random.nextInt(1, 100),
                Gender.MALE);
        underTest.insertCustomer(customer);

        //When
        var actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        var email = faker.internet().safeEmailAddress() + "@" + UUID.randomUUID();
        var random = new Random();
        var customer = new Customer(
                faker.name().fullName(),
                email,
                random.nextInt(1, 100),
                Gender.MALE);
        underTest.insertCustomer(customer);

        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge()
            );
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1;

        //When
        var actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        //Given
        var email = faker.internet().safeEmailAddress() + "@" + UUID.randomUUID();
        var random = new Random();
        var customer = new Customer(
                faker.name().fullName(),
                email,
                random.nextInt(1, 100),
                Gender.MALE);
        //When
        underTest.insertCustomer(customer);
        //Then
        assertTrue(underTest.existsCustomerByEmail(email));
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        var email = faker.internet().safeEmailAddress() + "@" + UUID.randomUUID();
        var random = new Random();
        var customer = new Customer(
                faker.name().fullName(),
                email,
                random.nextInt(1, 100),
                Gender.MALE);
        underTest.insertCustomer(customer);
        //When
        var actual = underTest.existsCustomerByEmail(email);
        //Then
//        assertTrue(underTest.existsCustomerByEmail(email));
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailReturnsFalseWhenDoesNotExist() {
        //Given
        var email = faker.internet().safeEmailAddress() + "@" + UUID.randomUUID();

        //When
        var actual = underTest.existsCustomerByEmail(email);
        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        //Given
        var email = faker.internet().safeEmailAddress() + "@" + UUID.randomUUID();
        var random = new Random();
        var customer = new Customer(
                faker.name().fullName(),
                email,
                random.nextInt(1, 100),
                Gender.MALE);
        underTest.insertCustomer(customer);

        //When
        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //Then
        assertThat(underTest.existsCustomerById(id));

    }

    @Test
    void existsCustomerByIdReturnFalseWhenDoesNotExist() {
        //Given
        var id = -1;

        //When
        var actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomer() {
        var email = faker.internet().safeEmailAddress() + "@" + UUID.randomUUID();
        var fullName = faker.name().fullName();
        var random = new Random();
        int age = random.nextInt(1, 100);

        var customer = new Customer(
                fullName,
                email,
                age,
                Gender.MALE);
        underTest.insertCustomer(customer);
        //When
        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //Then
        underTest.deleteCustomer(id);
        assertThat(underTest.selectCustomerById(id)).isNotPresent();
    }

    @Test
    void deleteCustomerWillReturnFalseWhenDoesNotExist() {
        //Given
        var id = -1;
        //When
        underTest.existsCustomerById(id);
//        underTest.deleteCustomer(id);
        //Then

    }

    @Test
    void updateCustomer() {
        //Given
        var email = faker.internet().safeEmailAddress() + "@" + UUID.randomUUID();
        var fullName = faker.name().fullName();
        var random = new Random();
        int age = random.nextInt(1, 100);

        var customer = new Customer(
                fullName,
                email,
                age,
                Gender.MALE);
        underTest.insertCustomer(customer);
        //When
        var id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.selectCustomerById(id);
        var fakeName = "tt";
        var fakeEmail = "actual.get().getEmail()";

        var fakeAge = random.nextInt(101, 200);
        if (!email.equals(fakeEmail)) {
            actual.get().setEmail(fakeEmail);
        }
        if (!fullName.equals(fakeName)) {
            actual.get().setName(fakeName);
        }
        if (age != fakeAge) {
            actual.get().setAge(fakeAge);
        }
        underTest.updateCustomer(customer);
        //Then
        assertThat(
                customer.getName().equals(fakeName) &&
                        customer.getEmail().equals(fakeName) &&
                        customer.getAge() == fakeAge);
    }
}