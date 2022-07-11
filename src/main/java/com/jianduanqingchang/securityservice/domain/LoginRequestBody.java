package com.jianduanqingchang.securityservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * HTML body used for deserialization in login API
 *
 * @author yujie
 */
@Data
@Component
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginRequestBody {
    
    private boolean rememberMe = false;

    private String username;

    private String password;
}
