package com.example.cosc341_recycling_sorting_project.ui.stats;

public class TopRecycler {
    private final String name;
    private final int rank;
    private final int points; // or kgs

    public TopRecycler(String name, int rank, int points) {
        this.name = name;
        this.rank = rank;
        this.points = points;
    }

    public String getName() { return name; }
    public int getRank() { return rank; }
    public int getPoints() { return points; }
}
