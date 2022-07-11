package com.jianduanqingchang.securityservice.controller;

import com.jianduanqingchang.securityservice.domain.LoginRequestBody;
import com.jianduanqingchang.securityservice.response.MyResponseEntity;
import com.jianduanqingchang.securityservice.service.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityExistsException;
import java.util.HashMap;

/**
 *
 * @author yujie
 */
@RestController
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @PostMapping("register")
    public MyResponseEntity<Object> register(@RequestBody LoginRequestBody user) {

        try {
            var data = new HashMap<>(1);
            long userId = registerService.addUser(user.getUsername(), user.getPassword());
            data.put("user_id", userId);
            return MyResponseEntity.sendOk(data);
        }catch (EntityExistsException e){
            return MyResponseEntity.sendBadRequest("User exists, please login");
        }
    }
}
