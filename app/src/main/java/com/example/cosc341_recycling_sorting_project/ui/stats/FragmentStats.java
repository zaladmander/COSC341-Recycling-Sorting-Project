package com.example.cosc341_recycling_sorting_project.ui.stats;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cosc341_recycling_sorting_project.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FragmentStats extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentStats() {
        // Required empty public constructor
    }

    public static FragmentStats newInstance(String param1, String param2) {
        FragmentStats fragment = new FragmentStats();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // References to views
        TextView textGreeting = view.findViewById(R.id.text_greeting);
        TextView textStreakTitle = view.findViewById(R.id.text_streak_title);
        TextView textStreakDesc = view.findViewById(R.id.text_streak_desc);
        Button btnWeeklySummary = view.findViewById(R.id.btn_weekly_summary);
        Button btnCommunity = view.findViewById(R.id.btn_community);

        // Hard coded display content
        textGreeting.setText("Hello, User!");
        textStreakTitle.setText("3-Week Recycling Streak");
        textStreakDesc.setText("1 week to next tier");

        // Button click behavior

        btnWeeklySummary.setOnClickListener(v -> {
            // TODO: navigate to Weekly Summary fragment
             NavHostFragment.findNavController(FragmentStats.this)
                 .navigate(R.id.action_fragmentStats_to_weeklySummary);
        });

        btnCommunity.setOnClickListener(v -> {
            // TODO: navigate to Community fragment
            NavHostFragment.findNavController(FragmentStats.this)
                    .navigate(R.id.action_fragmentStats_to_community);
        });

    }
}