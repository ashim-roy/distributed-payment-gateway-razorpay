package com.AshimCS.razorpay.payment.entity;

import com.AshimCS.razorpay.common.entity.Money;
import com.AshimCS.razorpay.common.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Entity
@Table(name = "refund")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status = RefundStatus.PENDING; // If you not  define status field in builder then it will take the default value = PENDING

    @Column(length = 100)
    private String bankReference;

    @Column(length = 100)
    private String errorCode;

    @Column(length = 500)
    private String errorDescription;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes; //merchant can attach a note in JSON format

    private LocalDateTime processedAt; //craeted and updatedat added by baseclass
}

