package com.upgrad.patterns.Authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicAuthProviderTests {

    @Test
    void acceptsConfiguredCredentials() {
        assertTrue(new BasicAuthProvider("India", "India").Authenticate());
    }

    @Test
    void rejectsMissingOrInvalidCredentials() {
        assertFalse(new BasicAuthProvider(null, null).Authenticate());
        assertFalse(new BasicAuthProvider("invalid", "credentials").Authenticate());
    }
}
