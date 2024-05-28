package com.knowget.knowgetbackend.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class CommentDeleteDto {
    @NotNull
    private String id;
}
