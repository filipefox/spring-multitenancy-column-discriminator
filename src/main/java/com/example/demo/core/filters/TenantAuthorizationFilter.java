package com.example.demo.core.filters;

import com.example.demo.core.authentications.TenantAuthenticationToken;
import com.example.demo.core.services.JwtService;
import com.example.demo.core.utils.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class TenantAuthorizationFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);

        if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith(Constants.BEARER_SCHEMA)) {
            TenantAuthenticationToken tenantAuthenticationToken = getTenantAuthenticationToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(tenantAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private TenantAuthenticationToken getTenantAuthenticationToken(String authorizationHeader) {
        TenantAuthenticationToken tenantAuthenticationToken = null;

        try {
            /* AUTHENTICATION */
            Jws<Claims> jwsClaims = jwtService.parseJwt(authorizationHeader);
            String email = jwsClaims.getBody().getSubject();
            /* AUTHORIZATION */
            int roleId = jwsClaims.getBody().get(Constants.ROLE_ID_CLAIM, Integer.class);
            List<SimpleGrantedAuthority> authorities = getSimpleGrantedAuthorities(jwsClaims);
            int tenantId = jwsClaims.getBody().get(Constants.TENANT_ID_CLAIM, Integer.class);
            tenantAuthenticationToken = new TenantAuthenticationToken(email, roleId, authorities, tenantId);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error("Problems with JWT", e);
        }

        return tenantAuthenticationToken;
    }

    private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Jws<Claims> jwsClaims) {
        String role = jwsClaims.getBody().get(Constants.ROLE_NAME_CLAIM, String.class);
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
        return Collections.singletonList(simpleGrantedAuthority);
    }
}