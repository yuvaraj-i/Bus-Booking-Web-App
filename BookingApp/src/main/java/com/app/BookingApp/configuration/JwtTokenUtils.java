package com.app.BookingApp.configuration;

import java.util.Date;
import java.util.HashMap;

import com.app.BookingApp.models.MyClaims;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenUtils {
    @Value("${Access_key}")
    private String SECRET_KEY;

    public String getUserMobileNumber(String token) {
        return (String) getPayload(token).get("mobile_number");
    }

    public Long getUserId(String token) {
        return (Long) getPayload(token).get("id");
    }

    public boolean isTokenExpried(String jwtToken) {
        if (jwtToken == null) {
            return true;
        }

        try {

            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return false;
        }

        catch (ExpiredJwtException e) {
            return true;
        }

    }

    public boolean VerifyToken(String token, UserDetails user) {
        if (!isTokenSigned(token) && isTokenExpried(token)) {
            return false;
        }

        // System.out.println(user.getId() + " " + getUserId(token));
        return user.getUsername().equals(getUserMobileNumber(token));
    }

    private Claims getPayload(String token) {
        Claims tokenValue = null;
        try {
            tokenValue = Jwts.parser()
                    .setSigningKey(SECRET_KEY).parseClaimsJws(token)
                    .getBody();
        }

        catch (ExpiredJwtException e) {
            tokenValue = e.getClaims();
        }

        return tokenValue;
    }

    public boolean isTokenSigned(String token) {
        return Jwts.parser().isSigned(token);
    }

    public String generateToken(MyClaims payload) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("mobile_number", payload.getMobileNumber());
        claims.put("roles", payload.getRoles());

        return createToken(claims);
    }

    private String createToken(HashMap<String, Object> claims) {
        return Jwts.builder().addClaims(claims).setHeaderParam("typ", "jwt_access_token")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Claims getClamisFromExpriedToken(String jwtToken) {
        Claims payload = null;

        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
        }

        catch (ExpiredJwtException e) {
            payload = e.getClaims();
        }

        return payload;

    }

}
