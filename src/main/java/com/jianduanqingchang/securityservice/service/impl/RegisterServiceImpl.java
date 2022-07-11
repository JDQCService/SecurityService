package com.jianduanqingchang.securityservice.service.impl;

import com.jianduanqingchang.securityservice.domain.SecurityUserEntity;
import com.jianduanqingchang.securityservice.domain.UserRoleEnum;
import com.jianduanqingchang.securityservice.repository.SecurityUserRepository;
import com.jianduanqingchang.securityservice.service.RegisterService;
import com.jianduanqingchang.securityservice.utils.PasswordUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityExistsException;

/**
 * @author yujie
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private SecurityUserRepository securityUserRepository;

    @Override
    public long addUser(String username, String credential) throws EntityExistsException {

        if (!isUniqueUsername(username)){
            throw new EntityExistsException();
        }
        var userEntity = new SecurityUserEntity(username, PasswordUtil.getBCryptPassword(credential), UserRoleEnum.USER);
        return securityUserRepository.save(userEntity).getId();
    }

    private boolean isUniqueUsername(String username){
        return !securityUserRepository.existsByUsernameAndRemoved(username, 0L);
    }
}
