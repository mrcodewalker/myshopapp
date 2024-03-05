package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("comment_content")
    private String commentContent;
    @JsonProperty("rating")
    private int rating;
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("is_active")
    private boolean active;
}
