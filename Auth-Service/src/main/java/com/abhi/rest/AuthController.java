package com.abhi.rest;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abhi.binding.AuthRequest;
import com.abhi.binding.AuthResponse;
import com.abhi.dto.UserDto;
import com.abhi.entity.UserCredential;
import com.abhi.mapper.UserMapper;
import com.abhi.repo.UserCredentialRepository;
import com.abhi.service.AuthService;
import com.netflix.discovery.converters.Auto;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService service;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserCredentialRepository repo;

	@PostMapping("/register")
	public ResponseEntity<?> addNewUser(@RequestBody UserDto dto) {
		
		UserCredential user = UserMapper.getEntity(dto);
		
		if(repo.findByName(user.getName()).isPresent()) {
			throw new RuntimeException(dto.getName() +": this user name already exist ");
		}
		 String saveUser = service.saveUser(user);
		 return new ResponseEntity<>(saveUser, org.springframework.http.HttpStatus.CREATED);
	}

//	@PostMapping("/token")
//	public AuthResponse getToken(@RequestBody AuthRequest authRequest) {
//
//		AuthResponse response = new AuthResponse();
//
//		try {
//			Authentication authenticate = authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//			String token = null;
//			
//			if (authenticate.isAuthenticated()) {
//				System.out.println("isAuthenticated");
//				token = service.generateToken(authRequest.getUsername());
//				response.setToken(token);
//				response.setLoginValid("yes");
//			}
//		} catch (Exception e) {
//			response.setToken("");
//			response.setLoginValid("no");
//		}
//
//		return response;
//	}
	
	@PostMapping("/token")
	public AuthResponse getToken(@RequestBody AuthRequest authRequest) {
	    AuthResponse response = new AuthResponse();

	    try {
	        System.out.println("authenticate user: " + authRequest.getUsername());
	        Authentication authenticate = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

	        if (authenticate.isAuthenticated()) {
	            System.out.println("User authenticated: " + authRequest.getUsername());
	            String token = service.generateToken(authRequest.getUsername());
	            response.setToken(token);
	            response.setLoginValid("yes");
	        }
	    } catch (Exception e) {
	        System.out.println("Authentication failed: " + e.getMessage());
	        response.setToken("");
	        response.setLoginValid("no");
	    }

	    return response;
	}

	@GetMapping("/validate")
	public String validateToken(@RequestParam("token") String token) {
		service.validateToken(token);
		return "Token is valid";
	}
}
