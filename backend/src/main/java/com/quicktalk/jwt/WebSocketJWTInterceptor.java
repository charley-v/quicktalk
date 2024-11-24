package com.quicktalk.jwt;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class WebSocketJWTInterceptor implements HandshakeInterceptor {

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
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

    	String query = request.getURI().getQuery();
        if (query == null || !query.contains("token=")) {
            throw new SecurityException("Missing token parameter");
        }

        // Extract token value
        String token = query.split("token=")[1];
        
        try {
        	
        	// Parse the JWT header to extract the kid
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }
            String headerJson = new String(Base64.getUrlDecoder().decode(tokenParts[0]));
            
            // Extract the kid from the JWT header
            JSONObject header = new JSONObject(headerJson);
            String kid = header.getString("kid");

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
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            attributes.put("userId", claims.getSubject());
            return true;
        } catch (Exception e) {
            throw new SecurityException("Invalid JWT", e);
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
    }
}
