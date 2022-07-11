package com.jianduanqingchang.securityservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author yujie
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class JwtUser extends User {
    
    private long userId;
    private String jwtToken;
    private UserRoleEnum role;
    private boolean rememberMe;
    private static final long serialVersionUID = 1L;
    
    public JwtUser(String username, String password, long userId, String jwtToken,
                   UserRoleEnum role, boolean rememberMe, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.userId = userId;
        this.jwtToken = jwtToken;
        this.role = role;
        this.rememberMe = rememberMe;
    }
}
