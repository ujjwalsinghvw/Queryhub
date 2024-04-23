//package com.vwits.qaplatform.controller;
//
//
//import com.vwits.qaplatform.entity.User;
//import com.vwits.qaplatform.service.UserService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/users/{id}")
//    public ResponseEntity<User> getUser(@PathVariable Long id) {
//        User user = userService.getUserById(id);
//        return ResponseEntity.ok(user);
//    }
//}
