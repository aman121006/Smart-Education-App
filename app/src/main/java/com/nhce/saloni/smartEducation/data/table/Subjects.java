package com.nhce.saloni.smartEducation.data.table;

import com.nhce.saloni.smartEducation.data.model.Subject;
import com.nhce.saloni.smartEducation.data.persistence.operations.RealTimeDatabaseFirebase;


public class Subjects extends RealTimeDatabaseFirebase {
    public static Subjects SubjectsTable = null;

    private Subjects(){
        super("Subjects", Subject.class);
    }

    public static Subjects getInstance(){
        return SubjectsTable == null? new Subjects(): SubjectsTable;
    }

}