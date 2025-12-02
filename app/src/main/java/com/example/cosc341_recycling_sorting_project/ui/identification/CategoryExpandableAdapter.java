package com.example.cosc341_recycling_sorting_project.ui.identification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cosc341_recycling_sorting_project.R;

import java.util.List;
import java.util.Map;

public class CategoryExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Category> categories;
    private Map<Category, List<Recyclable>> items;

    public CategoryExpandableAdapter(Context context,
                                     List<Category> categories,
                                     Map<Category, List<Recyclable>> items) {
        this.context = context;
        this.categories = categories;
        this.items = items;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Category cat = categories.get(groupPosition);
        List<Recyclable> recyclables = items.get(cat);
        return recyclables == null ? 0 : recyclables.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Category category = categories.get(groupPosition);
        List<Recyclable> recyclables = items.get(category);
        if (recyclables == null || childPosition >= recyclables.size()) {
            return null;
        }
        return recyclables.get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_category_group, parent, false);
        }

        TextView title = convertView.findViewById(R.id.textCategoryName);
        title.setText(categories.get(groupPosition).toString());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_recyclable_child, parent, false);
        }

        Recyclable r = (Recyclable) getChild(groupPosition, childPosition);

        ImageView img = convertView.findViewById(R.id.imageRecyclable);
        TextView name = convertView.findViewById(R.id.textRecyclableName);

        img.setImageResource(r.getImageResId());
        name.setText(r.getName());

        return convertView;
    }

    // required stubs:
    @Override public boolean hasStableIds() { return false; }
    @Override public boolean isChildSelectable(int g, int c) { return true; }
    @Override public long getGroupId(int g) { return g; }
    @Override public long getChildId(int g, int c) { return c; }
}

