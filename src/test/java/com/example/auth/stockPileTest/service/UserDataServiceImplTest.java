package com.example.auth.stockPileTest.service;

import com.example.auth.commons.helper.UserHelper;
import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.stockPile.repository.UserDataRepository;
import com.example.auth.stockPile.service.UserDataService;
import com.example.auth.stockPile.service.UserDataServiceImpl;
import com.example.auth.stockPileTest.helper.UserDataServiceImplTestGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
 class UserDataServiceImplTest {
    private final static String  userId = "id";
    private final static String  email = "sans@gmail.com";
    private final UserDataRepository userDataRepository = mock(UserDataRepository.class);
    private final ModelMapper modelMapper = UserDataServiceImplTestGenerator.getModelMapper();
    private final UserHelper userHelper = mock(UserHelper.class);
    private final AdminConfigurationService adminConfigurationService = mock(AdminConfigurationService.class);

    public UserDataService userDataService = new UserDataServiceImpl(userDataRepository,modelMapper , userHelper,adminConfigurationService);

    @Test
    void testAddUserData(){

        AdminConfiguration adminConfiguration=adminConfigurationService.getConfiguration();

        var userData = UserDataServiceImplTestGenerator.mockUserData();

        var configuration = UserDataServiceImplTestGenerator.getMockAdminConfiguration();

        var userResponse = UserDataServiceImplTestGenerator.mockUserDataResponse(null);

        var userDataAddRequest = UserDataServiceImplTestGenerator.mockUserAddRequest();

        when(userDataRepository.save(userData)).thenReturn(userData);

        when(adminConfigurationService.getConfiguration()).thenReturn(configuration);

        var actualData = userDataService.addUser(userDataAddRequest);

        Assertions.assertEquals(userResponse,actualData);


    }

    @Test

    void testUpdateUserData() throws NoSuchFieldException, IllegalAccessException {

        var userData = UserDataServiceImplTestGenerator.mockUserData();

        var userDataAddRequest = UserDataServiceImplTestGenerator.mockUserAddRequest();

        when(userDataRepository.findByIdAndSoftDeleteIsFalse(userId)).thenReturn(java.util.Optional.ofNullable(userData));


        userDataService.updateUser(userDataAddRequest,userId);

        verify(userDataRepository, times(1)).findByIdAndSoftDeleteIsFalse(userId);

    }

    @Test

    void testGetUserDataById(){

        var userData = UserDataServiceImplTestGenerator.mockUserData();

        var userResponse = UserDataServiceImplTestGenerator.mockUserDataResponse(userId);

        when(userDataRepository.findByIdAndSoftDeleteIsFalse(userId)).thenReturn(java.util.Optional.ofNullable(userData));

        var actualData = userDataService.getUserById(userId);

        Assertions.assertEquals(userResponse,actualData);
    }

    @Test
    void testGetAllUserData(){

        var users = UserDataServiceImplTestGenerator.mockAllUserData();

        var userResponses = UserDataServiceImplTestGenerator.mockListOfUserDataResponse();

        when(userDataRepository.findAllBySoftDeleteFalse()).thenReturn(users);

        var actualData = userDataService.getAllUser();

        Assertions.assertEquals(userResponses,actualData);

    }

    @Test

    void  testDeleteUserDataById(){

        var userData = UserDataServiceImplTestGenerator.mockUserData();

        when(userDataRepository.findByIdAndSoftDeleteIsFalse(userId)).thenReturn(java.util.Optional.ofNullable(userData));

        userDataService.deleteUser(userId);

        verify(userDataRepository , times( 1)).findByIdAndSoftDeleteIsFalse(userId);
    }

    @Test
    void testgetUserIdByEmail(){
        var userData = UserDataServiceImplTestGenerator.mockUserData();

        when(userDataRepository.findByEmailAndSoftDeleteIsFalse(email)).thenReturn(java.util.Optional.ofNullable(userData));


        var actualData = userDataService.getUserIdByEmail(email);

        Assertions.assertEquals(userData.getId(), actualData);

    }
}
