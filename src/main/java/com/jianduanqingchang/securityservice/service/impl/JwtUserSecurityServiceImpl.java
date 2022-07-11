package com.jianduanqingchang.securityservice.service.impl;

import com.jianduanqingchang.securityservice.domain.JwtUser;
import com.jianduanqingchang.securityservice.domain.SecurityUserEntity;
import com.jianduanqingchang.securityservice.domain.UserRoleEnum;
import com.jianduanqingchang.securityservice.exception.SecurityEntityNotExistException;
import com.jianduanqingchang.securityservice.repository.SecurityUserRepository;
import com.jianduanqingchang.securityservice.service.JwtUserSecurityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yujie
 */
@Component
public class JwtUserSecurityServiceImpl implements JwtUserSecurityService {

    @Resource
    SecurityUserRepository securityUserRepository;

    @Override
    public UserDetails loadUserByUsernameAndRemMe(String username, boolean rememberMe) throws SecurityEntityNotExistException {

        var userEntity = securityUserRepository.findByUsernameAndRemoved(username,0L)
                .orElseThrow(() -> new SecurityEntityNotExistException(SecurityUserEntity.class, username));
        if (null == userEntity) {
            throw new UsernameNotFoundException("User name not found");
        }
        var authorities = getAuthorities(userEntity.getRole());
        return new JwtUser(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getId(),
                "",
                userEntity.getRole(),
                rememberMe,
                authorities);
    }

    @Override
    public List<GrantedAuthority> getAuthorities(UserRoleEnum userRoleEnum) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRoleEnum.VISITOR.getRole()));
        authorities.add(new SimpleGrantedAuthority(userRoleEnum.getRole()));
        if (userRoleEnum == UserRoleEnum.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(UserRoleEnum.ADMIN.getRole()));
        }
        return authorities;
    }
}
