package com.example.cosc341_recycling_sorting_project.ui.stats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341_recycling_sorting_project.R;
import com.example.cosc341_recycling_sorting_project.ui.identification.Recyclable;

import java.util.List;

public class WeeklyItemsAdapter extends RecyclerView.Adapter<WeeklyItemsAdapter.ViewHolder> {

    public interface OnItemClick {
        void onClick(Recyclable item);
    }

    private final List<Recyclable> items;
    private final OnItemClick clickListener;

    public WeeklyItemsAdapter(List<Recyclable> items, OnItemClick clickListener) {
        this.items = items;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public WeeklyItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_summary_items, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull WeeklyItemsAdapter.ViewHolder holder, int position) {
        Recyclable item = items.get(position);
        holder.tvName.setText(item.getName());

        // If you have a resource id on the item:
        if (item.getImageResId() != 0) {
            holder.ivPreview.setImageResource(item.getImageResId());
        } else {
            holder.ivPreview.setImageResource(R.drawable.bronze_badge); // fallback
        }

        holder.itemView.setOnClickListener(v -> clickListener.onClick(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPreview;
        TextView tvName;
        TextView tvShortDesc;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPreview = itemView.findViewById(R.id.item_icon);
            tvName = itemView.findViewById(R.id.item_name);
        }
    }
}
