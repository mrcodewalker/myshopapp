package com.project.shopapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.components.JwtTokenUtils;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.*;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDenyException;
import com.project.shopapp.dtos.Province;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.resoponses.UserResponse;
import com.project.shopapp.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();
    public final LocalizationUtils localizationUtils;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

//    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    @Transactional
    public User createUser(UserDTO userDTO) throws Exception {
        // register user
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));

        if (role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("Can not register an admin account ");
        }

        User user = User.builder()
                .fullName(userDTO.getFullName())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .role(new Role(userDTO.getRoleId(), Role.USER))
                .active(true)
                .build();
//        User user = modelMapper.map(userDTO,User.class);
//        Role role = roleRepository.findById(userDTO.getRoleId())
//                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (userDTO.getFacebookAccountId()==0&&userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
        }
        return userRepository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password, Long roleId) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()){
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
        }
//        return optionalUser.get(); // JWT Token
        User existingUser = optionalUser.get();
        // Check password
        if (existingUser.getFacebookAccountId() == 0
                && existingUser.getGoogleAccountId() == 0){
            if (!passwordEncoder.matches(password, existingUser.getPassword())){
                throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
            }
        }
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty()|| !roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.ROLE_DOES_NOT_EXISTS));
        }

        if (!optionalUser.get().isActive()){
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          phoneNumber, password,
                existingUser.getAuthorities()
        );
        // Authenticate with java spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User getUserById(long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(
                        () ->
                                new DataNotFoundException("Can not find user with id: "+id)
                );
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)){
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()){
            return user.get();
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (User clone : userList){
            modelMapper.typeMap(User.class, UserResponse.class);
            UserResponse userResponse = new UserResponse();
            modelMapper.map(clone,userResponse);
            userResponse.setDateOfBirth(clone.getDateOfBirth());
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @Override
    @Transactional
    public User updateUser(Long userId, UpdateUserDTO updateUserDTO) throws Exception {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String newPhoneNumber = updateUserDTO.getPhoneNumber();
        if (!existingUser.getPhoneNumber().equals(newPhoneNumber) &&
                userRepository.existsByPhoneNumber(newPhoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        if (updateUserDTO.getFullName() != null) {
            existingUser.setFullName(updateUserDTO.getFullName());
        }
        if (updateUserDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updateUserDTO.getPhoneNumber());
        }
        if (updateUserDTO.getAddress() != null) {
            existingUser.setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updateUserDTO.getDateOfBirth());
        }
        existingUser.setId(userId);

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User changesPasswordUser(Long id, ChangesPasswordDTO changesPasswordDTO) throws Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Can not find user with id: "+id
                ));
        if (changesPasswordDTO.getPassword()
                .equals(changesPasswordDTO.getOldPassword())){
            throw new Exception("Can not changes password with same value");
        }
        if (!passwordEncoder
                .matches( changesPasswordDTO.getOldPassword() ,existingUser.getPassword())){
            throw new Exception("Can not changes password because old password is not correct");
        }
        if (!changesPasswordDTO.getPassword()
                .equals(changesPasswordDTO.getRetypePassword())){
            throw new Exception("Password and retype password does not match");
        }
            existingUser.setPassword(passwordEncoder.encode(changesPasswordDTO.getPassword()));
        existingUser.setId(id);
        return this.userRepository.save(existingUser);
    }

    @Override
    public List<Province> getAllProvinces() throws Exception {
        try {
            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            return List.of(provinces);
        } catch (Exception e){
            logger.warn("Can not get all provinces !NOTICE");
            return null;
        }
    }

    @Override
    public List<ProvinceDTO> getAllProvincesDTO() throws Exception {
        try{
            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            Set<Long> containProvinceIdCheck = new HashSet<>();
            List<ProvinceDTO> list = new ArrayList<>();
            for (Province clone : provinces){
                if (!containProvinceIdCheck.contains(clone.getProvinceId())){
                    ProvinceDTO provinceDTO = new ProvinceDTO();
                    provinceDTO.setProvinceId(clone.getProvinceId());
                    provinceDTO.setProvinceName(clone.getProvinceName());
                    list.add(provinceDTO);
                    containProvinceIdCheck.add(clone.getProvinceId());
                }
            }
            return list;
        } catch (Exception e){
            logger.warn("Can not get 63 provinces !NOTICE");
            return null;
        }
    }

    @Override
    public List<DistrictDTO> getAllDistrictsDTO(
            Long provinceId
    ) throws Exception {
        try{
            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            Set<Long> districtCheck = new HashSet<>();
            List<DistrictDTO> list = new ArrayList<>();
            for (Province clone : provinces){
                if (clone.getProvinceId().equals(provinceId)&&!districtCheck.contains(clone.getDistrictId())){
                    DistrictDTO districtDTO = new DistrictDTO();
                    districtDTO.setDistrictId(clone.getDistrictId());
                    districtDTO.setDistrictName(clone.getDistrictName());
                    list.add(districtDTO);
                    districtCheck.add(clone.getDistrictId());
                }
            }
            return list;
        } catch (Exception e){
            logger.warn("Can not get districts !NOTICE");
            return null;
        }
    }

    @Override
    public List<CommuneDTO> getAllCommunesDTO(
            Long districtId
    ) throws Exception {
        try{
            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            Set<Long> communeCheck = new HashSet<>();
            List<CommuneDTO> list = new ArrayList<>();
            for (Province clone : provinces){
                if (!clone.getDistrictId().equals(districtId)) continue;
                if (!communeCheck.contains(clone.getCommuneId())){
                    CommuneDTO communeDTO = new CommuneDTO();
                    communeDTO.setCommuneId(clone.getCommuneId());
                    communeDTO.setCommuneName(clone.getCommuneName());
                    list.add(communeDTO);
                    communeCheck.add(clone.getCommuneId());
                }
            }
            return list;
        } catch (Exception e){
            logger.warn("Can not get districts !NOTICE");
            return null;
        }
    }

    @Override
    public Page<User> findByKeyword(String keyword, Pageable pageable) {
        return this.userRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public User updateUserByAdmin(Long id, String roleName, boolean isActive) throws Exception {
        User user = this.getUserById(id);
        if (roleName.toUpperCase().equals(Role.ADMIN)){
            user.setRole(new Role(2L,roleName));
        } else {
            user.setRole(new Role(1L, roleName));
        }
        user.setRole(new Role(user.getRole().getId(),roleName.toLowerCase()));
        user.setActive(isActive);
        return this.userRepository.save(user);
    }
}
