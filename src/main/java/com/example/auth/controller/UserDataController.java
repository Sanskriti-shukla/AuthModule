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


    @PostMapping(name = "addUser", value = "/add")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<UserDataResponse> addUser(@RequestBody UserAddRequest userAddRequest) {
        DataResponse<UserDataResponse> dataResponse = new DataResponse<>();
        System.out.println("hello");
        dataResponse.setData(userDataService.addUser(userAddRequest));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.SAVED_SUCCESSFULLY));
        return dataResponse;

    }


    @PutMapping(name = "updateUser", value = "/update/user")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> updateUser(@RequestBody UserAddRequest userAddRequest, @RequestParam String id) throws NoSuchFieldException, IllegalAccessException {
        DataResponse<Object> dataResponse = new DataResponse<>();
        userDataService.updateUser(userAddRequest, id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.UPDATED_SUCCESSFULLY));
        return dataResponse;
    }



    @GetMapping(name = "getUserById", value = "user/{id}")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<UserDataResponse> getUserById(@RequestParam String id) {
        DataResponse<UserDataResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(userDataService.getUserById(id));
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return dataResponse;
    }

    @GetMapping(name = "getAllUser" , value = "/get/all/users")
    @Access(levels = Role.ANONYMOUS)
    public ListResponse<UserDataResponse> getAllUser() {
        ListResponse<UserDataResponse> listResponse = new ListResponse<>();
        listResponse.setData(userDataService.getAllUser());
        listResponse.setStatus(Response.getOkResponse(ResponseConstant.OK));
        return listResponse;
    }

<<<<<<< HEAD
    @RequestMapping(name = "deleteUser", value = "/delete/user", method = RequestMethod.POST)
=======


    @DeleteMapping(name = "deleteUser", value ="/delete/user/{id}")
>>>>>>> gitlab-changes
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<Object> deleteUser(@PathVariable String id) {
        DataResponse<Object> dataResponse = new DataResponse<>();
        userDataService.deleteUser(id);
        dataResponse.setStatus(Response.getOkResponse(ResponseConstant.DELETED_SUCCESSFULLY));
        return dataResponse;
    }


    @GetMapping(name = "getUserIdByEmail" , value = "/userid/email")
    @Access(levels = Role.ANONYMOUS)
    public DataResponse<String> getUserIdByEmail(@RequestParam String email){
        DataResponse<String> dataResponse= new DataResponse<>();
        dataResponse.setData(userDataService.getUserIdByEmail(email));
        dataResponse.setStatus(Response.getOkResponse());
        return  dataResponse;
    }
}
