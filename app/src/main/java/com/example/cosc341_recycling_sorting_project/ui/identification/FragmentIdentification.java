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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.example.cosc341_recycling_sorting_project.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentIdentification extends Fragment {

    private Map<Category, List<Recyclable>> dataByCategory;
    private RecyclableGridAdapter gridAdapter;
    private List<Recyclable> currentList = new ArrayList<>();

    public FragmentIdentification() {
        // Required empty public constructor
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

                // HERE. RIGHT HERE.
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

        // buildData: your existing JSON → Map<Category, List<Recyclable>>
        dataByCategory = buildData();

        // Spinner for categories
        final List<Category> categories = Arrays.asList(Category.values());
        ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(
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
        if (!categories.isEmpty()) {
            Category initial = categories.get(0);
            updateGridForCategory(initial, null);
        }

        // change category
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                Category selected = categories.get(position);
                String query = search.getText() != null ? search.getText().toString() : "";
                updateGridForCategory(selected, query);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        // search filter
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                Category selected = (Category) spinner.getSelectedItem();
                String query = s.toString();
                updateGridForCategory(selected, query);
            }
        });
    }

    private void updateGridForCategory(Category category, @Nullable String query) {
        List<Recyclable> list = dataByCategory.get(category);
        if (list == null) list = new ArrayList<>();

        query = (query == null) ? "" : query.trim().toLowerCase();

        if (query.isEmpty()) {
            currentList = new ArrayList<>(list);
        } else {
            List<Recyclable> filtered = new ArrayList<>();
            for (Recyclable r : list) {
                if (r.getName().toLowerCase().contains(query)) {
                    filtered.add(r);
                }
            }
            currentList = filtered;
        }

        gridAdapter.submitList(currentList);
    }

}