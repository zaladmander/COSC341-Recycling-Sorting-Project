package com.example.cosc341_recycling_sorting_project.ui.identification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.example.cosc341_recycling_sorting_project.R;
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

        ExpandableListView accordion = view.findViewById(R.id.expandableCategories);

        List<Category> groups = Arrays.asList(Category.values());
        Map<Category, List<Recyclable>> data = buildData();

        CategoryExpandableAdapter adapter =
                new CategoryExpandableAdapter(requireContext(), groups, data);

        accordion.setAdapter(adapter);
    }

}