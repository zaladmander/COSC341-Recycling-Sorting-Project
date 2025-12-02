package com.example.cosc341_recycling_sorting_project.ui.identification;

public class Recyclable {

    private final String name;
    private final String description;
    private final int imageResId;
    private final Category category;

    public Recyclable(String name, String description, int imageResId, Category category) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public Category getCategory() {
        return category;
    }
}

