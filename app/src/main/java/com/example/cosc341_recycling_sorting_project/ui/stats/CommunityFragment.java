package com.example.cosc341_recycling_sorting_project.ui.stats;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.cosc341_recycling_sorting_project.R;
import com.example.cosc341_recycling_sorting_project.ui.identification.Recyclable;
import com.example.cosc341_recycling_sorting_project.ui.stats.TopRecycler;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private TextView tvCityName, tvCityKgs;
    private RecyclerView rvTop, rvTrending;
    private TopRecyclersAdapter topAdapter;
    private TrendingSearchesAdapter trendingAdapter;

    public CommunityFragment() { /* required empty constructor */ }

    public static CommunityFragment newInstance() { return new CommunityFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        tvCityName = view.findViewById(R.id.tv_city_name);
        tvCityKgs = view.findViewById(R.id.tv_city_kgs_value);
        rvTop = view.findViewById(R.id.rv_top_recyclers);
        rvTrending = view.findViewById(R.id.rv_trending);

        View trendingHeader = view.findViewById(R.id.layout_trending_header);
        ImageView ivTrendingChev = view.findViewById(R.id.iv_trending_chev);
        final View trendingContainer = view.findViewById(R.id.card_trending_container);

        // placeholder data (same as before)...
        List<TopRecycler> top = new ArrayList<>();
        top.add(new TopRecycler("Alex", 1, 560));
        top.add(new TopRecycler("Sam", 2, 490));
        top.add(new TopRecycler("Taylor", 3, 420));

        List<Recyclable> trending = new ArrayList<>();
        trending.add(new Recyclable("Coffee Cup", "Goes in Recycling Cart"));
        trending.add(new Recyclable("Plastic Bag", "Not recyclable in curbside"));
        trending.add(new Recyclable("Glass Bottle", "Rinse first"));
        trending.add(new Recyclable("Pizza Box", "Check grease"));
        // add more to test scrolling
        for (int i = 0; i < 8; i++) {
            trending.add(new Recyclable("Item " + (i+5), "More examples"));
        }

        // top recyclers: horizontal list (same as before)
        topAdapter = new TopRecyclersAdapter(requireContext(), top);
        LinearLayoutManager topLayout = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvTop.setLayoutManager(topLayout);
        rvTop.setAdapter(topAdapter);

        // trending: set adapter on rv_trending (it may be initially hidden)
        trendingAdapter = new TrendingSearchesAdapter(trending, item -> {
            // navigation TODO
        });
        rvTrending.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTrending.setAdapter(trendingAdapter);
        rvTrending.setNestedScrollingEnabled(true);

        // set city values
        tvCityName.setText("City of Kelowna | Month of November, 2025");
        tvCityKgs.setText("12,345");

        // Expand/collapse state (closed by default)
        trendingContainer.setVisibility(View.GONE);
        ivTrendingChev.setRotation(0f);

        trendingHeader.setOnClickListener(v -> {
            // use Transition for a simple animated reveal/hide
            androidx.transition.TransitionManager.beginDelayedTransition((ViewGroup) trendingContainer.getParent());
            if (trendingContainer.getVisibility() == View.GONE) {
                // expand
                trendingContainer.setVisibility(View.VISIBLE);
                // rotate chevron to point up
                ivTrendingChev.animate().rotation(180f).setDuration(200).start();
            } else {
                // collapse
                trendingContainer.setVisibility(View.GONE);
                ivTrendingChev.animate().rotation(0f).setDuration(200).start();
            }
        });
    }

}
