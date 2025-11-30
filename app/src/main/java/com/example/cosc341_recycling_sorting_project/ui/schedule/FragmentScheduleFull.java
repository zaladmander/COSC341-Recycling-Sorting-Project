package com.example.cosc341_recycling_sorting_project.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cosc341_recycling_sorting_project.R;

public class FragmentScheduleFull extends Fragment {

    public FragmentScheduleFull(){
        //blank constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_schedule_full, container, false);

        //button to return to hood lookup
        Button btnLookup = view.findViewById(R.id.btnLookup);
        btnLookup.setOnClickListener(v -> {
            NavHostFragment.findNavController(FragmentScheduleFull.this)
                    .navigate(R.id.action_schedule_full_to_pickup);
        });
        return view;
    }

}
