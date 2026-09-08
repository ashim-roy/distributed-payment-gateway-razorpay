package com.AshimCS.razorpay.common.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    private final String resourceName;
    private final Object identifier;

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(resourceName +" not found: "+ identifier);
        this.resourceName = resourceName;
        this.identifier = identifier;
    }
}

/*

Using Object identifier instead of a specific type (like String or UUID) is a classic design choice for custom exceptions.
It gives you maximum flexibility and reusability across your entire application.
Here is why Object is used and how it handles your UUID:

1. Different Entities Use Different ID Types
In your project:

Merchant might use a UUID for its ID.
Another entity (like an Order or Payment) might use a Long or an Integer.
A User entity might use a String (like an email or username).

If your exception constructor only accepted a String, you would be forced to manually convert every non-string ID using .toString() every time you threw the exception:

// Annoying if it only accepted String:
.orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId.toString()));

By accepting an Object, Java lets you pass any type directly (UUID, Long, String, etc.) without extra conversion code.

 */