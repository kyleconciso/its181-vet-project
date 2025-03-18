package com.group2.server.controller;

import com.group2.server.model.ApiResponse;
import com.group2.server.model.User;
import com.group2.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Username and password are required", null));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            org.springframework.security.core.userdetails.User principal =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            // Get the actual user entity
            User userDetails = userService.findByUsername(principal.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User details not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", userDetails.getUsername());
            response.put("userId", userDetails.getId());
            response.put("role", userDetails.getRole());
            response.put("email", userDetails.getEmail());

            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid username or password", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Login failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody User user) {
        try {
            // Validation
            if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Username, password, and email are required", null));
            }

            // Check for duplicates
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Username is already taken", null));
            }

            if (userService.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Email is already in use", null));
            }

            // By default, register as regular user
            if (user.getRole() == null) {
                user.setRole("USER");
            }

            // Create the new user
            User newUser = userService.createUser(user);

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", newUser.getId());
            userData.put("username", newUser.getUsername());
            userData.put("email", newUser.getEmail());
            userData.put("role", newUser.getRole());

            return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", userData));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Registration failed: " + e.getMessage(), null));
        }
    }
}