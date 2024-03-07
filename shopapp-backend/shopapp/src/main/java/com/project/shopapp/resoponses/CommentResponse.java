package com.project.shopapp.resoponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.dtos.CommentDTO;
import com.project.shopapp.models.Comment;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("comment_content")
    private String commentContent;
    @JsonProperty("rating")
    private int rating;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("created_at")
    private Date createdAt;
    public static CommentResponse fromComment(Comment comment){
        return CommentResponse.builder()
                .commentContent(comment.getCommentContent())
                .productId(comment.getProduct().getId())
                .fullName(comment.getUser().getFullName())
                .userId(comment.getUser().getId())
                .rating(comment.getRating())
                .orderId(comment.getOrder().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
