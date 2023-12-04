package com.paulofranklins.customer;

import com.paulofranklins.exceptions.DuplicateResourceException;
import com.paulofranklins.exceptions.RequestValidationException;
import com.paulofranklins.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServicesTest {
    @Mock
    private CustomerDAO customerDAO;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomer() {
        //When
        underTest.getAllCustomer();
        //Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        var id = 2;
        var customer = new Customer(
                id, "paulo",
                "paulo@gmail.com", 28);

        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        //When
        var actual = underTest.getCustomer(2);

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //Given
        var id = 2;
        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.empty());
        //Then
        assertThatThrownBy(
                () -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "Customer with ID: [%s], Not Found"
                                .formatted(id));

//        verify(customerDAO, never()).selectCustomerById(any());

    }

    @Test
    void addCustomer() {
        //Given
        var email = "paulo@gmail.com";
        when(customerDAO.existsCustomerByEmail(email)).thenReturn(false);

        var request = new CustomerRegistrationRequest(
                "paulo", email, 20
        );
        //When
        underTest.addCustomer(request);

        //Then
        var argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).insertCustomer(
                argumentCaptor.capture()
        );

        var capturedValue = argumentCaptor.getValue();
        assertThat(capturedValue.getId()).isNull();
        assertThat(capturedValue.getName()).isEqualTo(request.name());
        assertThat(capturedValue.getEmail()).isEqualTo(request.email());
        assertThat(capturedValue.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenAddCustomerAndEmailAlreadyInUse() {
        //Given
        var email = "hello@gmail.com";
        when(customerDAO.existsCustomerByEmail(email))
                .thenReturn(true);
        var customer = new CustomerRegistrationRequest(
                "paulo", email, 29
        );

        //When
        assertThatThrownBy(
                () -> underTest.addCustomer(customer))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage(
                        "Customer with Email: [%s], already in use"
                                .formatted(email));

        //Then
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        //Given
        var id = 1;
        when(customerDAO.existsCustomerById(id)).thenReturn(true);

        //When
        underTest.deleteCustomer(id);

        //Then
        verify(customerDAO).deleteCustomer(id);
    }

    @Test
    void willThrowWhenTryingToDeleteCustomerInCaseIdNotExist() {
        //Given
        var id = 1;
        when(customerDAO.existsCustomerById(id)).thenReturn(false);

        //When
        assertThatThrownBy(
                () -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "Customer with ID: [%s], Not Found"
                                .formatted(id));

        //Then
        verify(customerDAO, never()).deleteCustomer(any());

    }

    @Test
    void canUpdateAllCustomerProperties() {
        //Given
        var id = 10;
        var customer = new Customer(
                id, "10", "10", 10);
        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        //When
        var updateRequest = new CustomerUpdateRequest(
                "paulo", "paulo@gmail.com", 20);
        when(customerDAO.existsCustomerByEmail(
                updateRequest.email())).thenReturn(false);
        underTest.updateCustomer(id, updateRequest);

        //Then
        var argCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(argCaptor.capture());
        var capturedCustomerValue = argCaptor.getValue();

        assertThat(capturedCustomerValue.getName())
                .isEqualTo(updateRequest.name());
        assertThat(capturedCustomerValue.getEmail())
                .isEqualTo(updateRequest.email());
        assertThat(capturedCustomerValue.getAge())
                .isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //Given
        var id = 10;
        var customer = new Customer(
                id, "10", "10", 10);
        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        //When
        var updateRequest = new CustomerUpdateRequest(
                "paulo", null, null);
        underTest.updateCustomer(id, updateRequest);

        //Then
        var argCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(argCaptor.capture());
        var capturedCustomerValue = argCaptor.getValue();

        assertThat(capturedCustomerValue.getName())
                .isEqualTo(updateRequest.name());
        assertThat(capturedCustomerValue.getEmail())
                .isEqualTo(customer.getEmail());
        assertThat(capturedCustomerValue.getAge())
                .isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //Given
        var id = 10;
        var customer = new Customer(
                id, "10", "10", 10);
        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        //When
        var updateRequest = new CustomerUpdateRequest(
                null, null, 30);
        underTest.updateCustomer(id, updateRequest);

        //Then
        var argCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(argCaptor.capture());
        var capturedCustomerValue = argCaptor.getValue();

        assertThat(capturedCustomerValue.getName())
                .isEqualTo(customer.getName());
        assertThat(capturedCustomerValue.getEmail())
                .isEqualTo(customer.getEmail());
        assertThat(capturedCustomerValue.getAge())
                .isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //Given
        var id = 10;
        var customer = new Customer(
                id, "10", "10", 10);
        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        //When
        var updateRequest = new CustomerUpdateRequest(
                null, "teste@gmail.com", null);

        when(customerDAO.existsCustomerByEmail(
                updateRequest.email())).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);
        //Then
        var argCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(argCaptor.capture());
        var capturedCustomerValue = argCaptor.getValue();

        assertThat(capturedCustomerValue.getName())
                .isEqualTo(customer.getName());
        assertThat(capturedCustomerValue.getEmail())
                .isEqualTo(updateRequest.email());
        assertThat(capturedCustomerValue.getAge())
                .isEqualTo(customer.getAge());
    }

    @Test
    void willThrowWhenTryingUpdateCustomerWithEmail() {
        //Given
        var id = 10;
        var customer = new Customer(
                id, "10", "10", 10);
        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        //When
        var updateRequest = new CustomerUpdateRequest(
                null, "paulo@gmail.com", null);

        when(customerDAO.existsCustomerByEmail(
                updateRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage(
                        "Customer with Email: [%s], already in use"
                                .formatted(updateRequest.email())
                );

        //Then
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenNoDataChangesAreMadeFromTheCurrentData() {
        //Given
        var id = 10;
        var customer = new Customer(
                10, "10", "10", 10);
        when(customerDAO.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        var updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());
        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("There are no alterations to the existing data.");
        //Then
        verify(customerDAO, never()).updateCustomer(any());
    }
}