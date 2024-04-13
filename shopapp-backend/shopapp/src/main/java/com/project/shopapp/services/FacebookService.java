package com.project.shopapp.services;

import com.project.shopapp.dtos.EmailDTO;
import com.project.shopapp.dtos.FacebookDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Email;
import com.project.shopapp.models.Facebook;
import com.project.shopapp.repositories.EmailRepository;
import com.project.shopapp.repositories.FacebookRepository;
import com.project.shopapp.resoponses.EmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacebookService implements IFacebookService {
    private final FacebookRepository facebookRepository;

    @Override
    public Facebook createUser(FacebookDTO facebookDTO) {
        if (facebookRepository.findByEmail(facebookDTO.getEmail()).isEmpty()){
            Facebook facebook = Facebook.builder()
                    .email(facebookDTO.getEmail())
                    .name(facebookDTO.getName())
                    .facebookId(facebookDTO.getFacebookId())
                    .build();
            return facebookRepository.save(facebook);
        }
        return null;
    }

    @Override
    public Facebook getUserById(long id) throws DataNotFoundException {
        return this.facebookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find email with id: "+id));
    }

    @Override
    public List<Facebook> getAllUsers() {
        return this.facebookRepository.findAll();
    }

    @Override
    public void deleteAccount(long id) {
        this.facebookRepository.deleteById(id);
    }

    @Override
    public Facebook getFacebookByEmail(String email) {
        return this.facebookRepository.findUserByEmail(email);
    }
}
