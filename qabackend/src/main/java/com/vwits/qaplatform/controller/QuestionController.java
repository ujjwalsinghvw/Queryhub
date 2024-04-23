package com.vwits.qaplatform.controller;

import com.vwits.qaplatform.dto.RequestQuestionDto;
import com.vwits.qaplatform.dto.ResponseQuestionDto;
import com.vwits.qaplatform.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/questions")
    public ResponseEntity<List<ResponseQuestionDto>> getAllQuestions() {
        List<ResponseQuestionDto> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/questions")
    public ResponseEntity<ResponseQuestionDto> createQuestion(@Valid @RequestBody RequestQuestionDto questionDto) {
        ResponseQuestionDto createdQuestion = questionService.createQuestion(questionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<ResponseQuestionDto> getQuestionById(@PathVariable Long id) {
        ResponseQuestionDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("users/{userId}/questions")
    public ResponseEntity<List<ResponseQuestionDto>> getQuestionsByUserId(@PathVariable Long userId) {
        List<ResponseQuestionDto> questions = questionService.getQuestionsByUserId(userId);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("questions/{id}")
    public ResponseEntity<ResponseQuestionDto> updateQuestion(@PathVariable Long id, @Valid @RequestBody RequestQuestionDto questionDto) {
        ResponseQuestionDto updatedQuestion = questionService.updateQuestion(id, questionDto);
        return ResponseEntity.ok(updatedQuestion);
    }
}