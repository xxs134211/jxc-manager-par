package com.xxs.jxcadmin.config.security;

import com.xxs.jxcadmin.pojo.User;
import com.xxs.jxcadmin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

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
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/error/**","/images/**","/js/**","/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
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
                    .authorizeRequests().antMatchers("/index","/login").permitAll()
                    .anyRequest().authenticated();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User userDetails = userService.findUserByUsername(username);
                return userDetails;
            }
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
}
