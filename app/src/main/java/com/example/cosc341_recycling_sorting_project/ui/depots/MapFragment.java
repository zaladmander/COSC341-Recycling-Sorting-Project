package com.example.cosc341_recycling_sorting_project.ui.depots;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cosc341_recycling_sorting_project.R;
import com.example.cosc341_recycling_sorting_project.ui.depots.LocationRepository;
import com.example.cosc341_recycling_sorting_project.ui.depots.RecycleLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQ_LOCATION = 1001;

    private LinearLayout layoutMarkerInfo;
    private TextView tvInfoName, tvInfoAddress, tvInfoDesc;
    private Button btnDirections, btnShare, btnOpenList;
    private EditText etSearchMap;

    private List<RecycleLocation> locations;

    public MapFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Bind UI
        layoutMarkerInfo = view.findViewById(R.id.layoutMarkerInfo);
        tvInfoName = view.findViewById(R.id.tvInfoName);
        tvInfoAddress = view.findViewById(R.id.tvInfoAddress);
        tvInfoDesc = view.findViewById(R.id.tvInfoDesc);
        btnDirections = view.findViewById(R.id.btnDirections);
        btnShare = view.findViewById(R.id.btnShare);
        btnOpenList = view.findViewById(R.id.btnOpenList);
        etSearchMap = view.findViewById(R.id.etSearchMap);

        setupSearchListener();
        setupListButton();

        // Load Google Maps fragment
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    private void setupListButton() {
        btnOpenList.setOnClickListener(v ->
                NavHostFragment.findNavController(MapFragment.this)
                        .navigate(R.id.navigation_list)
        );
    }

    private void setupSearchListener() {
        etSearchMap.setOnEditorActionListener((v, actionId, event) -> {

            boolean enterPressed =
                    event != null &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                            event.getAction() == KeyEvent.ACTION_DOWN;

            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    enterPressed) {

                searchLocation(v.getText().toString());
                return true;
            }

            return false;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Move zoom controls up by 70dp
        int bottomPadding = (int) (70 * getResources().getDisplayMetrics().density);
        mMap.setPadding(0, 0, 0, bottomPadding);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable My Location if permission already granted
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        }

        // Load recycling locations
        locations = LocationRepository.getLocations();

        for (RecycleLocation loc : locations) {
            LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(loc.getName())
                    .snippet(loc.getAddress()));
            if (marker != null) marker.setTag(loc);
        }

        // Center camera
        if (!locations.isEmpty()) {
            RecycleLocation first = locations.get(0);
            LatLng pos = new LatLng(first.getLatitude(), first.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13f));
        }

        // Marker click
        mMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof RecycleLocation) {
                showMarkerInfo((RecycleLocation) tag);
            }
            return true;
        });

        // Hide marker info on map click
        mMap.setOnMapClickListener(latLng -> hideMarkerInfo());
    }

    private void searchLocation(String query) {
        if (mMap == null || locations == null) return;

        String lower = query.toLowerCase();

        for (RecycleLocation loc : locations) {
            if (loc.getName().toLowerCase().contains(lower)
                    || loc.getAddress().toLowerCase().contains(lower)
                    || loc.getDescription().toLowerCase().contains(lower)) {

                LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f));
                showMarkerInfo(loc);
                return;
            }
        }

        Toast.makeText(requireContext(),
                "No location found for: " + query,
                Toast.LENGTH_SHORT).show();
    }

    private void showMarkerInfo(RecycleLocation loc) {
        tvInfoName.setText(loc.getName());
        tvInfoAddress.setText(loc.getAddress());
        tvInfoDesc.setText(loc.getDescription());

        btnDirections.setOnClickListener(v -> launchDirections(loc));
        btnShare.setOnClickListener(v -> shareLocation(loc));

        layoutMarkerInfo.setVisibility(View.VISIBLE);
    }

    private void hideMarkerInfo() {
        layoutMarkerInfo.setVisibility(View.GONE);
    }

    private void launchDirections(RecycleLocation loc) {
        String uri = String.format(
                Locale.US,
                "google.navigation:q=%f,%f",
                loc.getLatitude(), loc.getLongitude()
        );

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        startActivity(intent);
    }

    private void shareLocation(RecycleLocation loc) {
        String mapsLink = String.format(
                Locale.US,
                "http://maps.google.com/maps?q=%f,%f",
                loc.getLatitude(), loc.getLongitude()
        );

        String text = "Recycling Location:\n" +
                loc.getName() + "\n" +
                loc.getAddress() + "\n" +
                mapsLink;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(Intent.createChooser(intent, "Share via"));
    }
}
