package com.example.cosc341_recycling_sorting_project.ui.stats;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cosc341_recycling_sorting_project.R;
import com.example.cosc341_recycling_sorting_project.ui.identification.Recyclable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeeklySummaryFragment extends Fragment {

    private TextView tvKgsValue, tvItemsCount;
    private RecyclerView rvItems;
    private WeeklyItemsAdapter adapter;
    private List<Recyclable> sampleItems;

    public WeeklySummaryFragment() {
        // Required empty public constructor
    }

    public static WeeklySummaryFragment newInstance() {
        return new WeeklySummaryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_weekly_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwipeRefreshLayout swipe = view.findViewById(R.id.swipe_refresh);
        ProgressBar progress = view.findViewById(R.id.progress_loading);
        tvKgsValue = view.findViewById(R.id.tv_kgs_value);
        tvItemsCount = view.findViewById(R.id.tv_items_count);
        rvItems = view.findViewById(R.id.rv_weekly_items);
        Button btnSeeItems = view.findViewById(R.id.btn_see_items);

        // Setup RecyclerView immediately with an empty adapter so toggling will show a real view
        rvItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        sampleItems = new ArrayList<>();
        adapter = new WeeklyItemsAdapter(sampleItems, item -> {
            // placeholder item click handling
            Log.d("WeeklySummary", "Item clicked: " + item.getName());
            // TODO: navigate to item detail if desired
        });
        rvItems.setAdapter(adapter);

        // default hidden state and initial button text
        rvItems.setVisibility(View.GONE);
        btnSeeItems.setText("See Items");

        // ViewModel wiring
        WeeklySummaryViewModel vm = new ViewModelProvider(this).get(WeeklySummaryViewModel.class);


        // Swipe refresh listener
        swipe.setOnRefreshListener(() -> vm.fetchWeeklySummary());


        // Observe loading & data
        vm.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean isLoading = loading != null && loading;
            swipe.setRefreshing(isLoading);
            progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        vm.getKgs().observe(getViewLifecycleOwner(), val -> {
            if (val != null) {
                tvKgsValue.setText(String.format(Locale.getDefault(), "%.1f", val));
            }
        });

        vm.getItems().observe(getViewLifecycleOwner(), list -> {
            // Defensive: ensure list is non-null
            List<Recyclable> newList = list != null ? list : new ArrayList<>();
            tvItemsCount.setText("Items identified: " + newList.size());

            // Update adapter data cleanly
            sampleItems.clear();
            sampleItems.addAll(newList);
            adapter.notifyDataSetChanged();

            Log.d("WeeklySummary", "Observed items count: " + newList.size());
        });

        // trigger initial load
        vm.fetchWeeklySummary();

        // Toggle behavior: show/hide and request layout so height is recalculated
        btnSeeItems.setOnClickListener(v -> {
            Log.d("WeeklySummary", "SeeItems clicked; current visibility=" + rvItems.getVisibility());
            if (rvItems.getVisibility() == View.GONE) {
                rvItems.setVisibility(View.VISIBLE);
                // Ensure it measures and scrolls properly
                rvItems.requestLayout();
                btnSeeItems.setText("Hide Items");
            } else {
                rvItems.setVisibility(View.GONE);
                btnSeeItems.setText("See Items");
            }
        });
    }

}

