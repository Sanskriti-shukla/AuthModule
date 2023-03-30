package com.example.auth.decorator.user;

import com.example.auth.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor


@NoArgsConstructor
@Builder
public class UsersAddRequest {
    @NotNull
    String firstName;

   String middleName;
   String lastName;
        double age;
    String occupation;
    double salary;
    Address address;
    String email;
    String phoneNumber;

}
