package com.nhce.saloni.smartEducation.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TVSerial {
    @Getter
    private String title;
    @Getter
    private String description;
    @Getter
    private String price;
    @Getter
    private String imageURL;
}
