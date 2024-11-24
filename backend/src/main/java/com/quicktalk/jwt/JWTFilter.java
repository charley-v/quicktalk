package com.quicktalk.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quicktalk.utilities.JWTUtil;

import io.jsonwebtoken.Claims;

@Component
public class JWTFilter extends OncePerRequestFilter {

	private static final Logger JWT_FILTER_LOG = LoggerFactory.getLogger(JWTFilter.class);
	
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        JWT_FILTER_LOG.info("JWTFilter :: in doFilterInternal() :: authHeader {}",authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.validateToken(token);

                JWT_FILTER_LOG.debug("JWTFilter :: in doFilterInternal() :: claims {}",claims);
                
                // Create an authentication token
                PreAuthenticatedAuthenticationToken authentication =
                        new PreAuthenticatedAuthenticationToken(claims.getSubject(), null, null);

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
            	JWT_FILTER_LOG.info("JWTFilter :: exit doFilterInternal() :: Exception :: Return Response 401 Invalid JWT Token");
            	JWT_FILTER_LOG.info("JWTFilter :: exit doFilterInternal() :: Exception :: {}",e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        }

        JWT_FILTER_LOG.info("JWTFilter :: exit doFilterInternal() :: Forward JWT validated scuccessfuly");
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}