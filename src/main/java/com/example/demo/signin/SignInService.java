package com.example.demo.signin;

import com.example.demo.core.services.JwtService;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Base64;

@Service
public class SignInService {

    private static final String BASIC_SCHEMA = "Basic ";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String signIn(String authorizationHeader) throws AuthenticationException, EntityNotFoundException {
        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BASIC_SCHEMA)) {
            throw new AuthenticationCredentialsNotFoundException("Basic authentication token not found or without schema");
        }

        try {
            String decodedBase64 = decodeBase64(authorizationHeader.replace(BASIC_SCHEMA, ""));
            String[] userNamePassword = decodedBase64.split(":");
            String email = userNamePassword[0];
            String password = userNamePassword[1];
            User user = userService.findByEmail(email);
            verifyPassword(password, user.getPassword());
            return jwtService.createJwt(email, user.getRole().getId(), user.getRole().getName(), user.getTenantId());
        } catch (IndexOutOfBoundsException e) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
    }

    private String decodeBase64(String encodedString) throws BadCredentialsException {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
    }

    private void verifyPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadCredentialsException("Authentication request failed");
        }
    }
}