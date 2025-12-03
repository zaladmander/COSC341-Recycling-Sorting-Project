package com.example.cosc341_recycling_sorting_project.ui.depots;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cosc341_recycling_sorting_project.R;

public class PermissionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_permission, container, false);

        Button btnContinue = view.findViewById(R.id.btnContinueToMap);
        Button btnNoThanks = view.findViewById(R.id.btnNoThanks);
        TextView tvDataPolicy = view.findViewById(R.id.tvDataPolicy);

        // → Go to Map Fragment
        btnContinue.setOnClickListener(v ->
                NavHostFragment.findNavController(PermissionFragment.this)
                        .navigate(R.id.navigation_map)
        );

        // → Go to List Fragment
        btnNoThanks.setOnClickListener(v ->
                NavHostFragment.findNavController(PermissionFragment.this)
                        .navigate(R.id.navigation_list)
        );

        // → Go to Data Policy
        tvDataPolicy.setOnClickListener(v ->
                NavHostFragment.findNavController(PermissionFragment.this)
                        .navigate(R.id.navigation_data_policy)
        );

        return view;
    }
}
