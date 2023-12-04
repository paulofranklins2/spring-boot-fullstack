package com.paulofranklins.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
    void deleteCustomer(Integer id);
    void updateCustomer(Customer update);
}
