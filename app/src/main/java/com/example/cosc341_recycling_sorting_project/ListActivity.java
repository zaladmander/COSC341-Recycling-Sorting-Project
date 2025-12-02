package com.example.cosc341_recycling_sorting_project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cosc341_recycling_sorting_project.ui.depots.LocationAdapter;
import com.example.cosc341_recycling_sorting_project.ui.depots.LocationRepository;
import com.example.cosc341_recycling_sorting_project.ui.depots.RecycleLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recycler = findViewById(R.id.recyclerLocations);
        btnBack = findViewById(R.id.btnBackToMap);
        etSearch = findViewById(R.id.etSearch);
        btnSort = findViewById(R.id.btnSortDistance);
        tvRangeValue = findViewById(R.id.tvRangeValue);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        allLocations = new ArrayList<>(LocationRepository.getLocations());
        fetchLastKnownLocation();

        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(ListActivity.this, MapActivity.class);
            startActivity(i);
            finish();
        });
        setupSearch();
        setupSortListener();
    }

    private void fetchLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOCATION);
            return;
        }

        Task<Location> locationTask = fusedLocationClient.getLastLocation();

        locationTask.addOnSuccessListener(this, location -> {
            if (location != null) {
                userLat = location.getLatitude();
                userLon = location.getLongitude();
            } else {
                Toast.makeText(this, "Could not get current location. Sorting by default coordinates.", Toast.LENGTH_LONG).show();
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
                fetchLastKnownLocation();
            } else {
                Toast.makeText(this, "Location permission denied. Sorting by default coordinates.", Toast.LENGTH_LONG).show();
                continueListSetup();
            }
        }
    }

    private void continueListSetup() {
        calculateAllDistances();
        visibleLocations = new ArrayList<>(allLocations);
        adapter = new LocationAdapter(visibleLocations);
        recycler.setAdapter(adapter);
        btnSort.setImageResource(R.drawable.ic_sort_ascending);
        tvRangeValue.setText(String.format(Locale.ROOT, "%.1f km", currentMaxRangeKm));
        filterLocations(etSearch.getText().toString());
    }

    private void calculateAllDistances() {
        for (RecycleLocation loc : allLocations) {
            float[] results = new float[1];
            Location.distanceBetween(userLat, userLon, loc.getLatitude(), loc.getLongitude(), results);
            loc.distanceMeters = results[0];
        }
    }
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLocations(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupSortListener() {
        btnSort.setOnClickListener(v -> {
            sortByDistanceAscending = !sortByDistanceAscending;
            int drawableRes = sortByDistanceAscending ?
                    R.drawable.ic_sort_ascending : R.drawable.ic_sort_descending;
            btnSort.setImageResource(drawableRes);
            filterLocations(etSearch.getText().toString());
        });
    }

    private void filterLocations(String query) {
        String lower = query.toLowerCase(Locale.ROOT);
        double maxRangeMeters = currentMaxRangeKm * 1000;
        visibleLocations.clear();
        for (RecycleLocation loc : allLocations) {
            boolean matchesSearch = lower.isEmpty() ||
                    loc.getName().toLowerCase(Locale.ROOT).contains(lower) ||
                    loc.getAddress().toLowerCase(Locale.ROOT).contains(lower) ||
                    loc.getDescription().toLowerCase(Locale.ROOT).contains(lower);
            boolean matchesRange = loc.distanceMeters <= maxRangeMeters;
            if (matchesSearch && matchesRange) {
                visibleLocations.add(loc);
            }
        }
        sortLocations();
    }

    private void sortLocations() {
        if (sortByDistanceAscending) {
            Collections.sort(visibleLocations, (loc1, loc2) ->
                    Double.compare(loc1.distanceMeters, loc2.distanceMeters)
            );
        } else {
            Collections.sort(visibleLocations, (loc1, loc2) ->
                    Double.compare(loc2.distanceMeters, loc1.distanceMeters)
            );
        }
        adapter.notifyDataSetChanged();
    }
}