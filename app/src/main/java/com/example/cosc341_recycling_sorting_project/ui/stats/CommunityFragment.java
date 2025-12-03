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
        trending.add(new Recyclable("Newspapers and flyers", "Clean newspapers, community papers and advertising flyers. Recycle loose with other paper.", R.drawable.newspapers_flyers));
        trending.add(new Recyclable("Magazines and catalogues", "All types of magazines and catalogues, including glossy ones.", R.drawable.magazines_catalogues));
        trending.add(new Recyclable("Corrugated cardboard boxes", "Shipping boxes, moving boxes and pizza boxes (with minimal food residue). Flatten large boxes for collection.", R.drawable.corrugated_cardboard));
        trending.add(new Recyclable("Plastic bottles", "Plastic bottles for food, dish soap, mouthwash, shampoo, conditioner and other household liquids. Empty and rinse; labels are fine.", R.drawable.plastic_bottles));
        trending.add(new Recyclable("Plastic jars and tubs", "Plastic jars and tubs for peanut butter, jam, nuts, spreads, yogurt, cottage cheese, sour cream and ice cream. Empty and rinse.", R.drawable.plastic_jars_tubs));
        trending.add(new Recyclable("Plastic bags and overwrap (depot only)", "Soft plastic bags for groceries, bread, produce, dry bulk foods and most frozen vegetables, plus outer wrap for diapers, flat-packed cans and large packages. Return clean and empty to a depot; not accepted in curbside carts.", R.drawable.plastic_bag_overwrap));
        trending.add(new Recyclable("Non-deposit glass bottles and jars", "Clear or coloured glass food jars and bottles that do not carry a beverage deposit. Empty, rinse and remove lids.", R.drawable.glass_jars_bottles));
        trending.add(new Recyclable("Steel food cans and lids", "Steel cans for soup, vegetables, beans and pet food, plus cookie, tea and chocolate tins. Empty and rinse; labels can stay on.", R.drawable.steel_food_cans));
        trending.add(new Recyclable("Aluminum foil and trays", "Clean aluminum foil, pie plates and take-out trays. Rinse off food before recycling.", R.drawable.aluminum_foil_trays));
        trending.add(new Recyclable("Paper cups and lids", "Disposable paper cups for hot or cold drinks. Empty and rinse. Lids go with container recycling; straws are garbage.", R.drawable.paper_cups_lids));
        trending.add(new Recyclable("Gable-top drink cartons", "Milk, cream and egg substitute cartons. Empty and rinse before recycling.", R.drawable.gable_top_cartons));
        trending.add(new Recyclable("Empty aerosol cans", "Completely empty aerosol cans for food, deodorant, hairspray, shaving cream and air freshener. Return partially full or paint aerosols to a hazardous waste program.", R.drawable.aerosol_cans));


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
