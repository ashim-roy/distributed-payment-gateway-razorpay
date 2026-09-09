package com.AshimCS.razorpay.merchant.service.impl;

import com.AshimCS.razorpay.common.exception.ResourceNotFoundException;
import com.AshimCS.razorpay.common.util.RandomizerUtil;
import com.AshimCS.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.AshimCS.razorpay.merchant.dto.response.ApiKeyResponse;
import com.AshimCS.razorpay.merchant.dto.response.CreateApiKeyResponse;
import com.AshimCS.razorpay.merchant.entity.ApiKey;
import com.AshimCS.razorpay.merchant.entity.Merchant;
import com.AshimCS.razorpay.merchant.repository.ApiKeyRepository;
import com.AshimCS.razorpay.merchant.repository.MerchantRepository;
import com.AshimCS.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.Nullable;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = false)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    @Transactional
    public CreateApiKeyResponse create(UUID merchantId, CreateApiKeyRequest request) {
        // check if the merchant exists
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_"+request.environment().name().toLowerCase()+ "_"+RandomizerUtil.randomBase64(24); // generate a random string for keyIdId
        // generate secret key for the API key.
        String rawSecret = RandomizerUtil.randomBase64(40); // TODO replace with cryptographic random hash.

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO: hash the secret before saving it to the database
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        // // will return the raw secret but I will store the ENCRYPTED Secret.
        return new CreateApiKeyResponse(apiKey.getId(), keyId, rawSecret, request.environment());
        // apiKey.getId(), from DB rest local variable keyId, rawSecret, request.environment()
        // passing the local keyId variable is just a clean, direct shortcut because that exact string is already sitting in memory right above it. Both approaches yield the exact same value.
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) { // // get list of all API key for a merchant
        return apiKeyRepository.findByMerchant_Id(merchantId).stream()
                .map(apiKey ->
                        new ApiKeyResponse(
                                apiKey.getId(),
                                apiKey.getKeyId(),
                                apiKey.getEnvironment(),
                                apiKey.isEnabled(),
                                apiKey.getLastUsedAt(), null))
                .toList();

        //return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional // If made the entity dirty so transaction is used to make the persistent context open for a long time = method life cycle, Otherwise - changes are not sync with db
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey key = apiKeyRepository.findById(keyId) // <-- findById() searches by primary key (UUID)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        key.setEnabled(false);  // soft delete (enabled = false), the record stays safely in the database
        apiKeyRepository.save(key);

    }

    @Override
    @Transactional
    public @Nullable CreateApiKeyResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));
        // generat a nw screet and send to merchant.
        String newRawSecret = RandomizerUtil.randomBase64(40);
        // maing curr secrt as preeevSecret and new secret as current secret.
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(newRawSecret);      // TODO encode this with BCrypt or Argon2 or PBKDF2 or Scrypt or SHA256 or SHA512 or SHA3-256 or SHA3-512
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        apiKey = apiKeyRepository.save(apiKey);

        return new CreateApiKeyResponse(apiKey.getId(), apiKey.getKeyId(), newRawSecret, apiKey.getEnvironment());
    }


//
//    @Override
//    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
//        return null;
//    }
}
