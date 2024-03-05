package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCommentResponse {
    @JsonProperty("comments")
    private List<CommentResponse> commentResponseList;
    @JsonProperty("message")
    private String message;
}
