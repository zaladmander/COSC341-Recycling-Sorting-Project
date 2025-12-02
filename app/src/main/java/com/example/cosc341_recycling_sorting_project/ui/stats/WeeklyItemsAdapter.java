package com.example.cosc341_recycling_sorting_project.ui.stats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341_recycling_sorting_project.R;
import com.example.cosc341_recycling_sorting_project.ui.identification.Recyclable;

import java.util.List;

public class WeeklyItemsAdapter extends RecyclerView.Adapter<WeeklyItemsAdapter.Holder> {

    public interface OnItemClick {
        void onClick(Recyclable item);
    }

    private final List<Recyclable> items;
    private final OnItemClick listener;

    public WeeklyItemsAdapter(List<Recyclable> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weekly_summary_items, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Recyclable it = items.get(position);
        holder.name.setText(it.getName());
        holder.sub.setText(it.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(it);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView sub;
        Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            sub = itemView.findViewById(R.id.item_sub);
        }
    }
}
