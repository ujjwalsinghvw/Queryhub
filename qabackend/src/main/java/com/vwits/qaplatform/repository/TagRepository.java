package com.vwits.qaplatform.repository;

import com.vwits.qaplatform.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByIdIn(List<Long> tagIds);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameContainingIgnoreCase(String name);

}
