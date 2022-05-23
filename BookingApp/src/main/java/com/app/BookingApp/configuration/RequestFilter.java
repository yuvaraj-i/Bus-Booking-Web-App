package com.app.BookingApp.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.BookingApp.services.MyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class RequestFilter extends OncePerRequestFilter {

    private JwtTokenUtils jwtUtils;
    private RefreshTokenUtils refreshUtils;
    private MyUserService userService;

    @Autowired
    public RequestFilter(JwtTokenUtils jwtUtils, MyUserService userService, RefreshTokenUtils refreshUtils) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.refreshUtils = refreshUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filters)
            throws ServletException, IOException {
        String jwtToken = null;
        String refreshToken = null;
        String mobileNumber = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization_1")) {
                    jwtToken = cookie.getValue();
                }

                if (cookie.getName().equals("Authorization_2")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken != null && jwtUtils.isTokenExpried(jwtToken)) {
            jwtToken = refreshUtils.renewAccessToken(jwtToken);
            Cookie cookie = new Cookie("Authorization_1", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            Cookie newCookie = new Cookie("Authorization_1", jwtToken);
            newCookie.setHttpOnly(false);
            newCookie.setMaxAge(60 * 60 * 24 *30);
            newCookie.setPath("/");
            response.addCookie(newCookie);

        }   

        if (jwtToken != null) {
            mobileNumber = jwtUtils.getUserMobileNumber(jwtToken);
        }

        if (mobileNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userData = userService.loadUserByUsername(mobileNumber);

            if (userData != null && jwtUtils.VerifyToken(jwtToken, userData)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userData, null, userData.getAuthorities());

                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                // redirect to Login Page;

            }

        }

        filters.doFilter(request, response);
    }

}
