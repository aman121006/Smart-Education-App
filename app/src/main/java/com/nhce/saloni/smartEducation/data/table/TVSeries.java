package com.nhce.saloni.smartEducation.data.table;

import com.nhce.saloni.smartEducation.data.model.TVSerial;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;

public class TVSeries extends RealTimeDatabaseFirebase {
    public static TVSeries tVSeriesTable = null;

    private TVSeries(){
        super("TvSeries", TVSerial.class);
    }

    public static TVSeries getInstance(){
        return tVSeriesTable == null? new TVSeries(): tVSeriesTable;
    }
}
