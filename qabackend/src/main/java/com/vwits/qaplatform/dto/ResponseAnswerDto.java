package com.vwits.qaplatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vwits.qaplatform.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAnswerDto {
    private Long id;

    private String description;

    private LocalDateTime postedAt;

    private LocalDateTime updatedAt;

    private Boolean isAnonymous;

    private Long questionId;

    private String authorName;

}
