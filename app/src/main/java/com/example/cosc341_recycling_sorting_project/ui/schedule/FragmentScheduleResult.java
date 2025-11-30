package com.example.cosc341_recycling_sorting_project.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cosc341_recycling_sorting_project.R;

public class FragmentScheduleResult extends Fragment {

    public FragmentScheduleResult(){
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_schedule_result, container, false);
    }
}
