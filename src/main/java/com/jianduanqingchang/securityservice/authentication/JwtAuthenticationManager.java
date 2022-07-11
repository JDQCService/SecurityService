package com.jianduanqingchang.securityservice.authentication;

import com.jianduanqingchang.securityservice.service.JwtUserSecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JwtAuthenticationManager implements AuthenticationManager {

    private final JwtUserSecurityService userSecurityService;

    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationManager(JwtUserSecurityService jwtUserSecurityService, PasswordEncoder passwordEncoder){
        this.userSecurityService = jwtUserSecurityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)){
            throw new BadCredentialsException("Bad Credential, invalid authentication");
        }
        var auth = (UsernamePasswordRememberAuthenticationToken) authentication;
        var username = auth.getPrincipal().toString();
        var userDetail = this.userSecurityService.loadUserByUsernameAndRemMe(username, auth.isRememberMe());
        if (null == auth.getCredentials()){
            throw new BadCredentialsException("Bad Credential, invalid authentication");
        }
        if (!passwordEncoder.matches(auth.getCredentials().toString(), userDetail.getPassword())) {
            throw new BadCredentialsException("Bad Credential, invalid password");
        }
        auth.setDetails(userDetail);
        return auth;
    }
}
