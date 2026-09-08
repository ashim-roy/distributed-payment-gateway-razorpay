package com.AshimCS.razorpay.merchant.dto.response;

import com.AshimCS.razorpay.common.enums.Environment;

import java.util.UUID;

public record CreateApiKeyResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
