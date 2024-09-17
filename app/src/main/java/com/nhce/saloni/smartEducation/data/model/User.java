package com.nhce.saloni.smartEducation.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Getter
    private String emailId;
    @Getter
    private String name;
    @Getter
    private String usn;
    @Getter
    private String password;
    @Getter
    private String userType;
}
