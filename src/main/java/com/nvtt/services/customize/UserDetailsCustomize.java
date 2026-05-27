/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.services.customize;

import com.nvtt.pojo.User;
import com.nvtt.services.UserService;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *
 * @author vthan
 */
@Component("userDetailsService")
public class UserDetailsCustomize implements UserDetailsService {
    
    private static final Logger logger = LogManager.getLogger(UserDetailsCustomize.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("start sql loadUserByUserName");
        User user = this.userService.getUserByEmail(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Username/Password invalid");
        }
        
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("This account was blocked");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        
        logger.info("end sql;");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}
