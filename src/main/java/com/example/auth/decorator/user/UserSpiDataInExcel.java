package com.example.auth.decorator.user;

import com.example.auth.commons.decorator.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSpiDataInExcel {
    String _id;
    String firstName;
    String email;
    String semester;
    int sum;
    int count;
    int average;


    @ExcelField(excelHeader = "Id", position = 2)
    public String getId() {
        return _id;

    }

    @ExcelField(excelHeader = "firstName", position = 3)
    public String getName() {
        return firstName;

    }

    @ExcelField(excelHeader = "Email", position = 4)
    public String getEmail() {
        return email;

    }

    @ExcelField(excelHeader = "Semester", position = 5)
    public String getSemester() {
        return semester;

    }

}
