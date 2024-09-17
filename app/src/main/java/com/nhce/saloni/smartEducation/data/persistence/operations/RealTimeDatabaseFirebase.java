package com.nhce.saloni.smartEducation.data.persistence.operations;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.nhce.saloni.smartEducation.data.persistence.core.RealTimeDatabaseFirebaseConfig;

import java.util.HashMap;

import io.reactivex.subjects.PublishSubject;

public class RealTimeDatabaseFirebase<T> extends RealTimeDatabaseFirebaseConfig {

    public PublishSubject<HashMap<String, T>> datatableObservableSubject;
    public PublishSubject<T> datarowObservableSubject;

    protected RealTimeDatabaseFirebase(String pathFromRootNode, final Class<T> serializeToClass) {
        super(pathFromRootNode);

        this.datatableObservableSubject = PublishSubject.create();
        this.datarowObservableSubject = PublishSubject.create();
        setChildEventListener(serializeToClass, null);
    }

    protected RealTimeDatabaseFirebase(String pathFromRootNode, String leafNodeTitleValue, final Class<T> serializeToClass) {
        super(pathFromRootNode);

        this.datatableObservableSubject = PublishSubject.create();
        this.datarowObservableSubject = PublishSubject.create();
        setChildEventListener(serializeToClass, leafNodeTitleValue);
    }

    public static RealTimeDatabaseFirebase getInstance(String pathFromRootNode, final Class serializeToClass){
        return new RealTimeDatabaseFirebase(pathFromRootNode, serializeToClass);
    }

    public static RealTimeDatabaseFirebase getInstance(String pathFromRootNode,  String leafNodeTitleValue, final Class serializeToClass){
        return new RealTimeDatabaseFirebase(pathFromRootNode, leafNodeTitleValue, serializeToClass);
    }


    @Override
    protected void setChildEventListener(final Class serializeToClass, String leafNodeTitleValue) {
        this.databaseReference.addChildEventListener(getChildEventListener(serializeToClass, leafNodeTitleValue));
    }

    public Boolean insertDataRow(String rowId, T dataRow) {
        Boolean returnFlag;
        try {
            ((rowId  == null)? this.databaseReference.push(): this.databaseReference.child(rowId)).setValue(dataRow);
            returnFlag = true;
        } catch (Exception e) {
            returnFlag = false;
        }
        return returnFlag;
    }

    public ChildEventListener getChildEventListener(final Class<T> serializeToClass, String leafNodeTitleValue){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("++++++++++++++++++++++++++++++", snapshot.getValue().toString());
                datatable.put(snapshot.getKey(), snapshot.getValue(serializeToClass));
                datatableObservableSubject.onNext(datatable);
                if(leafNodeTitleValue != null && (snapshot.getKey().compareTo(leafNodeTitleValue) == 0? true: false)){
                    datarowObservableSubject.onNext(snapshot.getValue(serializeToClass));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                datatable.computeIfPresent(snapshot.getKey(), (k, v) -> snapshot.getValue(serializeToClass));
                datatableObservableSubject.onNext(datatable);
                if(leafNodeTitleValue != null && snapshot.getKey() == leafNodeTitleValue){
                    datarowObservableSubject.onNext(snapshot.getValue(serializeToClass));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                datatable.remove(snapshot.getKey());
                datatableObservableSubject.onNext(datatable);
                if(leafNodeTitleValue != null && snapshot.getKey() == leafNodeTitleValue){
                    datarowObservableSubject.onNext(snapshot.getValue(serializeToClass));
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }

}
