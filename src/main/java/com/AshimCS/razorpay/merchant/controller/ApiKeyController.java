package com.AshimCS.razorpay.merchant.controller;


import com.AshimCS.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.AshimCS.razorpay.merchant.dto.response.ApiKeyResponse;
import com.AshimCS.razorpay.merchant.dto.response.CreateApiKeyResponse;
import com.AshimCS.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<CreateApiKeyResponse> create (@PathVariable UUID merchantId,
                                                        @Valid @RequestBody CreateApiKeyRequest request) { // http://localhost:8080/v1/merchants/{merchantId}/api-keys
        // Check if the merchant exists
        // If not, throw ResourceNotFoundException
        // Generate a new API key
        // Save the API key to the database
        // Return the API key in the response

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantId, request));


    }

    // get all API key for a merchant
    @GetMapping
    //public ResponseEntity<List<ApiKeyResponse>> list(@PathVariable UUID merchantId) {
    public ResponseEntity<List<ApiKeyResponse>> listByMerchant(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantId));

        // return ResponseEntity.ok(apiKeyService.list(merchantId));
       // return ResponseEntity.ok(apiKeyService.listByMerchant(merchantContext.getMerchantId()));
    }

    // This would help merchant to revoke any APIKEY
//    @DeleteMapping("/keyId")
//    public ResponseEntity<Void> revoke(@PathVariable UUID keyId) {
//        apiKeyService.revoke(merchantContext.getMerchantId(), keyId);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/keyId")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        apiKeyService.revoke(merchantId, keyId);
        return ResponseEntity.noContent().build();
    }

}
