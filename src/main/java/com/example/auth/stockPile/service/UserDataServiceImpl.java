package com.example.auth.stockPile.service;


import com.example.auth.commons.constant.MessageConstant;
import com.example.auth.commons.exception.InvalidRequestException;
import com.example.auth.commons.exception.NotFoundException;
import com.example.auth.commons.helper.UserHelper;
import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.stockPile.decorator.UserAddRequest;
import com.example.auth.stockPile.decorator.UserDataResponse;
import com.example.auth.stockPile.model.UserData;
import com.example.auth.stockPile.repository.UserDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserDataServiceImpl implements UserDataService {
    private final UserDataRepository userDataRepository;
    private final ModelMapper modelMapper;
    private final UserHelper userHelper;
    private final AdminConfigurationService adminConfigurationService;

    public UserDataServiceImpl(UserDataRepository userDataRepository, ModelMapper modelMapper, UserHelper userHelper, AdminConfigurationService adminConfigurationService) {
        this.userDataRepository = userDataRepository;
        this.modelMapper = modelMapper;
        this.userHelper = userHelper;
        this.adminConfigurationService = adminConfigurationService;
    }

    @Override
    public UserDataResponse addUser(UserAddRequest userAddRequest) {
        checkValidation(userAddRequest);
        UserData userData = modelMapper.map(userAddRequest, UserData.class);
        userDataRepository.save(userData);
        UserDataResponse userDataResponse = modelMapper.map(userData, UserDataResponse.class);
        return userDataResponse;
    }

    @Override
    public void updateUser(UserAddRequest userAddRequest, String id) throws NoSuchFieldException, IllegalAccessException {
        UserData userData = userById(id);
        update(userAddRequest, id);
        userHelper.difference(userAddRequest, userData);


    }

    @Override
    public void deleteUser(String id) {
        UserData userData=userById(id);
        userData.setSoftDelete(true);
        userDataRepository.save(userData);
    }

    @Override
    public UserDataResponse getUserById(String id) {
       UserData userData= userById(id);
      return  modelMapper.map(userData,UserDataResponse.class);

    }

    @Override
    public List<UserDataResponse> getAllUser() {
      List<UserData> userData= userDataRepository.findAllBySoftDeleteFalse();
      List<UserDataResponse> userDataResponses= new ArrayList<>();
      userData.forEach(userData1 -> {
          UserDataResponse userDataResponse= modelMapper.map(userData1,UserDataResponse.class);
          userDataResponses.add(userDataResponse);

      });
        return userDataResponses;
    }

    @Override
    public String getUserIdByEmail(String email) {
        log.info("email:{}",email);
        UserData userData = userByEmail(email);
        return  userData.getId();
    }


    private UserData userByEmail(String email) {
        return  userDataRepository.findByEmailAndSoftDeleteIsFalse(email).orElseThrow(()-> new NotFoundException(MessageConstant.EMAIL_NOT_FOUND));
    }


    public UserData userById(String id) {
        return userDataRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_ID_NOT_FOUND));
    }


    public void checkValidation(UserAddRequest userAddRequest){
        AdminConfiguration adminConfiguration=adminConfigurationService.getConfiguration();
        if (userDataRepository.existsByEmailAndSoftDeleteIsFalse(userAddRequest.getEmail())){
            throw  new InvalidRequestException(MessageConstant.EMAIL_ALREADY_EXIST);

        }
        if (!userAddRequest.getEmail().matches(adminConfiguration.getEmailRegex())){
            throw  new InvalidRequestException(MessageConstant.INVALID_EMAIL);
        }

        if (!userAddRequest.getContact().matches(adminConfiguration.getMobileNoRegex())) {
            throw new InvalidRequestException(MessageConstant.INVALID_PHONE_NUMBER);
        }

    }

    private void update(UserAddRequest userAddRequest, String id) {
        UserData userData = userById(id);

        if (userAddRequest.getUserName() != null) {
            userData.setUserName(userAddRequest.getUserName());
        }

        if (userAddRequest.getContact() != null) {
            userData.setContact(userAddRequest.getContact());
        }
        if (userAddRequest.getName() != null) {
            userData.setName(userAddRequest.getName());
        }
        if (userAddRequest.getEmail() != null) {
            userData.setEmail(userAddRequest.getEmail());
        }
        userDataRepository.save(userData);
    }

}
