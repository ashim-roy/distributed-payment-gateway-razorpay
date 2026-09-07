package com.AshimCS.razorpay.operations.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "settlement_payment")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettlementPayment {
    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId") // to map a PK from the parent entity to any field in the child entity, we use @MapsId. It tells JPA that the primary key of this entity is also a foreign key to the parent entity.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;
}

/*
in code i can now do SettlementPayment.settlement
i cant do it for payment as payment in payment domain
 */
