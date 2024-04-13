package com.project.shopapp.controllers;

import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Email;
import com.project.shopapp.resoponses.CheckSocialAccountResponse;
import com.project.shopapp.resoponses.EmailResponse;
import com.project.shopapp.services.EmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/emails")
public class EmailController {
    private final EmailService emailService;
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUesrs(
           @PathVariable("id") Long id
    ) throws DataNotFoundException {
        Email emailResponse = this.emailService.getUserById(id);
        return ResponseEntity.ok(emailResponse);
    }
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam("email") String email
    ) throws DataNotFoundException {
        if (this.emailService.getUserByEmail(email).getId()<=0){
            return ResponseEntity.ok(
                    CheckSocialAccountResponse.builder()
                            .message("failed")
                            .build()
            );
        }
        return ResponseEntity.ok(
                CheckSocialAccountResponse.builder()
                        .message("successfully")
                        .quantity(this.emailService.getAllUsers().size())
                        .build()
        );
    }
}
