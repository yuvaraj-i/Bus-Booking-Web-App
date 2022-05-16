package com.app.BookingApp.configuration;

import java.sql.Date;
import java.util.HashMap;
import java.util.Optional;

import com.app.BookingApp.models.MyClaims;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.services.MyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class RefreshTokenUtils {

    private JwtTokenUtils jwtUtils;
    private MyUserService userService;

    @Autowired
    public RefreshTokenUtils(JwtTokenUtils jwtUtils, MyUserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Value("${Refresh_key}")
    private String SECRET_KEY;
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

    public boolean verifyToken(String refreshToken) {
        if (storedToken.equals(refreshToken)) {
            return true;
        }

        return false;
    }

    public String renewAccessToken(String jwtToken) {
        String mobileNumber = jwtUtils.getClamisFromExpriedToken(jwtToken);
        Optional<MyUser> optionalUser = userService.getUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return jwtToken;
        }

        MyClaims newPayload = new MyClaims(mobileNumber);
        return jwtUtils.generateToken(newPayload);
    }

}
