package com.project.shopapp.services;

import com.project.shopapp.dtos.CouponDTO;
import com.project.shopapp.dtos.EmailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Email;
import com.project.shopapp.repositories.EmailRepository;
import com.project.shopapp.resoponses.EmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final EmailRepository emailRepository;
    @Override
    public Email createUser(EmailDTO emailDTO){
        if (emailRepository.findByEmail(emailDTO.getEmail()).isEmpty()){
            Email email = Email.builder()
                    .email(emailDTO.getEmail())
                    .name(emailDTO.getName())
                    .picture(emailDTO.getPicture())
                    .build();
            return emailRepository.save(email);
        }
        return null;
    }

    @Override
    public Email getUserById(long id) throws DataNotFoundException {
        return this.emailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find email with id: "+id));
    }

    @Override
    public List<Email> getAllUsers() {
        return this.emailRepository.findAll();
    }

    @Override
    public void deleteCoupon(long id) {

    }

    @Override
    public Email getUserByEmail(String email) {
        return this.emailRepository.findUserByEmail(email);
    }
}
