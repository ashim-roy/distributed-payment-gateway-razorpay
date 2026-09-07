package com.AshimCS.razorpay.merchant.service;

import com.AshimCS.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.AshimCS.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signup( MerchantSignupRequest request);
}
