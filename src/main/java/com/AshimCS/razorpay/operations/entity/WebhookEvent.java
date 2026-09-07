package com.AshimCS.razorpay.operations.entity;

import com.AshimCS.razorpay.common.enums.WebhookEventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "webhook_event")
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class WebhookEvent {
    // When we send a webhok event to marchent we store it. When fired safely we can remove form it.
    // We store to retry. also for logging purposes

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false, length = 100)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Column(nullable = false)
    private String targetUrl;

    @Column(nullable = false)
    private String signature;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WebhookEventStatus status;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    private LocalDateTime nextRetryAt;

    private LocalDateTime lastAttemptAt;

    private Integer lastResponseCode;

    @Column(length = 1000)
    private String lastResponseBody; // whole payload and some more info. Why store? Retry me we don’t need to calculate again. Marchent can look at this for logging auditing

    private LocalDateTime deliveredAt;

}

