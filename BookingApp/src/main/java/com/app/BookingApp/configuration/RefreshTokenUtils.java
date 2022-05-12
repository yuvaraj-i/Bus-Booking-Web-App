package com.app.BookingApp.configuration;

import java.sql.Date;
import java.util.HashMap;

import com.app.BookingApp.models.MyClaims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class RefreshTokenUtils {

    private JwtTokenUtils jwtUtils;

    @Autowired
    public RefreshTokenUtils(JwtTokenUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    private String SECRET_KEY = "ggygdupwhdpowndjbskhcvhgwvcjhbwjhckvcqh";
    private String storedToken = "";

    public String generateRefreshToken(MyClaims payload) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("mobile_number", payload.getMobileNumber());
        String storedToken = createToken(claims);
        return storedToken;
    }

    private String createToken(HashMap<String, Object> claims) {
        return Jwts.builder().addClaims(claims).setHeaderParam("typ", "refresh_token")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 356))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public boolean verifyToken(String refreshToken){
        if (storedToken.equals(refreshToken)){
            return true;
        }

        return false;
    }

    public String renewAccessToken(String refreshToken) {
        MyClaims payload = jwtUtils.getClamisFromExpriedToken(refreshToken);

        return jwtUtils.generateToken(payload);
    }

}
