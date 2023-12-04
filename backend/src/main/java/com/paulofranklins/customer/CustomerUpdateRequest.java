package com.paulofranklins.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
