package com.AshimCS.razorpay.vault.entity;

import com.AshimCS.razorpay.common.enums.CardBrand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "vault_card")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaultCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 4)
    private String lastFour; // no need ot worry of getting it exposed as it is not sensitive data

    @Column(nullable = false, length = 6)
    private String bin; // first 6 digits, most case 6.   also tell which  brand the card is by first 2 or 4 digits.  so we can use it to identify the brand of the card.  but we will store the brand in a separate column as well.

    @Column(nullable = false)
    private byte[] encryptedPan;  // PCS scope:  encrypted PAN is sensitive data, so we will store it in encrypted form.  we will use AES encryption to encrypt the PAN.  we will use a DEK (Data Encryption Key) to encrypt the PAN.  the DEK will be encrypted using a KEK (Key Encryption Key) and stored in the database.  the KEK will be stored in a secure location (like AWS KMS or HashiCorp Vault).  when we need to decrypt the PAN, we will first decrypt the DEK using the KEK and then use the DEK to decrypt the PAN.

    @Column(nullable = false)
    private byte[] encryptedDek;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardBrand brand; // VISA, RUPAY

    @Column(nullable = false)
    private String expiryMonth;

    @Column(nullable = false)
    private String expiryYear;

    @Column(nullable = false)
    private String cardHolderName;

    private LocalDateTime deletedAt;
}


/* DEK explaination:
DEK is a Data Encryption Key.  It is used to encrypt the PAN (Primary Account Number) of the card.
The DEK is encrypted using a KEK (Key Encryption Key) and stored in the database.
The KEK is stored in a secure location (like AWS KMS or HashiCorp Vault).
When we need to decrypt the PAN, we will first decrypt the DEK using the KEK and then use the DEK to decrypt the PAN.
dek=asdfasdfk
PAN=123456

encrypted_pan = encrypt(PAN, dek)
encrypted_dek=encrypt(dek, masterKey)

dek = decrypt(encrypted_dek, masterKey)
pan = decrypt(encrypted_pan, dek);
 */