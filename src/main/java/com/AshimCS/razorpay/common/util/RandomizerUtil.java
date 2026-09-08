package com.AshimCS.razorpay.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomizerUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom(); // create ALL CAPS

    /**
     * Generates a cryptographically secure, URL-safe Base64-encoded random string.
     *
     * @param length the number of random bytes to generate before encoding
     * @return a URL-safe, padding-free Base64 string representing the random bytes
     */

    public static String randomBase64(int length) {

        byte[] buf = new byte[length];
        SECURE_RANDOM.nextBytes(buf);
//        [4, 12, 100, -12] {-128, 127}
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf); // B3W9_qXm2-vL7pRtK8y1Yw
    }
}

/*
 * Utility class for generating cryptographically secure, URL-safe random strings.
 * <p>
 * This class uses a single, thread-safe, globally shared {@link SecureRandom}
 * instance to optimize resource performance and prevent heavy OS entropy-gathering
 * overhead on every call. Encoded outputs use URL-safe Base64 without padding.
 * </p>
 *
 * • Base64.getUrlEncoder(): Takes the raw random bytes and converts them into a text-based ASCII string format using Base64 encoding.
 * It specifically uses the URL-friendly variant, which replaces characters like + and / with - and _ so they don't break URLs, webhooks, or query parameters.
 * • .withoutPadding(): Strips off trailing = padding characters, keeping the generated token clean and compact.
 * .encodeToString(buf): Finalizes the encoding and returns the resulting secure, random, text-based token (such as an API key or secret).
 * It generate a 32 character. Each char can be one out of 64 values

A-z, A-Z, 0-9, 2 more characters, so 64*64*64………..32 times
26,26,10,2
64*64*64………..32 times
 *
 */