package com.paulofranklins.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessTest {

    private CustomerJPADataAccessService undeTest;
    private AutoCloseable autoCloseable;
    private static Faker faker;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        autoCloseable = MockitoAnnotations.openMocks(this);
        undeTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //When
        undeTest.selectAllCustomers();

        //Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        var id = 10;

        //When
        undeTest.selectCustomerById(id);

        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        var name = faker.name().fullName();
        var email = faker.internet().emailAddress();
        Customer customer = new Customer(name, email, 28, Gender.MALE);

        //When
        undeTest.insertCustomer(customer);

        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        var email = faker.internet().emailAddress();

        //When
        undeTest.existsCustomerByEmail(email);

        //Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomer() {
        //Given
        var id = 2;

        //When
        undeTest.deleteCustomer(id);

        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void existsCustomerById() {
        //Given
        var id = 2;

        //When
        undeTest.existsCustomerById(id);

        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        var name = faker.name().fullName();
        var email = faker.internet().emailAddress();
        Customer customer = new Customer(name, email, 28, Gender.MALE);

        //When
        undeTest.updateCustomer(customer);

        //Then
        verify(customerRepository).save(customer);

    }
}