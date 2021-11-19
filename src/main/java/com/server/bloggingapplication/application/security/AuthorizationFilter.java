package com.server.bloggingapplication.application.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");

        UsernamePasswordAuthenticationToken token = null;

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {

            final String suppliedToken = authHeader.replace("Bearer ", "");
            String userData = JWT.require(Algorithm.HMAC256("TEST_SECRET_KEY".getBytes())).build().verify(suppliedToken)
                    .getSubject();
            if (userData != null) {
                token = new UsernamePasswordAuthenticationToken(userData, null,Collections.emptyList());
            }
        }
        //token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

}
