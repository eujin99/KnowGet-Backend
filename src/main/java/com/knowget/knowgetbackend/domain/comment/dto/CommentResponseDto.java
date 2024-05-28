package com.knowget.knowgetbackend.domain.comment.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentResponseDto {

    @NotNull
    private Long commentIdx;
    @NotNull
    private String content;
    @NotNull
    private LocalDateTime createdDate;
    @NotNull
    private LocalDateTime modifiedTime;

}
