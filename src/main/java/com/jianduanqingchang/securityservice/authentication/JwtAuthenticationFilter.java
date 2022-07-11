package com.jianduanqingchang.securityservice.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jianduanqingchang.securityservice.domain.LoginRequestBody;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * JWT login filter，Overrided{@link UsernamePasswordAuthenticationFilter}，
 * use username and password to login, response token
 *
 * @author yujie
 */
@Log4j2
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * attempt to auth
     *
     * @param request  HTTP request
     * @param response HTTP response
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        //Only POST method is allowed in this url
        var methodType = "POST";
        if (!methodType.equals(request.getMethod())) {
            throw new AuthenticationServiceException("Only POST method is allowed");
        }
        UsernamePasswordAuthenticationToken authRequest;
        try (InputStream is = request.getInputStream()) {
            var mapper = new ObjectMapper();
            var loginUserBody = mapper.readValue(is, LoginRequestBody.class);
            //Login by username and password
            if(StringUtils.hasText(loginUserBody.getUsername())) {
                authRequest = new UsernamePasswordRememberAuthenticationToken(
                        loginUserBody.getUsername(),
                        loginUserBody.getPassword(),
                        loginUserBody.isRememberMe());
            }else {
                throw new AuthenticationServiceException("Invalid username");
            }
            this.setDetails(request, authRequest);
        }catch (NullPointerException e){
            throw new AuthenticationServiceException("Invalid Input");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthenticationServiceException("Unknown Error");
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
