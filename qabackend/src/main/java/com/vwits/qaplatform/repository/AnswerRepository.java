package com.vwits.qaplatform.repository;

import com.vwits.qaplatform.entity.Answer;
import com.vwits.qaplatform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByAuthorId(Long authorId);
}
