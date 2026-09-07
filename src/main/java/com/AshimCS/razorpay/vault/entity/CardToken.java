package com.AshimCS.razorpay.vault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "card_token")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vault_card_id", nullable = false)
    private VaultCard vaultCard;

    private UUID customer;

    @Column(nullable = false)
    private UUID merchant;

    private LocalDateTime revokedAt;

}

/*
A real entity to interact with card for a merchant. User might enter it already,
when they use zara we issue token from zara an dfor HnM different token.
Token created for ZARA cant used ot HnM.

Token - using which we interact with card. Our app don’t know real card.
Card info ina. Service, everywhere else you use. A token.

 */