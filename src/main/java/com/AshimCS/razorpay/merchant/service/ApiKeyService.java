package com.AshimCS.razorpay.merchant.service;

import com.AshimCS.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.AshimCS.razorpay.merchant.dto.response.ApiKeyResponse;
import com.AshimCS.razorpay.merchant.dto.response.CreateApiKeyResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {

    CreateApiKeyResponse create(UUID merchantId, CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    @Nullable CreateApiKeyResponse rotate(UUID merchantId, UUID keyId);
//
//    void revoke(UUID merchantId, UUID keyId);
//
//    @Nullable ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
