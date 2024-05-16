package com.vwits.qaplatform.controller;

import com.vwits.qaplatform.dto.RequestAnswerDto;
import com.vwits.qaplatform.dto.ResponseAnswerDto;
import com.vwits.qaplatform.entity.Answer;
import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.service.AnswerService;
import com.vwits.qaplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @PostMapping("/answers")
    public ResponseEntity<Object> postAnswer(@Valid @RequestBody RequestAnswerDto requestAnswerDto, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        requestAnswerDto.setPostedBy(currentUser.getId());
        ResponseAnswerDto postedAnswer = answerService.postAnswer(requestAnswerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedAnswer);
    }

    @PutMapping("answers/{id}")
    public ResponseEntity<Object> putAnswer(@PathVariable Long id, @Valid @RequestBody RequestAnswerDto requestAnswerDto, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        Answer ans = answerService.getAnswerById(id);
        if(ans.getAuthor().getId() != currentUser.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseAnswerDto updatedAnswer = answerService.updateAnswer(id, requestAnswerDto);
        return ResponseEntity.ok(updatedAnswer);
    }

    @GetMapping("/users/{userId}/answers")
    public ResponseEntity<List<ResponseAnswerDto>> getAnswersByUserId(@PathVariable Long userId, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        if(userId != currentUser.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<ResponseAnswerDto> answers = answerService.getAnswersByUserId(userId);
        return ResponseEntity.ok(answers);
    }

    @DeleteMapping("/answers/{id}")
    public ResponseEntity<Object> deleteAnswer(@PathVariable Long id, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName());
        Answer ans = answerService.getAnswerById(id);
        if(ans.getAuthor().getId() != currentUser.getId())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        answerService.deleteAnswer(id);
        return ResponseEntity.ok().build();
    }



}
