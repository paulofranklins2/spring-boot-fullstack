package com.paulofranklins.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class CustomerJPADataAccessTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    private static Faker faker;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // Mocking
        var page = mock(Page.class);
        var customers = List.of(new Customer());
        when(page.getContent()).thenReturn(customers);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Execution
        var expected = underTest.selectAllCustomers();

        // Verification
        assertThat(expected).isEqualTo(customers);

        // Verify the invocation with the correct Pageable argument
        var pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerRepository).findAll(pageableArgumentCaptor.capture());

        // Assert the captured Pageable has the expected size
        assertThat(pageableArgumentCaptor.getValue().getPageSize()).isEqualTo(100);
    }


    @Test
    void selectCustomerById() {
        //Given
        var id = 10;

        //When
        underTest.selectCustomerById(id);

        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        var name = faker.name().fullName();
        var email = faker.internet().emailAddress();
        var customer = new Customer(name, email, "password", 28, Gender.MALE);

        //When
        underTest.insertCustomer(customer);

        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerByEmail() {
        //Given
        var email = faker.internet().emailAddress();

        //When
        underTest.existsCustomerByEmail(email);

        //Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomer() {
        //Given
        var id = 2;

        //When
        underTest.deleteCustomer(id);

        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void existsCustomerById() {
        //Given
        var id = 2;

        //When
        underTest.existsCustomerById(id);

        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        var name = faker.name().fullName();
        var email = faker.internet().emailAddress();
        var customer = new Customer(name, email, "password", 28, Gender.MALE);

        //When
        underTest.updateCustomer(customer);

        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void canUpdateProfileImageId() {
        // Given
        var profileImageId = "2222";
        var customerId = 1;

        // When
        underTest.updateCustomerProfileImageId(profileImageId, customerId);

        // Then
        verify(customerRepository).updateProfileImageId(profileImageId, customerId);
    }
}