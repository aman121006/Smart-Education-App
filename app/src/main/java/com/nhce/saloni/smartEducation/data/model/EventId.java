package com.nhce.saloni.smartEducation.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class EventId {
    @Getter
    private String startTime;
    @Getter
    private String endTime;
    @Getter
    private String objective;
    @Getter
    private String readingMaterial;
}
