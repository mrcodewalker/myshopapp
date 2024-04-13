package com.project.shopapp.controllers;


import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.project.shopapp.models.Root;
import com.project.shopapp.resoponses.GoogleLoginResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/oauth2")
public class OAuth2Controller {
    @GetMapping("/login/google")
    @PermitAll
    public ResponseEntity<?> currentUserGoogle(
        OAuth2AuthenticationToken oAuth2AuthenticationToken
    ){
        Map<String, Object> map = oAuth2AuthenticationToken.getPrincipal().getAttributes();
            Root root = Root.builder()
                    .picture((String) map.get("picture"))
                    .name((String) map.get("name"))
                    .email((String) map.get("email"))
                    .build();

            ModelMapper modelMapper = new ModelMapper();
            GoogleLoginResponse googleLoginResponse = modelMapper.map(root, GoogleLoginResponse.class);


            return ResponseEntity.ok(googleLoginResponse);
    }
    @GetMapping("/login/facebook")
    public Map<String, Object> currentUserFacebook(
            OAuth2AuthenticationToken oAuth2AuthenticationToken
    ){
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }
    public ResponseEntity<?> toPerson(Map<String, Object> map){
        if (map==null){
            return ResponseEntity.ok("null");
        }
        Root root = new Root();
        root.setEmail((String) map.get("email"));
        root.setName((String) map.get("name"));
        root.setPicture((String) map.get("picture"));
        return ResponseEntity.ok(root);
    }
    @GetMapping("/get/info/google")
    @PermitAll
    public ResponseEntity<?> getInformation(
            OAuth2AuthenticationToken oAuth2AuthenticationToken
    ){
        Map<String, Object> map = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        Root root = Root.builder()
                .picture((String) map.get("picture"))
                .name((String) map.get("name"))
                .email((String) map.get("email"))
                .build();

        ModelMapper modelMapper = new ModelMapper();
        GoogleLoginResponse googleLoginResponse = modelMapper.map(root, GoogleLoginResponse.class);


        return ResponseEntity.ok(googleLoginResponse);
    }
}
