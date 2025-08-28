package com.example.demo.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.domain.LoginResponse;
import com.example.demo.domain.Role;
import com.example.demo.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") 
public class AuthController {

	@Autowired
	private UserService userService; // implements UserDetailsService

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

		String token = tokenProvider.createToken(
				auth.getName(),
				auth.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()));

		return ResponseEntity.ok(new LoginResponse(token));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
		// 简单演示
		userService.register(req.getUsername(), passwordEncoder.encode(req.getPassword()), req.getRoles());
		return ResponseEntity.ok("Registered");
	}
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
class LoginRequest {
    private String username;
    private String password;
    private Set<Role> roles;
}
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
class RegisterRequest {
    private String username;
    private String password;
    private Set<Role> roles;
}

