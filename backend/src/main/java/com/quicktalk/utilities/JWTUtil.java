package com.quicktalk.utilities;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;

@Component
public class JWTUtil {

    private static final Logger JWT_UTIL_LOG = LoggerFactory.getLogger(JWTUtil.class); 

    @Value("${cognito.jwt.keys.kid1}")
    private String kid1;
    
    @Value("${cognito.jwt.keys.kid2}")
    private String kid2;
    
    @Value("${cognito.jwt.keys.kid1.n}")
    private String kid1Modulus;

    @Value("${cognito.jwt.keys.kid1.e}")
    private String kid1Exponent;

    @Value("${cognito.jwt.keys.kid2.n}")
    private String kid2Modulus;

    @Value("${cognito.jwt.keys.kid2.e}")
    private String kid2Exponent;

    private PublicKey getPublicKey(String kid) throws Exception {
        JWT_UTIL_LOG.info("JWTUtil :: in getPublicKey() :: kid {}", kid);

        String modulus = null;
        String exponent = null;

        if (kid1.equals(kid)) {
            modulus = kid1Modulus;
            exponent = kid1Exponent;
        } else if (kid2.equals(kid)) {
            modulus = kid2Modulus;
            exponent = kid2Exponent;
        } else {
            throw new IllegalArgumentException("Invalid key ID (kid): " + kid);
        }

        if (modulus == null || exponent == null) {
            throw new IllegalArgumentException("Missing modulus or exponent for kid: " + kid);
        }

        BigInteger bigIntModulus = new BigInteger(1, Base64.getUrlDecoder().decode(modulus));
        BigInteger bigIntExponent = new BigInteger(1, Base64.getUrlDecoder().decode(exponent));

        // Generate the RSAPublicKey
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntExponent);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    public Claims validateToken(String token) throws Exception {
        // Parse the JWT header to extract the kid
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        String headerJson = new String(Base64.getUrlDecoder().decode(tokenParts[0]));
        JWT_UTIL_LOG.info("JWTUtil :: in validateToken() :: headerJson {}", headerJson);

        JSONObject header = new JSONObject(headerJson);
        String kid = header.getString("kid");
       
        JWT_UTIL_LOG.debug("JWTUtil :: in validateToken() :: kid {}", kid);

        // Get the correct public key using the kid
        PublicKey publicKey = getPublicKey(kid);

        // Validate the JWT using the selected public key
        return Jwts.parserBuilder()
                   .setSigningKey(publicKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}
