package com.example.AdminInvoice.JwtToken;

import com.example.AdminInvoice.Controller.LoginMessage;
import com.example.AdminInvoice.Entity.Login;
import com.example.AdminInvoice.Repository.LoginDao;
import com.example.AdminInvoice.Repository.RegisterDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 2592000000L; // 30 days in milliseconds
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 2592000000L; // 30 days in milliseconds

    @Autowired private LoginDao loginDao;
    @Autowired private RegisterDao registerDao;

    private static final Logger LOGGER = Logger.getLogger(JwtUtil.class.getName());
    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        LOGGER.log(Level.INFO, "Generating access token for user: {0}", username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefresh(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);
        LOGGER.log(Level.INFO, "Generating refresh token for user: {0}", username);
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            LOGGER.log(Level.INFO, "Parsing claims from token");
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Invalid token", e);
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                LOGGER.log(Level.WARNING, "Token claims are null or invalid");
                return false;
            }
            boolean isTokenValid = !claims.getExpiration().before(new Date());
            if (!isTokenValid) {
                LOGGER.log(Level.WARNING, "Token is expired");
            } else {
                LOGGER.log(Level.INFO, "Token is valid");
            }
            return isTokenValid;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Token validation failed", e);
            return false;
        }
    }

    public LoginMessage refreshAccessTokenUsingRefreshToken(String refresh) {
        if (!validateToken(refresh)) {
            LOGGER.log(Level.WARNING, "Refresh token validation failed");
            return new LoginMessage("Refresh token expired or invalid", false, "", "", "", "", HttpStatus.UNAUTHORIZED);
        }

        Claims claims = getClaimsFromToken(refresh);
        if (claims == null || !"refresh".equals(claims.get("type"))) {
            LOGGER.log(Level.WARNING, "Invalid refresh token type");
            return new LoginMessage("Invalid refresh token", false, "", "", "", "", HttpStatus.UNAUTHORIZED);
        }
        String username = claims.getSubject();
        String newAccessToken = generateAccessToken(username);

        Optional<Login> optionalLogin = loginDao.findByRefresh(refresh);
        if (optionalLogin.isPresent()) {
            Login loginToUpdate = optionalLogin.get();
            loginToUpdate.setAccessToken(newAccessToken);
            loginDao.save(loginToUpdate);
            LOGGER.log(Level.INFO, "Access token updated successfully for user: {0}", username);
            return new LoginMessage("Access token updated successfully", true, newAccessToken, refresh, loginToUpdate.getUserRole(), loginToUpdate.getUsername(), HttpStatus.OK);
        } else {
            LOGGER.log(Level.WARNING, "Refresh token not found in database");
            return new LoginMessage("Refresh token not found", false, "", refresh, "", "", HttpStatus.NOT_FOUND);
        }
    }
}
