package com.jianduanqingchang.securityservice;

import com.jianduanqingchang.securityservice.authentication.JwtAuthenticationFilter;
import com.jianduanqingchang.securityservice.authentication.JwtAuthenticationManager;
import com.jianduanqingchang.securityservice.authentication.handler.JwtAuthenticationFailHandler;
import com.jianduanqingchang.securityservice.authentication.handler.JwtAuthenticationSuccessHandler;
import com.jianduanqingchang.securityservice.authentication.handler.JwtLogoutHandler;
import com.jianduanqingchang.securityservice.authorization.JwtAuthorizationFilter;
import com.jianduanqingchang.securityservice.authorization.handler.JwtAccessDeniedHandler;
import com.jianduanqingchang.securityservice.authorization.handler.JwtAuthorizationEntryPoint;
import com.jianduanqingchang.securityservice.service.JwtUserSecurityService;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author yujie
 */
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfigJwt {

    /**
     * Whitelist url for all http methods
     */
    private static final String[] AUTH_WHITELIST_ALL = {

    };

    private static final String[] AUTH_WHITELIST_POST = {
            "/register",
            "/login/**"
    };

    /**
     * Whitelist url for only get method
     */
    private static final String[] AUTH_WHITELIST_GET = {

    };

    /**
     * Login successful handler
     */
    @Resource
    JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    @Resource
    JwtUserSecurityService userSecurityService;

    /**
     * Login failed handler
     */
    @Resource
    JwtAuthenticationFailHandler jwtAuthenticationFailHandler;

    /**
     * Logout handler
     */
    @Resource
    private JwtLogoutHandler logoutHandler;

    /**
     * Password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //exception handler
                .exceptionHandling()
                //how to deal with user without login(i.e. without token)
                .authenticationEntryPoint(new JwtAuthorizationEntryPoint())
                //how to deal with user having token but no permission.
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                //disable csrf and session due to we use token instead
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //whitelist for the apis that don't need login
                .authorizeRequests().antMatchers(AUTH_WHITELIST_ALL).permitAll()
                //whitelist for the apis that don't need login, only http GET is allowed
                .and().authorizeRequests().antMatchers(HttpMethod.GET, AUTH_WHITELIST_GET).permitAll()
                //whitelist for the apis that don't need login, only http POST is allowed
                .and().authorizeRequests().antMatchers(HttpMethod.POST, AUTH_WHITELIST_POST).permitAll()
                //logout
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(this.logoutHandler)
                //all requests need to be auth unless they are in the whitelist above
                .and().authorizeRequests().anyRequest().authenticated().and()
                //JWT Filter
                .addFilterBefore(this.jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(this.jwtAuthorizationFilter(), JwtAuthorizationFilter.class)
                //build
                .build();
    }

    /**
     * customized {@link JwtAuthenticationFilter} for authentication
     *
     * @return JwtAuthenticationFilter
     */
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        var filter = new JwtAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(this.jwtAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(this.jwtAuthenticationFailHandler);
        filter.setFilterProcessesUrl("/login");

        //reuse WebSecurityConfigurerAdapter's AuthenticationManager，
        // otherwise we need to build AuthenticationManager ourselves
        filter.setAuthenticationManager(new JwtAuthenticationManager(userSecurityService, passwordEncoder()));
        return filter;
    }

    /**
     * Customized authorization filter using authentication things
     *
     * @return Customized authorization filter
     */
    @Bean
    JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(new JwtAuthenticationManager(userSecurityService, passwordEncoder()));
    }
}
