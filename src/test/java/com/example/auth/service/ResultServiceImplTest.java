package com.example.auth.service;

import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.helper.ResultServiceImplTestGenerator;
import com.example.auth.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ResultServiceImplTest {

    private static final String id = "id";

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ModelMapper modelMapper = ResultServiceImplTestGenerator.getModelMapper();
    private final AdminConfigurationService adminConfigurationService= mock(AdminConfigurationService.class);

    public ResultService resultService = new ResultServiceImpl(userRepository, modelMapper, adminConfigurationService);

    @Test
    void TestAddResult() throws InvocationTargetException, IllegalAccessException {

        //given
        var user = ResultServiceImplTestGenerator.getMockUser();
        var addResult = ResultServiceImplTestGenerator.MockAddResult();
        var Resultresponse = ResultServiceImplTestGenerator.MockGetUserResponse();
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);
        //when
        var actualData = resultService.addResult(id, addResult);

        //then
        Assertions.assertEquals(Resultresponse, actualData);
    }

    @Test
    void testAddResult() throws InvocationTargetException, IllegalAccessException {
        //given

        var results = ResultServiceImplTestGenerator.MockListResult();
        var addResult = ResultServiceImplTestGenerator.MockAddResult();
        var user = ResultServiceImplTestGenerator.getMockUser();

        var Resultresponse = ResultServiceImplTestGenerator.MockGetUserResponse();

        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(user));

        //when
        var actualData = resultService.addResult(id, addResult);

        //then
        Assertions.assertEquals(Resultresponse, actualData);
    }


    @Test
    void TestSpi() {
        var spiResponse = ResultServiceImplTestGenerator.getMockUserSpiResponse();
        when(userRepository.getUserBySpi(5.6)).thenReturn(spiResponse);

        var actualData = resultService.getBySpi(5.6);
        Assertions.assertEquals(spiResponse, actualData);

    }

}

