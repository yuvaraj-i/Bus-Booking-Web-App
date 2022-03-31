package com.app.BookingApp.configuration;

import java.util.Date;
import java.util.HashMap;

import com.app.BookingApp.models.MyClaims;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenUtils {
    // @Value("${key}")
    private String SECRET_KEY = "yxdrewxfkclkmcdnkcdjbjc";

    public String getUserMobileNumber(String token) {
        return (String) getPayload(token).get("mobile_number");
    }

    public boolean VerifyToken(String token, UserDetails user) {

        if (!isTokenSigned(token)) {
            return false;
        }
        if (getExpriyDate(token).before(new Date())) {
            return false;
        }
        // System.out.println(user.getId() + " " + getUserId(token));
        return user.getUsername().equals(getUserMobileNumber(token));
    }

    public Claims getPayload(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY).parseClaimsJws(token)
                .getBody();
    }

    private Date getExpriyDate(String token) {
        return getPayload(token).getExpiration();
    }

    private boolean isTokenSigned(String token) {
        return Jwts.parser().isSigned(token);
    }

    public String generateToken(MyClaims payload) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", payload.getId());
        claims.put("mobile_number", payload.getMobileNumber());

        return "Bearer " + createToken(claims);
    }

    private String createToken(HashMap<String, Object> claims) {
        return Jwts.builder().addClaims(claims).setHeaderParam("typ", "jwt")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

}
