package com.example.auth.decorator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsResponse {
    String userId;

    String name;

    String comment;

    Date createdOn;
}
