package com.vwits.qaplatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestQuestionDto {

    @NotBlank(message = "question title is missing")
    @Pattern(regexp = "\\D+", message = "title must be a string")
    private String title;

    @NotBlank(message = "question description is missing")
    @Pattern(regexp = "\\D+", message = "description must be a string")
    private String description;

    private Boolean isAnonymous=false;

    @NotNull(message = "user id not found")
    @Range(min = 1, max = 999999999999999999L, message = "postedBy must be a valid integer")
    private Long postedBy;

    private List<String> tags;


}