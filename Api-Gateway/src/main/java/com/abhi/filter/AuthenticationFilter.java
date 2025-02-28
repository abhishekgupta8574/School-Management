package com.abhi.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.abhi.jwt.JwtService;

import io.jsonwebtoken.Claims;



@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;

	@Autowired
	private JwtService jwtUtil;

	public AuthenticationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			if (validator.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("Missing authorization header");
				}

				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				System.out.println(authHeader);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
				try {
                    Claims claims = jwtUtil.extractClaims(authHeader);
                    String role = claims.get("role", String.class); 
                    String path = exchange.getRequest().getURI().getPath(); 

                    
                    if ((path.startsWith("/teacher") && !(role.equalsIgnoreCase("teacher") || role.equalsIgnoreCase("admin"))) ||
                            (path.startsWith("/student") && !(role.equalsIgnoreCase("student") || role.equalsIgnoreCase("admin")))) {
                            throw new RuntimeException("Unauthorized: You do not have permission to access this service");
                        }
                    
                } catch (Exception e) {
                    System.out.println("Unauthorized access: " + e.getMessage());
                    throw new RuntimeException("Unauthorized access " + e.getMessage() );
                }
            }
			return chain.filter(exchange);
		});
	}

	public static class Config {
	}
}
