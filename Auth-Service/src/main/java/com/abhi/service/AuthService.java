package com.abhi.service;
import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abhi.dto.UserKafkaDto;
//import com.abhi.entity.Role;
import com.abhi.entity.UserCredential;
import com.abhi.repo.UserCredentialRepository;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private KafkaTemplate<String, UserKafkaDto> kafkaTemplate;
    private static final String TOPIC = "user-creation";
    
    public String saveUser(UserCredential credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        UserCredential save = repository.save(credential);
        UserKafkaDto kafkaDto= new UserKafkaDto();
        BeanUtils.copyProperties(save, kafkaDto);
        kafkaDto.setDate(LocalDateTime.now());
        kafkaTemplate.send(TOPIC,kafkaDto);
        return "user added to the system";
    }
    
    

    public String generateToken(String username) {
    	UserCredential userCredential = repository.findByName(username).get();
    	 String role = userCredential.getRole();
    	return jwtService.generateToken(username,role);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }


}
