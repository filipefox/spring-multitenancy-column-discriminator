package com.example.demo.core.contexts;

import com.example.demo.core.authentications.TenantAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {

    public int getTenantId() {
        return ((TenantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getTenantId();
    }
}