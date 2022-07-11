package com.jianduanqingchang.securityservice.controller;

import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yujie
 */
@Log4j2
@RestController
public class SecurityController {

    @PreAuthorize("hasAnyRole('User','Admin')")
    @PostMapping("security/validation")
    public MyResponseEntity<Object> getUsername() {
        return MyResponseEntity.sendOk(null);
    }
}
