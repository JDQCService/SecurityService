package com.jianduanqingchang.securityservice.service;

import javax.persistence.EntityExistsException;

/**
 * @author yujie
 */
public interface RegisterService {

    long addUser(String username, String credential) throws EntityExistsException;
}
