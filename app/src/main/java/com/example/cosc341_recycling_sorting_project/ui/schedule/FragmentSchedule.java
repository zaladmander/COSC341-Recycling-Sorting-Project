package com.example.cosc341_recycling_sorting_project.ui.schedule;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.cosc341_recycling_sorting_project.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSchedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSchedule extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSchedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSchedule.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSchedule newInstance(String param1, String param2) {
        FragmentSchedule fragment = new FragmentSchedule();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Button
        View btnSeeSchedule = view.findViewById(R.id.btnSeeSchedule);
        //Spinner
        Spinner spnHoods = view.findViewById(R.id.spnHood);

        btnSeeSchedule.setOnClickListener(v -> {
           //get and pass the selected neighbourhood to the next fragment
            String hood = spnHoods.getSelectedItem().toString();

            Bundle bundle = new Bundle();
            bundle.putString("selected_hood", hood);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_schedule_to_scheduleDetails, bundle);
        });

        return view;
    }
}