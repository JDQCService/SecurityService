package com.jianduanqingchang.securityservice.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Add remember me on the basis of {@link UsernamePasswordAuthenticationToken}
 * @author yujie
 */
@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
public class UsernamePasswordRememberAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private boolean rememberMe;
    
    public UsernamePasswordRememberAuthenticationToken(Object principal, Object credentials, boolean rememberMe) {
        super(principal, credentials);
        this.rememberMe = rememberMe;
    }
    
    public UsernamePasswordRememberAuthenticationToken(Object principal, Object credentials, boolean rememberMe, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.rememberMe = rememberMe;
    }
}
