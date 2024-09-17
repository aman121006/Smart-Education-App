package com.nhce.saloni.smartEducation.data.injection;

import lombok.Getter;
import lombok.Setter;


public class RealTimeData {
    private static RealTimeData data_instance = null;

    @Getter
    @Setter
    private static String userId;
    @Getter
    @Setter
    private static String userName;
    @Getter
    @Setter
    private static String userListType;
    @Getter
    @Setter
    private static String subjectId;
    @Getter
    @Setter
    private static String date;
    @Getter
    @Setter
    private static String startTime;

    private RealTimeData() {
        userId = "1001";
        userName = "Albert Einstein";
        userListType = "teacherList";
        subjectId = null;
        date = null;
        startTime = null;
    }
    public static RealTimeData getInstance() {
        if (data_instance == null)
            data_instance = new RealTimeData();
        return data_instance;
    }

}
