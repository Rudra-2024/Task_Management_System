package com.rudra.taskmanagementsystem.Controller;


import com.rudra.taskmanagementsystem.Models.User;
import com.rudra.taskmanagementsystem.Security.JwtTokenProvider;
import com.rudra.taskmanagementsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");
        if (userService.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }
        User newUser = userService.registerUser(username, password);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> user) {
        try {
            String username = user.get("username");
            String password = user.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtTokenProvider.createToken(authentication);
            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }
}
