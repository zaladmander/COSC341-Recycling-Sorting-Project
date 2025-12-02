package com.example.cosc341_recycling_sorting_project.ui.stats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341_recycling_sorting_project.R;

import java.util.List;

public class TopRecyclersAdapter extends RecyclerView.Adapter<TopRecyclersAdapter.Holder> {

    private final List<TopRecycler> items;
    private final Context ctx;

    public TopRecyclersAdapter(Context context, List<TopRecycler> items) {
        this.items = items;
        this.ctx = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_recycler, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TopRecycler t = items.get(position);
        holder.name.setText(t.getName());
        holder.rank.setText(String.valueOf(t.getRank()));
        holder.points.setText(t.getPoints() + " kg");

        // Choose badge drawable based on rank
        int badgeRes = getBadgeDrawableForRank(t.getRank());
        if (badgeRes != 0) {
            holder.badge.setImageResource(badgeRes);
        } else {
            // fallback: a default circular background resource or color
            holder.badge.setImageResource(R.drawable.bronze_badge);
        }

    }

    private int getBadgeDrawableForRank(int rank) {
        // mapping: 1 -> gold, 2 -> silver, 3 -> bronze, 4+ -> platinum
        switch (rank) {
            case 1:
                return R.drawable.gold_badge;
            case 2:
                return R.drawable.silver_badge;
            case 3:
                return R.drawable.bronze_badge;
            default:
                return R.drawable.plat_badge;
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class Holder extends RecyclerView.ViewHolder {

        final ImageView badge;
        final TextView rank;
        final TextView name;
        final TextView points;

        Holder(@NonNull View itemView) {
            super(itemView);
            badge = itemView.findViewById(R.id.iv_badge);
            rank = itemView.findViewById(R.id.tv_rank);
            name = itemView.findViewById(R.id.tv_recycler_name);
            points = itemView.findViewById(R.id.tv_recycler_points);
        }
    }
}
