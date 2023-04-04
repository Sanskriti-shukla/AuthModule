package com.example.auth.controller;
import com.example.auth.commons.Access;
import com.example.auth.commons.constant.ResponseConstant;
import com.example.auth.commons.decorator.AdminResponse;
import com.example.auth.commons.enums.Role;
import com.example.auth.commons.model.AdminConfiguration;
import com.example.auth.commons.service.AdminConfigurationService;
import com.example.auth.commons.decorator.DataResponse;
import com.example.auth.commons.decorator.Response;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("adminConfiguration")
public class AdminController {
    private final AdminConfigurationService adminConfigurationService;

    public AdminController(AdminConfigurationService adminConfigurationService) {
        this.adminConfigurationService = adminConfigurationService;
    }

    @PostMapping(name = "addConfiguration", value = "/add")
    @Access(levels = Role.ADMIN)
    public DataResponse<AdminResponse> addConfiguration()  {
        DataResponse<AdminResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(adminConfigurationService.addConfiguration());
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;
    }


    @GetMapping(name = "getConfigurationDetails", value = "/getDetails")
    @Access(levels = Role.ADMIN)
    public DataResponse<AdminConfiguration> getConfigurationDetails()  {
        DataResponse<AdminConfiguration> dataResponse = new DataResponse<>();
        dataResponse.setData(adminConfigurationService.getConfiguration());
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }
}
