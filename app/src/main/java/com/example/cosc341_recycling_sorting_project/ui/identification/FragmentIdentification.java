package com.example.cosc341_recycling_sorting_project.ui.identification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cosc341_recycling_sorting_project.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentIdentification extends Fragment {

    private Map<Category, List<Recyclable>> dataByCategory;
    private RecyclableGridAdapter gridAdapter;

    public FragmentIdentification() {
        // Required empty public constructor
    }

    private List<Recyclable> getAllRecyclables() {
        List<Recyclable> all = new ArrayList<>();
        for (List<Recyclable> list : dataByCategory.values()) {
            if (list != null) {
                all.addAll(list);
            }
        }
        return all;
    }

    private Map<Category, List<Recyclable>> buildData() {
        Map<Category, List<Recyclable>> map = new HashMap<>();

        try {
            InputStream is = getResources().openRawResource(R.raw.recyclables);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONArray array = new JSONArray(jsonBuilder.toString());

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String name = obj.getString("name");
                String desc = obj.getString("description");
                String imgName = obj.getString("imageResId");      // e.g. "plastic_bag"
                String catStr = obj.getString("category");    // e.g. "PLASTIC"

                int resId = getResources().getIdentifier(
                        imgName,          // "plastic_bag"
                        "drawable",
                        requireContext().getPackageName()
                );

                Category cat = Category.valueOf(catStr);

                Recyclable rec = new Recyclable(name, desc, resId, cat);

                if (!map.containsKey(cat)) {
                    map.put(cat, new ArrayList<>());
                }
                map.get(cat).add(rec);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_identification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = view.findViewById(R.id.spinnerCategory);
        RecyclerView recycler = view.findViewById(R.id.recyclerRecyclables);
        TextInputEditText search = view.findViewById(R.id.editTextSearch);

        dataByCategory = buildData();

        List<String> categories = new ArrayList<>();
        categories.add("All");  // index 0
        for (Category c : Category.values()) {
            categories.add(c.name());
        }

        // Spinner for categories
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spinner.setAdapter(spinnerAdapter);

        // RecyclerView grid
        gridAdapter = new RecyclableGridAdapter(requireContext());
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recycler.setAdapter(gridAdapter);

        // initial category
        spinner.setSelection(0); // "All"
        updateGridForCategory(null, null);

        // change category
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                String query = search.getText() != null ? search.getText().toString() : "";

                if (position == 0) {
                    // "All"
                    updateGridForCategory(null, query);
                } else {
                    // position 1 -> first enum, etc.
                    Category selected = Category.values()[position - 1];
                    updateGridForCategory(selected, query);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        // search filter
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                int position = spinner.getSelectedItemPosition();

                Category category = null;
                if (position > 0) {
                    category = Category.values()[position - 1];
                }

                updateGridForCategory(category, s.toString());
            }
        });
    }

    private void updateGridForCategory(@Nullable Category category, @Nullable String query) {
        List<Recyclable> base;

        if (category == null) {
            // "All"
            base = getAllRecyclables();
        } else {
            base = dataByCategory.get(category);
            if (base == null) base = new ArrayList<>();
        }

        query = (query == null) ? "" : query.trim().toLowerCase();

        List<Recyclable> filtered = new ArrayList<>();
        if (query.isEmpty()) {
            filtered.addAll(base);
        } else {
            for (Recyclable r : base) {
                if (r.getName().toLowerCase().contains(query)) {
                    filtered.add(r);
                }
            }
        }

        gridAdapter.submitList(filtered);
    }


}