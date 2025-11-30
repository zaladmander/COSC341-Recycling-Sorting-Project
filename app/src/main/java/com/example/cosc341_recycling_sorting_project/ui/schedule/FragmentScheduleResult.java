package com.example.cosc341_recycling_sorting_project.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cosc341_recycling_sorting_project.R;

public class FragmentScheduleResult extends Fragment {

    public FragmentScheduleResult(){
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_result, container, false);
        TextView txtSchedInfo = view.findViewById(R.id.txtSchedInfo);
        TextView txtSelectedHood = view.findViewById(R.id.txtSelectedHood);

        //receive the selected neighbourhood and place where necessary
        if (getArguments() != null) {
            String hood = getArguments().getString("selected_hood");
            txtSelectedHood.setText("Neighbourhood: "+ hood.toUpperCase());
            txtSchedInfo.setText("According to your selected neighbourhood you are in DAYOFWEEK A/B.... This means for the week of DATE you should put out your RECYCLING/COMPOST and GARBAGE bins.");

        }


        return view;
    }

}
