package com.AshimCS.razorpay.merchant.service.impl;

import com.AshimCS.razorpay.common.enums.MerchantStatus;
import com.AshimCS.razorpay.common.enums.UserRole;
import com.AshimCS.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.AshimCS.razorpay.merchant.dto.response.MerchantResponse;
import com.AshimCS.razorpay.merchant.entity.AppUser;
import com.AshimCS.razorpay.merchant.entity.Merchant;
import com.AshimCS.razorpay.merchant.repository.AppUserRepository;
import com.AshimCS.razorpay.merchant.repository.MerchantRepository;
import com.AshimCS.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;

    @Override
    public MerchantResponse signup(MerchantSignupRequest request) {

        // Check if the user already exists
        if(merchantRepository.existsByEmail(request.email())){
            throw new RuntimeException("Merchant with Email already exists:" + request.email());
        }

        // business logic to create a new merchant and save it to the database
        // will use builder pattern to create a new merchant object and save it to the database
        Merchant merchant = Merchant.builder()
                .businessName(request.businessName())
                .businessType(request.businessType())
                .name(request.name())
                .email(request.email())
                .status(MerchantStatus.PENDING_KYC)
                .build();
        // Merchant wont have password, app user will have it so we need to create an App user
        merchant =  merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password()) //TODO: hash the password before saving it to the database
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);

        return new MerchantResponse(
                merchant.getId(), merchant.getName(),
                merchant.getEmail(), merchant.getBusinessName(),
                merchant.getBusinessType(), merchant.getStatus()
        );

    }

}
