package com.paulofranklins.customer;

import com.paulofranklins.exceptions.DuplicateResourceException;
import com.paulofranklins.exceptions.RequestValidationException;
import com.paulofranklins.exceptions.ResourceNotFoundException;
import com.paulofranklins.s3.S3Buckets;
import com.paulofranklins.s3.S3Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO,
                           CustomerDTOMapper customerDTOMapper,
                           PasswordEncoder passwordEncoder,
                           S3Service s3Service,
                           S3Buckets s3Buckets) {
        this.customerDAO = customerDAO;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
        this.s3Buckets = s3Buckets;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.selectAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Integer customerId) {
        return customerDAO.selectCustomerById(customerId)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID: [%s], Not Found".formatted(customerId)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        var email = customerRegistrationRequest.email();

        if (!customerDAO.existsCustomerByEmail(email))
            customerDAO.insertCustomer(
                    new Customer(
                            customerRegistrationRequest.name(),
                            customerRegistrationRequest.email(),
                            passwordEncoder.encode(customerRegistrationRequest.password()),
                            customerRegistrationRequest.age(),
                            customerRegistrationRequest.gender()));

        else throw new DuplicateResourceException(
                "Customer with Email: [%s], already in use".formatted(email)
        );
    }

    public void deleteCustomer(Integer id) {
        boolean isTrue = customerDAO.existsCustomerById(id);
        if (!isTrue)
            throw new ResourceNotFoundException(
                    "Customer with ID: [%s], Not Found"
                            .formatted(id));
        else customerDAO.deleteCustomer(id);
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest customerUpdateRequest) {
        var customer = customerDAO.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID: [%s], Not Found".formatted(customerId)
                ));

        boolean changes = false;

        if (customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if (!customerDAO.existsCustomerByEmail(customerUpdateRequest.email())) {
                customer.setEmail(customerUpdateRequest.email());
                changes = true;
            } else throw new DuplicateResourceException(
                    "Customer with Email: [%s], already in use".formatted(customerUpdateRequest.email())
            );
        }

        if (customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("There are no alterations to the existing data.");
        }
        customerDAO.updateCustomer(customer);
    }

    public void uploadCustomerProfileImage(Integer customerId,
                                           MultipartFile file) {
        checkIfCustomerExistsOrThrow(customerId);

        var profileImageId = UUID.randomUUID().toString();
        try {
            s3Service.putObject(
                    s3Buckets.getCustomer(),
                    "profile-images/%s/%s".formatted(customerId, profileImageId),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException("failed to upload profile image", e);
        }
        customerDAO.updateCustomerProfileImageId(profileImageId, customerId);
    }

    public byte[] getCustomerProfileImage(Integer customerId) {
        var customer = customerDAO.selectCustomerById(customerId)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID: [%s], Not Found".formatted(customerId)
                ));

        if (isBlank(customer.profileImageId())) {
            throw new ResourceNotFoundException(
                    "Customer with ID: [%s], profile image Not Found".formatted(customerId));
        }

        return s3Service.getObject(
                s3Buckets.getCustomer(),
                "profile-images/%s/%s".formatted(customerId, customer.profileImageId())
        );
    }


    private void checkIfCustomerExistsOrThrow(Integer customerId) {
        if (!customerDAO.existsCustomerById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer with ID: [%s], Not Found".formatted(customerId)
            );
        }
    }
}
