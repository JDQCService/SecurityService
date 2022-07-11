package com.jianduanqingchang.securityservice.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author yujie
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserRoleEnum implements Serializable {

    /**
     * Visitor
     */
    VISITOR("ROLE_Visitor"),
    /**
     * User
     */
    USER("ROLE_User"),

    /**
     * Admin
     */
    ADMIN("ROLE_Admin");

    private final String role;

    private static final long serialVersionUID = 1L;

    UserRoleEnum(String role) {
        this.role = role;
    }

    /**
     * Get UserRole enum through string
     *
     * @param userRoleStr string ,like "Admin"
     * @return enum type like UserRoleEnum.ADMIN or null
     * @throws NullPointerException No such Enum type
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserRoleEnum getUserRoleByStr(@JsonProperty("role") String userRoleStr) throws NullPointerException{
        if (StringUtils.isEmpty(userRoleStr)){
            return null;
        }
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.role.substring(5).equalsIgnoreCase(userRoleStr)){
                return userRoleEnum;
            }
        }
        throw new NullPointerException("No such UserRole enum type");
    }

    @JsonValue
    public String getRoleStrByRole(){
        return this.role.substring(5);
    }

    public String getRole(){
        return this.role;
    }
}
