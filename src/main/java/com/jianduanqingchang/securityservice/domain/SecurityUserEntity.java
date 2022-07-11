package com.jianduanqingchang.securityservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author yujie
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class SecurityUserEntity {

    public SecurityUserEntity(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
        this.removed = 0L;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * username, used for auth
     */
    @Basic(optional = false)
    @Column(name = "username")
    private String username;

    /**
     * password
     */
    @Basic(optional = false)
    @Column(name = "secret")
    private String password;

    /**
     * role
     */
    @Basic(optional = false)
    @Column(name = "user_role", length = 30)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    /**
     * marked as logical removed
     */
    @Basic(optional = false)
    @Column(name = "removed")
    private long removed;
}
