package com.xxs.jxcadmin.config.security;

import com.xxs.jxcadmin.config.ClassPathTldsLoader;
import com.xxs.jxcadmin.filters.CaptchaCodeFilter;
import com.xxs.jxcadmin.pojo.User;
import com.xxs.jxcadmin.service.IRbacService;
import com.xxs.jxcadmin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 13421
 */
@SpringBootConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JxcAuthenticationFailedHandler jxcAuthenticationFailedHandler;
    @Autowired
    private JxcAuthenticationSuccessHandler jxcAuthenticationSuccessHandler;
    @Resource
    private IUserService userService;
    @Autowired
    private JxcLogoutSuccessHandler jxcLogoutSuccessHandler;
    @Resource
    private CaptchaCodeFilter captchaCodeFilter;
    @Resource
    private DataSource dataSource;
    @Resource
    private IRbacService rbacService;
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/error/**","/images/**","/js/**","/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .headers().frameOptions().disable()
                .and()
                    .formLogin()
                    .usernameParameter("userName")
                    .passwordParameter("password")
                    .loginPage("/index")
                    .loginProcessingUrl("/login")
                    .successHandler(jxcAuthenticationSuccessHandler)
                    .failureHandler(jxcAuthenticationFailedHandler)
                .and()
                    .logout()
                    .logoutUrl("/signout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler(jxcLogoutSuccessHandler)
                .and()
                    .rememberMe()
                    .rememberMeParameter("rememberMe")
                    .rememberMeCookieName("remember-me-cookie")
                    .tokenValiditySeconds(7*24*60*60)
                    .tokenRepository(persistentTokenReponsitory())
                .and()
                    .authorizeRequests().antMatchers("/index","/login","/image").permitAll()
                    .anyRequest().authenticated();
    }

    private PersistentTokenRepository persistentTokenReponsitory() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            User userDetails = userService.findUserByUsername(username);

            List<String> roleNames = rbacService.findRolesByUserName(username);
            List<String> authorities = rbacService.findAuthoritiesByRoleName(roleNames);
            roleNames = roleNames.stream().map(role-> "ROLE_"+role).collect(Collectors.toList());
            authorities.addAll(roleNames);
            System.out.println(authorities);
            userDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",authorities)));
            return userDetails;
        };
    }
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(encoder());
    }

    @Bean
    @ConditionalOnMissingBean(ClassPathTldsLoader.class)
    public ClassPathTldsLoader classPathTldsLoader(){
        return new ClassPathTldsLoader();
    }
}
