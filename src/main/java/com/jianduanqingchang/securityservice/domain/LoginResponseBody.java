package com.jianduanqingchang.securityservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Yujie Liu
 */
@Data
@AllArgsConstructor
public class LoginResponseBody {

    @JsonProperty("user_id")
    private long userId;

    private UserRoleEnum role;

    @JsonProperty("access_token")
    private String accessToken;
}
