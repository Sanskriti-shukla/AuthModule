package com.example.auth.decorator.user;

import com.example.auth.decorator.user.UserSpiData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class UserSpiResponse extends HashMap<String, List<UserSpiDataInExcel>> {
    String _id;
    List<UserSpiData> auth;
    int sum;
    int count;
    int average;

}
