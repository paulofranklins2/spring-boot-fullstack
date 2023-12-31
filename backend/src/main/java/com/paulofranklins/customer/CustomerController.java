package com.paulofranklins.customer;

import com.paulofranklins.jwt.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
    public List<CustomerDTO> getCustomer() {
        return customerServices.getAllCustomers();
    }

    @GetMapping(path = "/{id}")
    public CustomerDTO getUserById(@PathVariable("id") Integer customerId) {
        return customerServices.getCustomer(customerId);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerServices.addCustomer(customerRegistrationRequest);
        var token = jwtUtil.issueToken(customerRegistrationRequest.email(), "ROLE_USER");
        return ResponseEntity.ok().header(AUTHORIZATION, token).build();
    }

    @DeleteMapping(path = "/{id}")
    public void deleteCustomer(@PathVariable("id") Integer customerId) {
        customerServices.deleteCustomer(customerId);
    }

    @PutMapping(path = "/{id}")
    public void patchCustomer(@PathVariable("id") Integer customerId,
                              @RequestBody CustomerUpdateRequest request) {
        customerServices.updateCustomer(customerId, request);
    }

    @PostMapping(
            value = "{customerId}/profile-image",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCustomerProfileImage(
            @PathVariable("customerId") Integer customerId,
            @RequestParam("file") MultipartFile multipartFile) {
        customerServices.uploadCustomerProfileImage(customerId, multipartFile);
    }

    @GetMapping("{customerId}/profile-image")
    public byte[] getCustomerProfileImage(
            @PathVariable("customerId") Integer customerId) {
        return customerServices.getCustomerProfileImage(customerId);
    }
}
