package com.upgrad.patterns.Authentication;

import com.upgrad.patterns.Middleware.AuthenticationProcessor;
import com.upgrad.patterns.Middleware.BasicAuthProcessor;
import com.upgrad.patterns.Middleware.JwtAuthProcessor;

import javax.servlet.http.HttpServletRequest;

public class Authenticator {

    /**
     * Builds the authentication chain with JWT authentication as the first handler.
     */
    public static AuthenticationProcessor GetAuthProcessor() {
        AuthenticationProcessor basicAuthProcessor = new BasicAuthProcessor(null);
        AuthenticationProcessor jwtAuthProcessor = new JwtAuthProcessor(basicAuthProcessor);
        return jwtAuthProcessor;
    }

    public static AuthenticationProvider GetAuthProvider(HttpServletRequest request)
    {
        if(request.getHeader("Authorization") != null)
            return new JwtAuthProvider(request.getHeader("Authorization"));
        return new BasicAuthProvider(request.getHeader("Username"), request.getHeader("Password"));
    }
}
