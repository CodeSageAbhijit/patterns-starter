package com.upgrad.patterns.Middleware;

import com.upgrad.patterns.Authentication.AuthenticationProvider;
import com.upgrad.patterns.Authentication.JwtAuthProvider;

public class JwtAuthProcessor extends AuthenticationProcessor {

    public JwtAuthProcessor(AuthenticationProcessor processor)
    {
        super(processor);
    }

    // Check the JWT token or pass to the next processor.
    @Override
    public boolean isAuthorized(AuthenticationProvider provider) {

        if(provider instanceof JwtAuthProvider)
        {
            return provider.Authenticate();
        }
        else if(nextProcessor != null)
        {
            return nextProcessor.isAuthorized(provider);
        }
        return false;
    }
}
