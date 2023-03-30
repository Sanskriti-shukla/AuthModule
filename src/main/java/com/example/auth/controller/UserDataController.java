package com.example.auth.controller;


import com.example.auth.commons.Access;
import com.example.auth.commons.constant.ResponseConstant;
import com.example.auth.commons.decorator.DataResponse;
import com.example.auth.commons.decorator.ListResponse;
import com.example.auth.commons.decorator.Response;
import com.example.auth.commons.enums.Role;
import com.example.auth.stockPile.decorator.UserAddRequest;
import com.example.auth.stockPile.decorator.UserDataResponse;
import com.example.auth.stockPile.service.UserDataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userData")
public class UserDataController {
    private final UserDataService userDataService;


    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @RequestMapping(name = "addUser", value = "/add", method = RequestMethod.POST)
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<UserDataResponse> addUser(@RequestBody UserAddRequest userAddRequest) {
        DataResponse<UserDataResponse> dataResponse = new DataResponse<>();
        System.out.println("hello");
        dataResponse.setData(userDataService.addUser(userAddRequest));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;

    }


    @RequestMapping(name = "updateUser", value = "/update/user", method = RequestMethod.POST)
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> updateUser(@RequestBody UserAddRequest userAddRequest, @RequestParam String id) throws NoSuchFieldException, IllegalAccessException {
        DataResponse<Object> dataResponse = new DataResponse<>();
        userDataService.updateUser(userAddRequest, id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UPDATED_SUCCESSFULLY));
        return dataResponse;
    }


    @RequestMapping(name = "getUserById", value = "/user/{id}", method = RequestMethod.GET)
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<UserDataResponse> getUserById(@PathVariable String id) {
        DataResponse<UserDataResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(userDataService.getUserById(id));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }

    @RequestMapping(name = "getAllUser", value = "/get/all/users", method = RequestMethod.GET)
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<UserDataResponse> getAllUser() {
        ListResponse<UserDataResponse> listResponse = new ListResponse<>();
        listResponse.setData(userDataService.getAllUser());
        listResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return listResponse;
    }

    @RequestMapping(name = "deleteUser", value = "/delete/user", method = RequestMethod.POST)
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> updateUser(@RequestParam String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        userDataService.deleteUser(id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.DELETED_SUCCESSFULLY));
        return dataResponse;
    }


    @RequestMapping(name = "getUserIdByEmail", value = "/userid/email",method = RequestMethod.GET)
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<String> getUserIdByEmail(@RequestParam String email){
        DataResponse<String> dataResponse= new DataResponse<>();
        dataResponse.setData(userDataService.getUserIdByEmail(email));
        dataResponse.setStatus(Response.getOkResponse());
        return  dataResponse;
    }

}
