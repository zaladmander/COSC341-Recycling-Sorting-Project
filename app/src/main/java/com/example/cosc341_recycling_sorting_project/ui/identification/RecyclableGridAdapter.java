package com.example.cosc341_recycling_sorting_project.ui.identification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341_recycling_sorting_project.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclableGridAdapter
        extends RecyclerView.Adapter<RecyclableGridAdapter.RecyclableViewHolder> {

    public interface OnRecyclableClickListener {
        void onRecyclableClick(Recyclable recyclable);
    }

    private final Context context;
    private final List<Recyclable> items = new ArrayList<>();
    private final OnRecyclableClickListener listener;

    public RecyclableGridAdapter(Context context, OnRecyclableClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void submitList(List<Recyclable> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_recyclable_child, parent, false);
        return new RecyclableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclableViewHolder holder, int position) {
        Recyclable r = items.get(position);
        holder.name.setText(r.getName());
        holder.image.setImageResource(r.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecyclableClick(r);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RecyclableViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        RecyclableViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageRecyclable);
            name = itemView.findViewById(R.id.textRecyclableName);
        }
    }
}
