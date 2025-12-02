package com.example.cosc341_recycling_sorting_project.ui.identification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cosc341_recycling_sorting_project.R;

public class RecyclableDetailFragment extends Fragment {

    public RecyclableDetailFragment() {
        // required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclable_detail, container, false);

        ImageView image = view.findViewById(R.id.imageRecyclableLarge);
        TextView title = view.findViewById(R.id.textRecyclableTitle);
        TextView description = view.findViewById(R.id.textRecyclableDescription);

        Bundle args = getArguments();
        if (args != null) {
            title.setText(args.getString("name", ""));
            description.setText(args.getString("description", ""));
            int resId = args.getInt("imageResId", 0);
            if (resId != 0) {
                image.setImageResource(resId);
            }
        }

        return view;
    }
}
