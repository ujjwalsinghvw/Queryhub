package com.vwits.qaplatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vwits.qaplatform.entity.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseQuestionDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime postedAt;
    private LocalDateTime updatedAt;
    private Boolean isAnonymous;
    private UserDto postedBy;

    private List<ResponseAnswerDto> answers;

    private List<Tag> tags;





}