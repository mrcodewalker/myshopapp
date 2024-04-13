package com.project.shopapp.services;

import com.project.shopapp.dtos.CouponDTO;
import com.project.shopapp.dtos.EmailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Email;
import com.project.shopapp.resoponses.EmailResponse;

import java.util.List;

public interface IEmailService {
    Email createUser(EmailDTO emailDTO);
    Email getUserById(long id) throws DataNotFoundException;
    List<Email> getAllUsers();
    void deleteCoupon(long id);
    Email getUserByEmail(String email);
}
