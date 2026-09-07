package com.AshimCS.razorpay.operations.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Entity
@Table(name = "dlq_event")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DlqEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @OneToOne(fetch = FetchType.LAZY)
    private WebhookEvent webhookEvent;

    @Column(length = 1000)
    private String finalError;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> payload; // Store the payload of the event that went to DLQ for auditing and replay purposes.

    private LocalDateTime movedAt; // for the first time when event moved to DLQ. If event come again then we will update the movedAt. So we can know how many times event moved to DLQ

    private LocalDateTime replayedAt; // when last time marchent replayed the DLQ from dahboard. After replay event will go from dlq and process. If all go well. DLQ event will not porcess again. If evet come again then we neeed to process the evet again
}

/*
Since message brokers can sometimes purge or lose track of messages after max retries,
storing a record in PostgreSQL when an event goes to the DLQ helps you maintain an audit trail for merchants.

Sometimes event go to DLQ. It will have evennt id. If not proceesed when marcnet is ot alive or didn’t responnse. Evet will ot go waste zn d go to DLQ.

Replayed at - when last time marchent replayed the DLQ from dahboard. After replay event will go from dlq and process. If all go well. DLQ event will not porcess again. If evet come again then we neeed to process the evet again

Moved_AT = moved when evennt pushed to DLQ. One event can be pushed againn and again to DLQ.

Thi sis a PG DB. Not a DLQ. For DLQ we have separate thing. This is fo rlog.

 */