/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaevsoftware.USB;

import com.jaevsoftware.USB.auth.handler.LoginSuccessHandler;
import com.jaevsoftware.USB.model.service.IUploadFileService;
import com.jaevsoftware.USB.model.service.JpaUserDetailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    IUploadFileService uploadService;

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    JpaUserDetailService userDetailService;

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {

        PasswordEncoder encoder = passwordEncoder;
        builder.userDetailsService(userDetailService).passwordEncoder(encoder);
//        User.UserBuilder users = User.builder().passwordEncoder(password -> encoder.encode(password));
//        builder.inMemoryAuthentication().withUser(users.username("admin").password("1234").roles("USER"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/img/**", "/font/**", "/fontawesome/**", "/UsersUploads/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(loginSuccessHandler)
                .loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error403");
    }

}
