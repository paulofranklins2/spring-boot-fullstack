package com.paulofranklins.customer;

import com.paulofranklins.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerServices;
    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerServices, JWTUtil jwtUtil) {
        this.customerServices = customerServices;
        this.jwtUtil = jwtUtil;
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
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest c) {
        customerServices.addCustomer(c);
        var token = jwtUtil.issueToken(c.email(), "ROLE_USER");
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
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
