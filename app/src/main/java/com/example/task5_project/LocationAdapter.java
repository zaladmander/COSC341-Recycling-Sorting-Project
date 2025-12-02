package com.example.task5_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<RecycleLocation> locations;

    public LocationAdapter(List<RecycleLocation> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        RecycleLocation loc = locations.get(position);

        holder.tvName.setText(loc.getName());
        holder.tvAddress.setText(loc.getAddress());
        holder.tvDescription.setText(loc.getDescription());

        String distanceKm = String.format(Locale.ROOT, "Distance: %.1f km", loc.distanceMeters / 1000);
        holder.tvDistance.setText(distanceKm);
        holder.tvAcceptedMaterials.setText("Accepted: " + loc.getAcceptedMaterials()); // Assumes getAcceptedMaterials() exists

        holder.layoutDetails.setVisibility(loc.expanded ? View.VISIBLE : View.GONE);

        holder.ivExpandIcon.setRotation(loc.expanded ? 90f : 0f);

        holder.itemView.setOnClickListener(v -> {
            loc.expanded = !loc.expanded;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvDescription, tvDistance, tvAcceptedMaterials;
        LinearLayout layoutDetails;
        ImageView ivExpandIcon; // NEW

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDistance = itemView.findViewById(R.id.tvDistance); // NEW
            tvAcceptedMaterials = itemView.findViewById(R.id.tvAcceptedMaterials); // NEW
            layoutDetails = itemView.findViewById(R.id.layoutDetails);
            ivExpandIcon = itemView.findViewById(R.id.ivExpandIcon); // NEW
        }
    }
}

