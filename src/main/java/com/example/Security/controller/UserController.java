package com.example.Security.controller;
import com.example.Security.dto.AuthenticationResponse;
import com.example.Security.dto.LoginCredentials;
import com.example.Security.dto.UserDto;
import com.example.Security.service.JwtUtil;
import com.example.Security.service.MyUserDetailService;
import com.example.Security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

        @Autowired
        UserService userService;
        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        MyUserDetailService myUserDetailService;
        @Autowired
        JwtUtil jwtUtill;

        @PostMapping("/token")
        public ResponseEntity<AuthenticationResponse> generateToken(@RequestBody LoginCredentials loginCredentials){
                       this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                loginCredentials.getUsername(),loginCredentials.getPassword()));

                UserDetails userDetails=myUserDetailService.loadUserByUsername(loginCredentials.getUsername());
                String jwtToken=jwtUtill.generateToken(userDetails);

                return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
        }

        @GetMapping("/user")
        public ResponseEntity<List<UserDto>> getAllUser(){
            return ResponseEntity.ok(userService.getAllUser());
        }

        @PreAuthorize("hasRole('ROLE_USER' or 'ROLE_ADMIN')")
        @PostMapping("/test")
        public ResponseEntity<String> tester(){
                return ResponseEntity.ok("Test");
        }

}
