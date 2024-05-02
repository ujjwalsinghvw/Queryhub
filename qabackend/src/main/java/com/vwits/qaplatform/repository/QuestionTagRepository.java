package com.vwits.qaplatform.repository;

import com.vwits.qaplatform.entity.QuestionTag;
import com.vwits.qaplatform.entity.QuestionTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, QuestionTagId> {
    @Query("SELECT qt.tagId from QuestionTag qt WHERE qt.questionId = ?1")
    List<Long> findTagIdsByQuestionId(Long questionId);

    @Transactional
    void deleteByQuestionIdAndTagId(Long questionId, Long tagId);
    void deleteByQuestionId(Long questionId);
}
