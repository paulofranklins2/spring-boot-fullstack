package com.paulofranklins.customer;

import com.paulofranklins.exceptions.DuplicateResourceException;
import com.paulofranklins.exceptions.RequestValidationException;
import com.paulofranklins.exceptions.ResourceNotFoundException;
import com.paulofranklins.s3.S3Buckets;
import com.paulofranklins.s3.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private S3Service s3Service;
    @Mock
    private S3Buckets s3Buckets;
    private CustomerService underTest;
    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO, customerDTOMapper, passwordEncoder, s3Service, s3Buckets);
    }

    @Test
    void getAllCustomer() {
        //When
        underTest.getAllCustomers();
        //Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        var id = 2;
        var customer =
                new Customer(
                        id, "paulo", "paulo", "password", 28, Gender.MALE
                );
        when(customerDAO
                .selectCustomerById(id))
                .thenReturn(Optional.of(customer)
                );
        var expected = customerDTOMapper.apply(customer);

        //When
        var actual = underTest.getCustomer(2);
        //Then
        assertThat(actual).isEqualTo(expected);
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
                "paulo", email, "password", 20, Gender.MALE
        );

        var passwordHash = "2!asdfasri2348@!#$asfdklajr1ijfasd";

        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);

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
        assertThat(capturedValue.getPassword()).isEqualTo(passwordHash);
    }

    @Test
    void willThrowWhenAddCustomerAndEmailAlreadyInUse() {
        //Given
        var email = "hello@gmail.com";
        when(customerDAO.existsCustomerByEmail(email))
                .thenReturn(true);
        var customer = new CustomerRegistrationRequest(
                "paulo", email, "password", 29, Gender.MALE
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
                id, "paulo", "password", "password", 10, Gender.MALE);
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
                id, "test", "password", "password", null, null);
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
                id, null, "password", "password", 10, null);
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
                id, null, "password", "password", null, null);
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
                id, null, "password", "password", 10, Gender.MALE);
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
                id, "10", "password", "password", 10, Gender.MALE);
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

    @Test
    void canUploadProfileImage() {
        // Given
        var customerId = 10;

        when(customerDAO.existsCustomerById(customerId)).thenReturn(true);

        var bytes = "Hello World".getBytes();

        var multipartFile = new MockMultipartFile("file", bytes);

        var bucket = "customer-bucket";
        when(
                s3Buckets.getCustomer())
                .thenReturn(bucket);

        // When
        underTest.uploadCustomerProfileImage(customerId, multipartFile);

        // Then
        var profileImageIdArgumentCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(customerDAO)
                .updateCustomerProfileImageId(
                        profileImageIdArgumentCaptor.capture(),
                        eq(customerId));

        verify(s3Service)
                .putObject(bucket,
                        "profile-images/%s/%s".formatted(customerId,
                                profileImageIdArgumentCaptor.getValue()),
                        bytes);
    }

    @Test
    void cannotUploadProfileImageWhenCustomerDoesNotExists() {
        // Given
        var customerId = 10;

        when(customerDAO.existsCustomerById(customerId)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.uploadCustomerProfileImage(
                customerId, mock(MultipartFile.class))
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID: [" + customerId + "], Not Found");

        // Then
        verify(customerDAO).existsCustomerById(customerId);
        verifyNoMoreInteractions(customerDAO);
        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Service);
    }

    @Test
    void cannotUploadProfileImageWhenExceptionIsThrown() throws IOException {
        // Given
        int customerId = 10;

        when(customerDAO.existsCustomerById(customerId)).thenReturn(true);

        var multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenThrow(IOException.class);

        String bucket = "customer-bucket";
        when(s3Buckets.getCustomer()).thenReturn(bucket);

        // When
        assertThatThrownBy(() -> {
            underTest.uploadCustomerProfileImage(customerId, multipartFile);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("failed to upload profile image")
                .hasRootCauseInstanceOf(IOException.class);
        // Then
        verify(customerDAO, never()).updateCustomerProfileImageId(any(), any());
    }

    @Test
    void canDownloadProfileImage() {
        // Given
        var customerId = 10;
        var profileImageId = "2222";
        var customer = new Customer(
                customerId,
                "Alex",
                "alex@gmail.com",
                "password",
                19,
                Gender.MALE,
                profileImageId
        );
        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        var bucket = "customer-bucket";
        when(s3Buckets.getCustomer()).thenReturn(bucket);

        var expectedImage = "image".getBytes();

        when(s3Service.getObject(
                bucket,
                "profile-images/%s/%s".formatted(customerId, profileImageId))
        ).thenReturn(expectedImage);

        // When
        var actualImage = underTest.getCustomerProfileImage(customerId);

        // Then
        assertThat(actualImage).isEqualTo(expectedImage);
    }

    @Test
    void cannotDownloadWhenNoProfileImageId() {
        // Given
        int customerId = 10;
        Customer customer = new Customer(
                customerId,
                "Alex",
                "alex@gmail.com",
                "password",
                19,
                Gender.MALE
        );

        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.of(customer));

        // Then
        assertThatThrownBy(() -> underTest.getCustomerProfileImage(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID: [%s], profile image Not Found".formatted(customerId));

        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Service);
    }

    @Test
    void cannotDownloadProfileImageWhenCustomerDoesNotExists() {
        // Given
        var customerId = 10;

        when(customerDAO.selectCustomerById(customerId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomerProfileImage(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID: [%s], Not Found".formatted(customerId));

        verifyNoInteractions(s3Buckets);
        verifyNoInteractions(s3Service);
    }

}