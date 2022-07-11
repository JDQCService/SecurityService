package com.jianduanqingchang.securityservice.service;

import com.jianduanqingchang.securityservice.domain.UserRoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author yujie
 */
public interface JwtUserSecurityService {

    /**
     * load user info from DB
     *
     * @param username                      username
     * @param rememberMe                    remember me or not
     * @return                              user detail
     * @throws UsernameNotFoundException    user not found in DB
     */
    UserDetails loadUserByUsernameAndRemMe(String username, boolean rememberMe) throws UsernameNotFoundException;

    /**
     *
     * All user will be granted public
     * Login user will be granted corresponding title
     * Only admin will be granted Admin
     *
     * @param userRoleEnum  user role
     * @return              authority
     */
    List<GrantedAuthority> getAuthorities(UserRoleEnum userRoleEnum);
}
