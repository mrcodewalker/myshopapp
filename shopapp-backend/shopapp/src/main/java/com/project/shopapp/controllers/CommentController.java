package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CommentDTO;
import com.project.shopapp.models.Comment;
import com.project.shopapp.resoponses.CommentResponse;
import com.project.shopapp.resoponses.ListCommentResponse;
import com.project.shopapp.resoponses.OrderResponse;
import com.project.shopapp.services.CommentService;
import com.project.shopapp.services.ProductService;
import com.project.shopapp.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper = new ModelMapper();
    @PostMapping("/create_comment/{id}")
    public ResponseEntity<?> createComment(
            @PathVariable("id") Long id,
            @RequestBody CommentDTO commentDTO
            )throws Exception{
        try{
            commentDTO.setOrderId(id);
            return ResponseEntity.ok(CommentResponse.fromComment(
                    this.commentService.createComment(commentDTO)
            ));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/get_comments_by_id/{id}")
    public ResponseEntity<?> getCommentsByOrderId(
            @PathVariable("id") Long id
    ){
        try{
            List<Comment> comments = this.commentService.findByOrderId(id);
            List<CommentResponse> orderResponses = new ArrayList<>();
            for (Comment comment: comments){
                orderResponses.add(CommentResponse.fromComment(comment));
            }
            return ResponseEntity.ok(
                    ListCommentResponse.builder()
                            .commentResponseList(orderResponses)
                            .message("Get comments list successfully!")
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
