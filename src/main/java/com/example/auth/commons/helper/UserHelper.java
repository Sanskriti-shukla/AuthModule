package com.example.auth.commons.helper;

import com.example.auth.commons.advice.NullAwareBeanUtilsBean;
import com.example.auth.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHelper {
    @Autowired
    NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

    String firstName;
    String middleName;
    String lastName;
    String fullName;


    public String getFullName(User user) {
        this.firstName = StringUtils.normalizeSpace(user.getFirstName());
        this.middleName = StringUtils.normalizeSpace(user.getMiddleName());
        this.lastName = StringUtils.normalizeSpace(user.getLastName());

        List<String> fullNameList = new ArrayList<>();

        fullNameList.add(firstName);
        fullNameList.add(middleName);
        fullNameList.add(lastName);
        String string = fullNameList.stream().filter(Objects::nonNull).map(String::trim).collect(Collectors.joining(" "));
        this.fullName = string;
        String[] names = string.split(" ");
        if (names.length == 1) {
            this.firstName = names[0];
        } else if (names.length == 2) {
            this.firstName = names[0];
            this.lastName = names[0];

        } else if (names.length == 3) {
            this.firstName = names[0];
            this.middleName = names[1];
            this.lastName = names[2];
        } else if (names.length > 3) {
            this.firstName = names[0];
            this.middleName = names[1];
            StringBuilder stringBuilder = new StringBuilder();
            for (String value : names) {
                if (!value.equals(firstName) && !value.equals(lastName)) {
                    stringBuilder.append(" ").append(value);
                }
            }
            this.lastName = names.toString().trim();
        }
        return string;
    }

    public  Map<String, String> difference(Object object1, Object object2) throws IllegalAccessException, NoSuchFieldException {
        Map<String, String> changedProperties = new HashMap<>();
        for (Field field : object1.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            Field field2 = object2.getClass().getDeclaredField(field.getName());
            field2.setAccessible(true);
            Object value = field.get(object1);
            Object value1 = field2.get(object2);
            if (value != null && value1 != null) {
                if (!Objects.equals(value, value1)) {
                    changedProperties.put(field.getName(), value1.toString());
                }
            }
        }
        return changedProperties;
    }
}










