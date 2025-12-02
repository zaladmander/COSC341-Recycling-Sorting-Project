package com.example.cosc341_recycling_sorting_project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQ_LOCATION = 1001;

    private LinearLayout layoutMarkerInfo;
    private TextView tvInfoName, tvInfoAddress, tvInfoDesc;
    private Button btnDirections;
    private Button btnShare;
    private EditText etSearchMap;

    private List<RecycleLocation> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btnOpenList = findViewById(R.id.btnOpenList);
        btnOpenList.setOnClickListener(v ->
                startActivity(new Intent(MapActivity.this, ListActivity.class))
        );

        layoutMarkerInfo = findViewById(R.id.layoutMarkerInfo);
        tvInfoName = findViewById(R.id.tvInfoName);
        tvInfoAddress = findViewById(R.id.tvInfoAddress);
        tvInfoDesc = findViewById(R.id.tvInfoDesc);
        btnDirections = findViewById(R.id.btnDirections);
        btnShare = findViewById(R.id.btnShare);

        etSearchMap = findViewById(R.id.etSearchMap);
        setupSearchOnMap();

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_LOCATION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);


        locations = LocationRepository.getLocations();

        for (RecycleLocation loc : locations) {
            LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(loc.getName())
                    .snippet(loc.getAddress()));

            if (marker != null) {
                marker.setTag(loc);
            }
        }

        if (!locations.isEmpty()) {
            RecycleLocation first = locations.get(0);
            LatLng pos = new LatLng(first.getLatitude(), first.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13f));
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof RecycleLocation) {
                RecycleLocation loc = (RecycleLocation) tag;
                showMarkerInfo(loc);
            }
            return true;
        });


        mMap.setOnMapClickListener(latLng -> hideMarkerInfo());
    }

    private void showMarkerInfo(RecycleLocation loc) {
        tvInfoName.setText(loc.getName());
        tvInfoAddress.setText(loc.getAddress());
        tvInfoDesc.setText(loc.getDescription());

        btnDirections.setOnClickListener(v -> launchDirectionsIntent(loc));
        btnShare.setOnClickListener(v -> shareLocation(loc));

        layoutMarkerInfo.setVisibility(View.VISIBLE);
    }

    private void hideMarkerInfo() {
        layoutMarkerInfo.setVisibility(View.GONE);
    }

    private void launchDirectionsIntent(RecycleLocation loc) {
        String uri = String.format(Locale.US,
                "google.navigation:q=%f,%f",
                loc.getLatitude(),
                loc.getLongitude());

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Google Maps app not installed.", Toast.LENGTH_SHORT).show();
            String webUri = String.format(Locale.US,
                    "https://www.google.com/maps/dir/?api=1&destination=%f,%f&travelmode=driving",
                    loc.getLatitude(),
                    loc.getLongitude());
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
            startActivity(webIntent);
        }
    }

    private void shareLocation(RecycleLocation loc) {
        String mapsLink = String.format(Locale.US,
                "http://maps.google.com/maps?q=%f,%f",
                loc.getLatitude(),
                loc.getLongitude());

        String shareText = String.format(
                "Check out this recycling location: %s\nAddress: %s\nLink: %s",
                loc.getName(),
                loc.getAddress(),
                mapsLink
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Recycling Location: " + loc.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(shareIntent, "Share Location via..."));
    }


    private void setupSearchOnMap() {
        etSearchMap.setOnEditorActionListener((v, actionId, event) -> {
            boolean isEnterPressed =
                    event != null
                            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && event.getAction() == KeyEvent.ACTION_DOWN;

            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || isEnterPressed) {

                String query = v.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchAndFocusLocation(query);
                }
                return true;
            }
            return false;
        });
    }

    private void searchAndFocusLocation(String query) {
        if (mMap == null || locations == null || locations.isEmpty()) {
            Toast.makeText(this, "Map not ready yet", Toast.LENGTH_SHORT).show();
            return;
        }

        String lower = query.toLowerCase(Locale.ROOT);

        for (RecycleLocation loc : locations) {
            if (loc.getName().toLowerCase(Locale.ROOT).contains(lower)
                    || loc.getAddress().toLowerCase(Locale.ROOT).contains(lower)
                    || loc.getDescription().toLowerCase(Locale.ROOT).contains(lower)) {

                LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f));
                showMarkerInfo(loc);
                return;
            }
        }

        Toast.makeText(this, "No location found for: " + query, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_LOCATION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (mMap != null &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}