package com.project.shopapp.services;

import com.project.shopapp.dtos.*;
import com.project.shopapp.dtos.Province;
import com.project.shopapp.models.User;
import com.project.shopapp.resoponses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber,String password, Long roleId) throws Exception;
    User getUserById(long id) throws Exception;
    User getUserDetailsFromToken(String token) throws Exception;
    List<UserResponse> getAllUsers();
    User updateUser(Long id, UpdateUserDTO userDTO) throws Exception;
    void deleteUser(Long id);
    User changesPasswordUser(Long id, ChangesPasswordDTO changesPasswordDTO) throws Exception;
    List<Province> getAllProvinces() throws Exception;
    List<ProvinceDTO> getAllProvincesDTO() throws Exception;
    List<DistrictDTO> getAllDistrictsDTO(Long provinceId) throws Exception;
    List<CommuneDTO> getAllCommunesDTO(Long districtId) throws Exception;
    Page<User> findByKeyword(String keyword, Pageable pageable);
    User updateUserByAdmin(Long id, String roleName, boolean isActive) throws Exception;
}
