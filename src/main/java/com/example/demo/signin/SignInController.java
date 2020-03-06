package com.example.demo.signin;

import com.example.demo.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signIn")
public class SignInController {

    @Autowired
    private SignInService signinService;

    @GetMapping
    public ResponseEntity signIn(@RequestHeader String authorization) {
        String jwt = signinService.signIn(authorization);
        return ResponseEntity.ok()
                .header(Constants.AUTHORIZATION_HEADER, Constants.BEARER_SCHEMA + jwt)
                .body(null);
    }
}