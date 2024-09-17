package com.nhce.saloni.smartEducation.data.persistence.core;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import lombok.Getter;

public class RealTimeDatabaseFirebaseConfig<T> {

    @Getter
    protected HashMap<String, T> datatable;
    protected DatabaseReference databaseReference;

    protected RealTimeDatabaseFirebaseConfig(String pathFromRootNode){
        this.datatable = new HashMap<String, T>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child(pathFromRootNode);
    }

    protected void setChildEventListener(final Class serializeToClass, String LeafNodeTitleValue) {
    }
}
