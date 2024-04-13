package com.project.shopapp.controllers;

import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Email;
import com.project.shopapp.models.Facebook;
import com.project.shopapp.resoponses.CheckSocialAccountResponse;
import com.project.shopapp.services.EmailService;
import com.project.shopapp.services.FacebookService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/facebooks")
public class FacebookController {
    private final FacebookService facebookService;
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUesrs(
           @PathVariable("id") Long id
    ) throws DataNotFoundException {
        Facebook facebook = this.facebookService.getUserById(id);
        return ResponseEntity.ok(facebook);
    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam("email") String email
    ) throws DataNotFoundException {
        if (this.facebookService.getFacebookByEmail(email).getId()==0){
            return ResponseEntity.ok(
                    CheckSocialAccountResponse.builder()
                            .message("failed")
                            .build()
            );
        }
        return ResponseEntity.ok(
                CheckSocialAccountResponse.builder()
                        .message("successfully")
                        .quantity(this.facebookService.getAllUsers().size())
                        .build()
        );
    }
}
