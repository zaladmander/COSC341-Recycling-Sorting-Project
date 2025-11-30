package com.example.cosc341_recycling_sorting_project.ui.schedule;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cosc341_recycling_sorting_project.R;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

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

        //grab this weeks even or oddness
        LocalDate today = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        // Use the system’s locale week numbering (Canada starts weeks on Monday)
        WeekFields weekFields = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            weekFields = WeekFields.of(Locale.getDefault());
        }
        // Get the week number for today's date
        int weekNumber = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            weekNumber = today.get(weekFields.weekOfWeekBasedYear());
        }
        // Check if even or odd
        boolean isEvenWeek = (weekNumber % 2 == 0);

        //receive the selected neighbourhood and place where necessary
        if (getArguments() != null) {
            String hood = getArguments().getString("selected_hood");
            String zone = getArguments().getString("selected_zone");
            txtSelectedHood.setText("Neighbourhood: " + hood.toUpperCase());
            if (isEvenWeek) {
                txtSchedInfo.setText("According to your selected neighbourhood you are in Zone: " + zone.toUpperCase() + "\nThis means for the week of "+ today.toString() +" you should put out your COMPOST and GARBAGE bins on " + zone.split(" ")[0] + ".");
            }else {//must be an odd week
                txtSchedInfo.setText("According to your selected neighbourhood you are in Zone: " + zone.toUpperCase() + "\nThis means for the week of "+ today.toString() +" you should put out your RECYCLING and GARBAGE bins on " + zone.split(" ")[0] + ".");
            }
        }

        Button btnSeeFullSched = view.findViewById(R.id.btnSeeFullSched);
        btnSeeFullSched.setOnClickListener(v -> {
            NavHostFragment.findNavController(FragmentScheduleResult.this)
                    .navigate(R.id.action_schedule_result_to_full);
        });


        return view;
    }

}
