package com.app.BookingApp.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
        String authorizationHeader = request.getHeader("Authorization");
        String refreshHeader = request.getHeader("Refresh-Token");
        // request.getCookies();

        String mobileNumber = null;
        String jwtToken = null;
        String refreshToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.split(" ")[1];
            mobileNumber = jwtUtils.getUserMobileNumber(jwtToken);
        }

        if (refreshHeader != null){
            refreshToken = refreshHeader;
        }

        if (mobileNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userData = userService.loadUserByUsername(mobileNumber);

            if (refreshHeader != null & jwtUtils.isTokenExpried(jwtToken) & refreshUtils.verifyToken(refreshToken)){
                jwtToken = refreshUtils.renewAccessToken(jwtToken);
            }

            if (jwtUtils.VerifyToken(jwtToken, userData)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userData, null, userData.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else { 
                // redirect to Login Page;

            }
            
        }

        filters.doFilter(request, response);
    }

}
