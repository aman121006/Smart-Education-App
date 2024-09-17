package com.nhce.saloni.smartEducation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nhce.saloni.smartEducation.data.model.EventId;

import java.util.List;

public class ListviewEvent  extends ArrayAdapter<EventId> {
    Context context;
    int resource;
    List<EventId> EventList;
    public ListviewEvent(Activity context, int resource, List<EventId> EventList) {
        super(context, resource, EventList);
        this.context = context;
        this.resource = resource;
        this.EventList=EventList;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        TextView startTime = view.findViewById(R.id.startTime);
        TextView endTime = view.findViewById(R.id.endTime);
        TextView objective = view.findViewById(R.id.objective);
        TextView readingMaterial = view.findViewById(R.id.readingMaterial);

        //getting the hero of the specified position
        EventId EventId = EventList.get(position);

        //adding values to the list item
        startTime.setText(EventId.getStartTime());
        endTime.setText(EventId.getEndTime());
        objective.setText(EventId.getObjective());
        readingMaterial.setText(EventId.getReadingMaterial());

        //finally returning the view
        return view;
    }
}
