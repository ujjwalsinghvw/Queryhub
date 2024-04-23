package com.vwits.qaplatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAnswerDto {
    @NotBlank(message = "answer description is missing")
    @Pattern(regexp = "\\D+", message = "answer description must be a string")
    private String description;

    private Boolean isAnonymous=false;

    @NotNull(message = "question id not found")
    @Range(min = 1, max = 999999999999999999L, message = "question id must be a valid integer")
    private Long questionId;

    @NotNull(message = "user id not found")
    @Range(min = 1, max = 999999999999999999L, message = "postedBy must be a valid integer")
    private Long postedBy;
}
