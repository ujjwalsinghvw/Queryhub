package com.vwits.qaplatform.repository;

import com.vwits.qaplatform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAuthorId(Long authorId);

    @Query("SELECT q FROM Question q WHERE q.title LIKE CONCAT('%', :title, '%') OR q.description LIKE CONCAT('%', :description, '%')")
   List<Question> findByTitleOrDescription(String title , String description);
}
