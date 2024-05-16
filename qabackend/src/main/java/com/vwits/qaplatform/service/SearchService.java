package com.vwits.qaplatform.service;

import com.vwits.qaplatform.dto.ResponseQuestionDto;
import com.vwits.qaplatform.entity.Question;
import com.vwits.qaplatform.entity.QuestionTag;
import com.vwits.qaplatform.entity.Tag;
import com.vwits.qaplatform.repository.QuestionRepository;
import com.vwits.qaplatform.repository.QuestionTagRepository;
import com.vwits.qaplatform.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private QuestionTagRepository questionTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;
    public List<ResponseQuestionDto> searchQuestions(String query){

        // Find tags with partial name match
        List<Tag> tags = tagRepository.findByNameContainingIgnoreCase(query);

        // Find question IDs based on partial tag IDs match
        List<Long> tagQuestionIds = questionTagRepository.findByTagIdIn(tags.stream().map(Tag::getId).collect(Collectors.toList()))
                .stream().map(QuestionTag::getQuestionId).toList();

        // Find question IDs based on partial title or description match
        List<Long> textQuestionIds = questionRepository.findByTitleOrDescription(query, query)
                .stream().map(Question::getId).toList();

        // Combine both sets of question IDs
        Set<Long> allQuestionIds = new HashSet<>();
        allQuestionIds.addAll(tagQuestionIds);
        allQuestionIds.addAll(textQuestionIds);

        // Find questions based on the combined question IDs
         return questionRepository.findAllById(allQuestionIds).stream()
                 .map(question -> questionService.mapQuestionToResponseDto(question))
                 .collect(Collectors.toList());


    }
}
