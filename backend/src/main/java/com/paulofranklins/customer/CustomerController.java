package com.paulofranklins.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerServices;

    public CustomerController(CustomerService customerServices) {
        this.customerServices = customerServices;
    }

    @GetMapping
    public List<Customer> getCustomer() {
        return customerServices.getAllCustomer();
    }

    @GetMapping(path = "/{id}")
    public Customer getUserById(@PathVariable("id") Integer id) {
        return customerServices.getCustomer(id);
    }

    @PostMapping
    public void registerCustomer(
            @RequestBody CustomerRegistrationRequest c) {
        customerServices.addCustomer(c);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        customerServices.deleteCustomer(id);
    }

    @PutMapping(path = "/{id}")
    public void patchCustomer(@PathVariable("id") Integer id,
                              @RequestBody CustomerUpdateRequest r) {
        customerServices.updateCustomer(id, r);
    }
}
