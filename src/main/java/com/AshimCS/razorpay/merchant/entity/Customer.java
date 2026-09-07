package com.AshimCS.razorpay.merchant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer", indexes = {
        @Index(name = "idx_customer_merchant_id", columnList = "merchant_id"),
        @Index(name = "idx_customer_email", columnList = "email")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(length = 200)
    private String name;

    @Column(length = 200)
    private String email;

    @Column(length = 20)
    private String phone;

    private LocalDateTime deletedAt;
}

/*
why ManyToOne?

Cant mix marchent data, logical seperation. Isolation. For marchent zara asked for customer info such as Phone, email etc.
HnM asked fo rcustomer: they get some info from customer. Some info HnM doenst have access to zara.
If we put M2M. When customer updates all customer gets the info. For that we duplicate our database.
We can afford to have RAM customer to 3-4 marchent. W edont want to reveal info to a customer.

If. We make M2M - allmarcnet can get same info
 */