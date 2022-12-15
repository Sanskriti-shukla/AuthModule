package com.example.auth.decorator;

import com.example.auth.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String id;
    String firstName;
    String middleName;
    String lastName;
    String fullName;
    double age;
    String occupation;
    double salary;
    Address address;
    String email;
    String phoneNumber;
    List<Result> result;
    double cgpa;
    Date date;


}
