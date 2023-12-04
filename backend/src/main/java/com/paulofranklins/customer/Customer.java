package com.paulofranklins.customer;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                )
        }
)
public class Customer {
    @Id
//    @SequenceGenerator(name = "customer_id_sequence", sequenceName = "customer_id_sequence")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_sequence")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    private Integer Id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Integer age;

    public Customer(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
