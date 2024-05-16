package com.vwits.qaplatform.controller;


import com.vwits.qaplatform.dto.ResponseUserDto;
import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseUserDto> getUser(@PathVariable Long id) {
        ResponseUserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }



}
