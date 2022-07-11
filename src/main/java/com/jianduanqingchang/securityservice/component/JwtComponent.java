package com.jianduanqingchang.securityservice.component;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yujie
 */
@Log4j2
@Component
public class JwtComponent {

    /**
     * token expiration time
     * the token needs to be refreshed after this time
     */
    private static final long REFRESH_SECONDS = 1800L;

    /**
     * token validation time
     * the token becomes invalid and the user needs to log in again
     * normal: 1 hours
     * remember me: 10 days
     */
    private static final long NORMAL_VALID_SECONDS = 3600L;

    private static final long REMEMBER_ME_VALID_SECONDS= 3600L * 24 * 7;

    public static final String TOKEN_HEADER_NAME = "access_token";

    public static final String VALID_TIME_FIELD = "valid_til";

    public static final String REFRESH_TIME_FIELD = "refresh_aft";

    public static final String ROLE_FIELD = "role";

    public static final String BLACKLIST_FIELD = "blacklist";

    public static final String REMEMBER_ME_FIELD = "rem_me";

    /**
     * Issuer
     */
    private static final String ISSUER = "syncnema";

    private static final String SIGNATURE = "ESm16zvbU0io0y2hfR6iM4P0mXZNPSa8Lys2HAlFN9dukmvAGj6lsyqM8i0Tz4tb9dZStAr54W0dBicVrm8ijaNwfQaSYph0ML0nAJ3nnac9tzouevrBi6tn9n2mupQs";

    @Resource
    private RedisComponent redisComponent;

    /**
     * Check token is valid, expired or in blacklist
     * @param token token
     * @return true when valid
     */
    public boolean isValid(String token) {
        try {
            JwtComponent.getUserId(token);
            if (this.isBlackList(token)){
                log.info("Token in blacklist");
                return false;
            }
            return true;
        }catch (IllegalArgumentException e){
            log.info("Illegal Token, "+ e.getMessage());
            return false;
        }
    }

    public static String getSignature() {
        return SIGNATURE;
    }

    /**
     * check if the token is in blacklist
     *
     * @param token token
     * @return true means in the black list
     */
    public boolean isBlackList(String token) {
        return redisComponent.hasKey(BLACKLIST_FIELD, token);
    }

    /**
     * add token into redis blacklist
     *
     * @param token token
     */
    public void addBlackList(String token) {
        redisComponent.hSet(BLACKLIST_FIELD, token, "true");
    }

    public static String getTokenFromHeader(HttpServletRequest request){
        return request.getHeader(TOKEN_HEADER_NAME);
    }

    /**
     * Create token
     *
     * @param userId         userId
     * @param roleWithPrefix user's role
     * @param isRememberMe   remember me or not, this will lead to a different expiration time of time
     * @return a new token
     */
    public static String createToken(String userId, String roleWithPrefix, boolean isRememberMe) {
        long currentMillis = System.currentTimeMillis();
        long validTime = isRememberMe? REMEMBER_ME_VALID_SECONDS : NORMAL_VALID_SECONDS;
        Map<String, Object> map = new HashMap<>(4);
        map.put(VALID_TIME_FIELD, new Date(currentMillis + validTime * 1000));
        map.put(ROLE_FIELD, roleWithPrefix);
        map.put(REMEMBER_ME_FIELD, isRememberMe);
        map.put(REFRESH_TIME_FIELD, new Date(currentMillis + REFRESH_SECONDS * 1000));
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, getSignature())
                .setClaims(map)
                .setIssuer(ISSUER)
                .setSubject(userId)
                .compact();
    }

    public static String refreshToken(String oldToken) {
        String userId = getUserId(oldToken);
        String userRoleWithPrefix = getUserRoleWithPrefix(oldToken);
        boolean rememberMe = getRememberMe(oldToken);
        return createToken(userId, userRoleWithPrefix, rememberMe);
    }

    /**
     * get the role in token with prefix 'role_'
     *
     * @param token token
     * @return role with prefix
     */
    public static String getUserRoleWithPrefix(String token) throws JwtException {
        return getTokenBody(token).get(ROLE_FIELD).toString();
    }

    public static boolean getRememberMe(String token) throws JwtException {
        return (boolean)getTokenBody(token).get(REMEMBER_ME_FIELD);
    }

    /**
     * Check token valid time
     *
     * @param token token
     * @return HV
     */
    public static String getTokenValidTimeByToken(String token) {
        return (String)getTokenBody(token).get(VALID_TIME_FIELD);
    }

    /**
     * check token
     *
     * @param token token
     * @return true means expired
     */
    public static boolean needRefresh(String token) {
        try {
            long timeMillis = Long.parseLong(String.valueOf(getTokenBody(token).get(REFRESH_TIME_FIELD)));
            var refreshAfter = new Date(timeMillis);
            return new Date().after(refreshAfter);
        } catch (MalformedJwtException | SignatureException | ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * get userId from token
     *
     * @param token token
     * @return UserId
     */
    public static String getUserId(String token) throws JwtException {
        return getTokenBody(token).getSubject();
    }

    /**
     * get token payload
     *
     * @param token token
     * @return payload
     */
    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(getSignature())
                .parseClaimsJws(token)
                .getBody();
    }
}
