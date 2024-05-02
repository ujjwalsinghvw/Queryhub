package com.vwits.qaplatform.service;

import com.vwits.qaplatform.entity.Tag;
import com.vwits.qaplatform.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

}
