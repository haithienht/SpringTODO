package com.npht.springtodo.service;

import java.util.ArrayList;
import java.util.List;

import com.npht.springtodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.npht.springtodo.model.User u = userRepo.findByEmail(email);
        if (u == null) {
            System.out.println("Email not found: " + email);
            throw new UsernameNotFoundException("Email: " + email + " was not found in the database");
        }
        System.out.println("Found User: " + u);
        List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
        if (u.getIsAdmin()) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        
        UserDetails userDetails = (UserDetails) new User(u.getEmail(), u.getPassword(), authorityList);
        return userDetails;
    }

}