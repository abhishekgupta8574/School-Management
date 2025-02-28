package com.abhi.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.abhi.entity.UserCredential;
import com.abhi.repo.UserCredentialRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	System.out.println(username + " this is the username ");
        Optional<UserCredential> credential = repository.findByName(username);
//        UserCredential userCredential = credential.get();
//        System.out.println(userCredential +"user credential");
        
        if (credential.isEmpty()) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }
        UserCredential userCredential = credential.get();
        System.out.println("User found: " + userCredential);
        return new CustomUserDetails(userCredential);
        //return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }
}