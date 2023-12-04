package com.paulofranklins.customer;

import com.paulofranklins.exceptions.DuplicateResourceException;
import com.paulofranklins.exceptions.RequestValidationException;
import com.paulofranklins.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomer() {
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDAO.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID: [%s], Not Found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();

        if (!customerDAO.existsCustomerByEmail(email))
            customerDAO.insertCustomer(
                    new Customer(
                            customerRegistrationRequest.name(),
                            customerRegistrationRequest.email(),
                            customerRegistrationRequest.age()));

        else throw new DuplicateResourceException(
                "Customer with Email: [%s], already in use"
                        .formatted(email));
    }

    public void deleteCustomer(Integer id) {
        boolean isTrue = customerDAO.existsCustomerById(id);
        if (!isTrue)
            throw new ResourceNotFoundException(
                    "Customer with ID: [%s], Not Found"
                            .formatted(id));
        else customerDAO.deleteCustomer(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest r) {
        Customer customer = getCustomer(id);
        boolean changes = false;

        if (r.name() != null && !r.name().equals(customer.getName())) {
            customer.setName(r.name());
            changes = true;
        }

        if (r.email() != null && !r.email().equals(customer.getEmail())) {
            if (!customerDAO.existsCustomerByEmail(r.email())) {
                customer.setEmail(r.email());
                changes = true;
            } else throw new DuplicateResourceException(
                    "Customer with Email: [%s], already in use"
                            .formatted(r.email()));
        }

        if (r.age() != null && !r.age().equals(customer.getAge())) {
            customer.setAge(r.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("There are no alterations to the existing data.");
        }

        customerDAO.updateCustomer(customer);
    }
}
