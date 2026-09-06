package com.AshimCS.razorpay.payment.entity;

import com.AshimCS.razorpay.common.entity.Money;
import com.AshimCS.razorpay.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Entity
@Table(name = "order_record", indexes = {
        @Index(name = "idx_order_id_merchant_id", columnList = "id, merchant_id"),
        @Index(name = "idx_order_merchant_id", columnList = "merchant_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // no FK — cross-service boundary
    //	We use merchantId instead of @ManyToOne to maintain a clean service boundary,
    //	so the Order domain can later be extracted into its own microservice and database without changing the domain model.
    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Embedded
    private Money amount;

    @Column(length = 100)
    private String receipt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0; // 	New OrderRecord → attempts starts at 0 unless explicitly provided through the builder.


    @JdbcTypeCode((SqlTypes.JSON)) // Use @JdbcTypeCode(SqlTypes.JSON) to specify that the column should be treated as a JSON type in the database. This is necessary for Hibernate to correctly map the Map<String, Object> to a JSON column in PostgreSQL.
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes; // JSONB column to store additional information

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
