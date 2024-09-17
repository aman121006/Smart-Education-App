package com.nhce.saloni.smartEducation.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    @Getter
    private String subjectId;
    @Getter
    private String subjectName;
    @Getter
    private String userId;

}
