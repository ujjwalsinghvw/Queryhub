package com.vwits.qaplatform.controller;

import com.vwits.qaplatform.dto.AuthResponseDto;
import com.vwits.qaplatform.dto.LoginRequestDto;
import com.vwits.qaplatform.dto.RegistrationRequestDto;
import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.service.JwtService;
import com.vwits.qaplatform.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;



@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequestDto user){
        if(userService.findByEmail(user.getEmail()) != null ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        };
        User savedUser = userService.saveUser(user);
        String token = jwtService.generateToken(savedUser);
        AuthResponseDto response = new AuthResponseDto();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setToken(token);
        return  ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDto loginRequestDto){
        User user = userService.findByEmail(loginRequestDto.getEmail());
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        user.setLastRequest(new Date());
        userService.updateUser(user);

        String token = jwtService.generateToken(user);
        AuthResponseDto response = new AuthResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setToken(token);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/logoff")
    public ResponseEntity<?> logout(Authentication authentication){
        User user = userService.findByEmail(authentication.getName());

        if (user != null) {

            user.setLastRequest(new Date(user.getLastRequest().getTime() - 24 * 60 * 60 * 1000));
            userService.updateUser(user);
            return ResponseEntity.ok("User logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


}
