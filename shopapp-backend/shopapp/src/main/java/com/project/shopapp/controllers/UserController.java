package com.project.shopapp.controllers;


import com.project.shopapp.dtos.*;
import com.project.shopapp.dtos.Province;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.resoponses.*;
import com.project.shopapp.resoponses.get.GetUserResponse;
import com.project.shopapp.resoponses.get.GetUsersResponse;
import com.project.shopapp.services.UserService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4300")
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Autowired
    private ResourceLoader resourceLoader;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
            ){
        try{
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        RegisterUserResponse.builder()
                                .message(
                                        localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                                .build()
                );
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(
                    RegisterUserResponse.builder()
                            .message(
                                    localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY)
                            )
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/details")
    public ResponseEntity<?> getUserDetails(
            @RequestHeader("Authorization") String authorizationHeader
    ){
        try{
            String extractedToken = authorizationHeader.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse
                    .fromUser(user));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/details/{userId}")
    public ResponseEntity<UserResponse> updateUserDetails(
            @PathVariable("userId") Long userId,
            @RequestBody UpdateUserDTO userDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        try {
            String extractedToken = authorizationHeader.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);

            if (user.getId() != userId){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            User updatedUser = userService.updateUser(userId, userDTO);
            return ResponseEntity.ok(UserResponse.fromUser(updatedUser));

        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
            ) {
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword(),
                    userLoginDTO.getRoleId() == null ? 1 : userLoginDTO.getRoleId()
            );
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                    .message(
                            localizationUtils
                                    .getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage())
                    )
                    .build());
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getUsers(
        HttpServletRequest request
    ){
        try{
            return ResponseEntity.ok(
                    GetUsersResponse.builder()
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(MessageKeys.GET_USERS)
                            )
                            .userResponseList(userService.getAllUsers())
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(
            @PathVariable("id") Long id,
            HttpServletRequest request
    ){
        try{
            User user = userService.getUserById(id);
            return ResponseEntity.ok(
                    GetUserResponse.builder()
                            .message(
                                    localizationUtils
                                            .getLocalizedMessage(MessageKeys.GET_USER)
                            )
                            .userResponse(UserResponse.fromUser(user))
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/changes-password/{id}")
    public ResponseEntity<?> changesPasswordUser(
            @PathVariable("id") Long id,
            @RequestBody ChangesPasswordDTO changesPasswordDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        try{
            String extractedToken = authorizationHeader.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            if (user.getId() != id){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            User userClone = this.userService.changesPasswordUser(id, changesPasswordDTO);
            return ResponseEntity.ok(
                    ChangesPasswordResponse.builder()
                            .fullname(userClone.getFullName())
                            .message("You have been changed your password successfully, please log in again!")
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/provinces")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Province>> getAllProvinces() throws Exception {
        try {
            return ResponseEntity.ok(this.userService.getAllProvinces());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/get-provinces")
    public ResponseEntity<List<ProvinceDTO>> getProvinces() throws Exception{
        try {
            return ResponseEntity.ok(this.userService.getAllProvincesDTO());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/get-districts/{id}")
    public ResponseEntity<List<DistrictDTO>> getDistricts(
            @PathVariable("id") Long id
    ){
        try{
            return ResponseEntity.ok(this.userService.getAllDistrictsDTO(id));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/get-communes/{id}")
    public ResponseEntity<List<CommuneDTO>> getCommunes(
            @PathVariable("id") Long id
    ){
        try{
            return ResponseEntity.ok(this.userService.getAllCommunesDTO(id));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/get-users-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending());
        Page<UserResponse> userPage = this.userService
                .findByKeyword(keyword, pageRequest)
                .map(UserResponse::fromUser);
        int totalPages = userPage.getTotalPages();
        List<UserResponse> userResponses = userPage.getContent();
        return ResponseEntity.ok(ListUsersResponse
                .builder()
                .users(userResponses)
                .totalPages(totalPages)
                .build());
    }
    @PutMapping("/update-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserByAdmin(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserByAdminDTO updateUserByAdminDTO
    ) throws Exception{
        try{
            return ResponseEntity.ok(this.userService
                    .updateUserByAdmin(id, updateUserByAdminDTO.getRoleName()
                    , updateUserByAdminDTO.isActive()));
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
