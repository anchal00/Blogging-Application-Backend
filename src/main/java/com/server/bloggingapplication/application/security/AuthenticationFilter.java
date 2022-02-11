package com.server.bloggingapplication.application.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bloggingapplication.application.user.LoginUserRequestDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/blogapp/users/login");
    }

    private LoginUserRequestDTO userData;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            ServletInputStream inputStream = request.getInputStream();
            // extract user data from request
            this.userData = new ObjectMapper().readValue(inputStream, LoginUserRequestDTO.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    this.userData.getUsername(), this.userData.getPassword(), Collections.emptyList());

            return authenticationManager.authenticate(token);
            
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        String jwtToken = JWT.create().withSubject(this.userData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 72_00_000L)) //2 hours
                .sign(Algorithm.HMAC256("TEST_SECRET_KEY"));

        String body = ((org.springframework.security.core.userdetails.User)authResult.getPrincipal()).getUsername()+" " + jwtToken;
        
        response.setContentType("text");
        response.getWriter().write(body);
        response.getWriter().flush();
    }
}
