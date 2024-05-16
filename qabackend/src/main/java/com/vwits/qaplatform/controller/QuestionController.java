package com.vwits.qaplatform.controller;

import com.vwits.qaplatform.dto.RequestQuestionDto;
import com.vwits.qaplatform.dto.ResponseQuestionDto;
import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.service.QuestionService;
import com.vwits.qaplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/questions")
    public ResponseEntity<List<ResponseQuestionDto>> getAllQuestions() {
        List<ResponseQuestionDto> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/questions")
    public ResponseEntity<ResponseQuestionDto> createQuestion(@Valid @RequestBody RequestQuestionDto questionDto, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        questionDto.setPostedBy(currentUser.getId());
        ResponseQuestionDto createdQuestion = questionService.createQuestion(questionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<ResponseQuestionDto> getQuestionById(@PathVariable Long id) {
        ResponseQuestionDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id, Authentication authentication ) {
        User currentUser = userService.findByEmail(authentication.getName());
        ResponseQuestionDto ques = questionService.getQuestionById(id);
        if(ques.getPostedBy().getId() != currentUser.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        questionService.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("users/{userId}/questions")
    public ResponseEntity<List<ResponseQuestionDto>> getQuestionsByUserId(@PathVariable Long userId, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        if(userId != currentUser.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<ResponseQuestionDto> questions = questionService.getQuestionsByUserId(userId);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("questions/{id}")
    public ResponseEntity<ResponseQuestionDto> updateQuestion(@PathVariable Long id, @Valid @RequestBody RequestQuestionDto questionDto, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        ResponseQuestionDto ques = questionService.getQuestionById(id);
        if(ques.getPostedBy().getId() != currentUser.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseQuestionDto updatedQuestion = questionService.updateQuestion(id, questionDto);
        return ResponseEntity.ok(updatedQuestion);
    }


}