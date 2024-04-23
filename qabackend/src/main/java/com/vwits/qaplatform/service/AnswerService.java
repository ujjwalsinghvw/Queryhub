package com.vwits.qaplatform.service;

import com.vwits.qaplatform.dto.RequestAnswerDto;
import com.vwits.qaplatform.dto.ResponseAnswerDto;
import com.vwits.qaplatform.entity.Answer;
import com.vwits.qaplatform.entity.Question;
import com.vwits.qaplatform.entity.User;
import com.vwits.qaplatform.exception.EntityNotFoundException;
import com.vwits.qaplatform.repository.AnswerRepository;
import com.vwits.qaplatform.repository.QuestionRepository;
import com.vwits.qaplatform.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    @Autowired
    private  AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;


    public ResponseAnswerDto postAnswer(RequestAnswerDto requestAnswerDto) {
        User user = userRepository.findById(requestAnswerDto.getPostedBy())
                .orElseThrow(() -> new EntityNotFoundException("User id not found with id"));
        Question question = questionRepository.findById(requestAnswerDto.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("Question id not found"));

        Answer answer = new Answer();
        answer.setDescription(requestAnswerDto.getDescription());
        answer.setIsAnonymous(requestAnswerDto.getIsAnonymous());
        answer.setPostedAt(LocalDateTime.now());
        answer.setAuthor(user);
        answer.setQuestion(question);

        return convertToDto(answerRepository.save(answer));
    }

    public ResponseAnswerDto updateAnswer(Long id, RequestAnswerDto requestAnswerDto) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer id  not found"));

        answer.setDescription(requestAnswerDto.getDescription());
        answer.setIsAnonymous(requestAnswerDto.getIsAnonymous());
        answer.setUpdatedAt(LocalDateTime.now());

        return convertToDto(answerRepository.save(answer));
    }

    public List<ResponseAnswerDto> getAnswersByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User id not found"));

        List<Answer> answers = answerRepository.findByAuthorId(userId);
        return answers.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public void deleteAnswer(Long id) {
        answerRepository.deleteById(id);
    }

    public ResponseAnswerDto convertToDto(Answer answer) {
        ResponseAnswerDto responseAnswerDto = new ResponseAnswerDto();
        BeanUtils.copyProperties(answer, responseAnswerDto);
        responseAnswerDto.setQuestionId(answer.getQuestion().getId());
        responseAnswerDto.setAuthorName(answer.getAuthor().getName());
        return responseAnswerDto;
    }
}
