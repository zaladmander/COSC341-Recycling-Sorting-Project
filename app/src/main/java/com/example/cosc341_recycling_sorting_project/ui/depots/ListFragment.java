package com.example.cosc341_recycling_sorting_project.ui.depots;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341_recycling_sorting_project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ListFragment extends Fragment {

    private RecyclerView recycler;
    private Button btnBack;
    private EditText etSearch;
    private ImageButton btnSort;
    private TextView tvRangeValue;

    private List<RecycleLocation> allLocations;
    private List<RecycleLocation> visibleLocations;
    private LocationAdapter adapter;

    private FusedLocationProviderClient fusedLocationClient;
    private double userLat = 0.0;
    private double userLon = 0.0;

    private static final int REQ_LOCATION = 1002;

    private boolean sortByDistanceAscending = true;
    private double currentMaxRangeKm = 100.0;

    // ⭐ KEY FLAG: this tells us if we can show distance or not
    private boolean hasLocationAccess = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recycler = view.findViewById(R.id.recyclerLocations);
        btnBack = view.findViewById(R.id.btnBackToMap);
        etSearch = view.findViewById(R.id.etSearch);
        btnSort = view.findViewById(R.id.btnSortDistance);
        tvRangeValue = view.findViewById(R.id.tvRangeValue);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        allLocations = new ArrayList<>(LocationRepository.getLocations());

        setupBackButton();
        setupSearch();
        setupSortListener();

        return view;
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(ListFragment.this)
                        .navigate(R.id.navigation_map)
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchLastKnownLocation();
    }

    private void fetchLastKnownLocation() {

        // Check permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOCATION);
            return;
        }

        // Permission already granted
        hasLocationAccess = true;

        Task<Location> locationTask = fusedLocationClient.getLastLocation();

        locationTask.addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                userLat = location.getLatitude();
                userLon = location.getLongitude();
            } else {
                // Could not get actual location but permission exists
                Toast.makeText(requireContext(),
                        "Could not detect your location. Distance may be inaccurate.",
                        Toast.LENGTH_LONG).show();
            }

            continueListSetup();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_LOCATION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted -> try again
                hasLocationAccess = true;
                fetchLastKnownLocation();

            } else {

                // Permission denied -> show list WITHOUT distance
                hasLocationAccess = false;

                Toast.makeText(requireContext(),
                        "Location permission denied. Distance sorting hidden.",
                        Toast.LENGTH_LONG).show();

                continueListSetup();
            }
        }
    }

    private void continueListSetup() {

        // ⭐ Hide distance UI if no location access
        if (!hasLocationAccess) {
            btnSort.setVisibility(View.GONE);
            tvRangeValue.setVisibility(View.GONE);
        } else {
            btnSort.setVisibility(View.VISIBLE);
            tvRangeValue.setVisibility(View.VISIBLE);
        }

        // Calculate distance only if allowed
        if (hasLocationAccess) {
            calculateAllDistances();
        }

        visibleLocations = new ArrayList<>(allLocations);
        adapter = new LocationAdapter(visibleLocations);
        recycler.setAdapter(adapter);

        if (hasLocationAccess) {
            tvRangeValue.setText(String.format(Locale.ROOT, "%.1f km", currentMaxRangeKm));
        }

        filterLocations(etSearch.getText().toString());
    }

    private void calculateAllDistances() {
        for (RecycleLocation loc : allLocations) {
            float[] results = new float[1];
            Location.distanceBetween(userLat, userLon,
                    loc.getLatitude(), loc.getLongitude(), results);
            loc.distanceMeters = results[0];
        }
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLocations(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupSortListener() {
        btnSort.setOnClickListener(v -> {

            if (!hasLocationAccess) return; // shouldn't be visible anyway

            sortByDistanceAscending = !sortByDistanceAscending;

            int iconRes = sortByDistanceAscending ?
                    R.drawable.ic_sort_ascending : R.drawable.ic_sort_descending;

            btnSort.setImageResource(iconRes);

            filterLocations(etSearch.getText().toString());
        });
    }

    private void filterLocations(String query) {
        String lower = query.toLowerCase(Locale.ROOT);
        double maxRangeMeters = currentMaxRangeKm * 1000;

        visibleLocations.clear();

        for (RecycleLocation loc : allLocations) {

            boolean matchesSearch =
                    query.isEmpty() ||
                            loc.getName().toLowerCase(Locale.ROOT).contains(lower) ||
                            loc.getAddress().toLowerCase(Locale.ROOT).contains(lower) ||
                            loc.getDescription().toLowerCase(Locale.ROOT).contains(lower);

            boolean matchesRange =
                    !hasLocationAccess ||      // ⭐ if location denied → ALWAYS true
                            loc.distanceMeters <= maxRangeMeters;

            if (matchesSearch && matchesRange) {
                visibleLocations.add(loc);
            }
        }

        sortLocations();
    }

    private void sortLocations() {

        if (hasLocationAccess) {
            // Sort by distance
            if (sortByDistanceAscending) {
                Collections.sort(visibleLocations,
                        (a, b) -> Double.compare(a.distanceMeters, b.distanceMeters));
            } else {
                Collections.sort(visibleLocations,
                        (a, b) -> Double.compare(b.distanceMeters, a.distanceMeters));
            }
        } else {
            // Sort alphabetically if no location
            Collections.sort(visibleLocations,
                    (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        }

        adapter.notifyDataSetChanged();
    }
}
