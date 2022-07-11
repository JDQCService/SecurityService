package com.jianduanqingchang.securityservice.repository;

import com.jianduanqingchang.securityservice.domain.SecurityUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author yujie
 */
public interface SecurityUserRepository extends JpaRepository<SecurityUserEntity, Long> {

    /**
     * Find user entity by username
     *
     * @param username username
     * @param removed 0 stands for not removed
     * @return user entity or none
     */
    Optional<SecurityUserEntity> findByUsernameAndRemoved(String username, long removed);

    /**
     * Check the user has existed or not
     *
     * @param username  username
     * @param removed   0 stands for not removed
     * @return          true means existed
     */
    boolean existsByUsernameAndRemoved(String username, long removed);
}
