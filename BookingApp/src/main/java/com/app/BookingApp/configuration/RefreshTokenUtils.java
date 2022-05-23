package com.app.BookingApp.configuration;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import com.app.BookingApp.models.MyClaims;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Roles;
import com.app.BookingApp.repository.RolesRepository;
import com.app.BookingApp.services.MyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class RefreshTokenUtils {

    private JwtTokenUtils jwtUtils;
    private MyUserService userService;
    private RolesRepository rolesRepository;

    @Autowired
    public RefreshTokenUtils(JwtTokenUtils jwtUtils, MyUserService userService, RolesRepository rolesRepository) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.rolesRepository = rolesRepository;
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
        Claims claims = jwtUtils.getClamisFromExpriedToken(jwtToken);
        String mobileNumber = (String) claims.get("mobile_number");
        Optional<MyUser> optionalUser = userService.getUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return null;
        }

        Iterable<Roles> roles = rolesRepository.findByUserId(optionalUser.get().getId());
        Iterator<Roles> rolesIterator = roles.iterator();
        ArrayList<String> rolesList = new ArrayList<>();

        while (rolesIterator.hasNext()) {
            Roles userRole = rolesIterator.next();
            rolesList.add(userRole.getRole());
        }

        MyClaims newPayload = new MyClaims(mobileNumber, rolesList);
        return jwtUtils.generateToken(newPayload);
    }

}
