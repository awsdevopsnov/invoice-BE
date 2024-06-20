package com.example.AdminInvoice.Login;

import com.example.AdminInvoice.JwtToken.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TokenValidator implements TokenvalidateInteface{
    @Autowired private JwtUtil jwtUtil;

    @Override
    public boolean isValidToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            return jwtUtil.validateToken(jwtToken);
        }
        return false;
    }
}
