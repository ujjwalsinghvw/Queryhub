package com.vwits.qaplatform.controller;

import com.vwits.qaplatform.dto.ResponseQuestionDto;
import com.vwits.qaplatform.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query){
        List<ResponseQuestionDto> response = searchService.searchQuestions(query);

        return ResponseEntity.ok(response);
    }

}
