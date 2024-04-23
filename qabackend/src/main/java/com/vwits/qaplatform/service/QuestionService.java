package com.vwits.qaplatform.service;

import com.vwits.qaplatform.dto.RequestQuestionDto;
import com.vwits.qaplatform.dto.ResponseAnswerDto;
import com.vwits.qaplatform.dto.ResponseQuestionDto;
import com.vwits.qaplatform.dto.UserDto;
import com.vwits.qaplatform.entity.Question;
import com.vwits.qaplatform.entity.QuestionTag;
import com.vwits.qaplatform.entity.Tag;
import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.exception.EntityNotFoundException;
import com.vwits.qaplatform.repository.QuestionRepository;
import com.vwits.qaplatform.repository.QuestionTagRepository;
import com.vwits.qaplatform.repository.TagRepository;
import com.vwits.qaplatform.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionTagRepository questionTagRepository;

    public List<ResponseQuestionDto> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::mapQuestionToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseQuestionDto createQuestion(RequestQuestionDto questionDto) {
        Question question = new Question();
        mapToQuestion(question,questionDto);
        question.setPostedAt(LocalDateTime.now());
        Question savedQuestion = questionRepository.save(question);

        //create entries in QuestionTags table for the tags
        if(questionDto.getTags() != null){
            Set<String> tagNames = new HashSet<>(questionDto.getTags());
            for(String tagName: tagNames){
            Tag tag = tagRepository.findByName(tagName).orElseGet(()-> tagRepository.save(new Tag(tagName)));
                QuestionTag questionTag = new QuestionTag(savedQuestion.getId(), tag.getId());
                questionTagRepository.save(questionTag);
            }
        }
        return mapQuestionToResponseDto(savedQuestion);
    }

    public ResponseQuestionDto getQuestionById(Long id) {
        Optional<Question> questionOptional = questionRepository.findById(id);
        return questionOptional.map(this::mapQuestionToResponseDto).orElseThrow(()-> new EntityNotFoundException("Question id doesn't exist"));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        //delete entries from QuestionTags table;
        questionTagRepository.deleteByQuestionId(id);

        //delete the question
        questionRepository.deleteById(id);
    }

    public List<ResponseQuestionDto> getQuestionsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User doesn't exist"));
        return questionRepository.findByAuthorId(userId).stream()
                .map(this::mapQuestionToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseQuestionDto updateQuestion(Long id, RequestQuestionDto questionDto) {
        Question question = questionRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Question id doesn't exist"));
            mapToQuestion(question,questionDto);
            question.setUpdatedAt(LocalDateTime.now());
        Question savedQuestion = questionRepository.save(question);

        //Clear existing tags for this question
        questionTagRepository.deleteByQuestionId(id);

        //Add/update tags
        if(questionDto.getTags() != null){
            Set<String> tagNames = new HashSet<>(questionDto.getTags());
            for(String tagName: tagNames){
                Tag tag = tagRepository.findByName(tagName).orElseGet(()-> tagRepository.save(new Tag(tagName)));
                QuestionTag questionTag = new QuestionTag(savedQuestion.getId(), tag.getId());
                questionTagRepository.save(questionTag);
            }
        }
        return mapQuestionToResponseDto(savedQuestion);

    }

    private ResponseQuestionDto mapQuestionToResponseDto(Question question) {
        ResponseQuestionDto questionDto = new ResponseQuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        List<ResponseAnswerDto> answers = question.getAnswers().stream()
                        .map(ans -> answerService.convertToDto(ans))
                                .collect(Collectors.toList());
        questionDto.setAnswers(answers);
        questionDto.setPostedBy(new UserDto(question.getAuthor().getId(), question.getAuthor().getName()));
        List<Long> tagIds = questionTagRepository.findTagIdsByQuestionId(question.getId());
        List<Tag> tags = tagRepository.findByIdIn(tagIds);
        questionDto.setTags(tags);
        return questionDto;
    }

    private void mapToQuestion(Question question, RequestQuestionDto questionDto){
        BeanUtils.copyProperties(questionDto, question);
        User user = userRepository.findById(questionDto.getPostedBy()).orElseThrow(()->new EntityNotFoundException("User doesn't exist"));
        question.setAuthor(user);

    }
}
