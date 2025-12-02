package com.example.cosc341_recycling_sorting_project.ui.depots;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {
    public static List<RecycleLocation> getLocations() {
        List<RecycleLocation> list = new ArrayList<>();
        list.add(new RecycleLocation(
                "Columbia Bottle Depot – Dease Road",
                "680 Dease Road, Kelowna, BC",
                "Full-service bottle depot.",
                "Return-it beverage containers, bottles & cans.",
                49.89860863384442,
                -119.41016451303615
        ));
        list.add(new RecycleLocation(
                "Columbia Bottle Depot – Kent Road",
                "1936 Kent Road, Kelowna, BC",
                "Full-service bottle depot.",
                "Return-it beverage containers, bottles & cans.",
                49.879887938191004,
                -119.45282136139339
        ));
        list.add(new RecycleLocation(
                "Columbia Bottle Depot – St. Paul Street",
                "1314 St. Paul Street, Kelowna, BC",
                "Full-service bottle depot.",
                "Return-it beverage containers, bottles & cans.",
                49.89268532312454,
                -119.49242924460779
        ));
        list.add(new RecycleLocation(
                "Kelowna Recycling (Battery Doctors)",
                "1972 Windsor Road, Kelowna, BC",
                "Authorized collection site for complex recycling.",
                "Electronics, small appliances, paint, batteries, light bulbs, general metal recycling and hazardous-waste materials.",
                49.88096803546664,
                -119.45096121148765
        ));
        list.add(new RecycleLocation(
                "Return-It Express & Go – Kelowna",
                "2720 John Hindle Dr, Kelowna, BC",
                "Automated express drop-off kiosk.",
                "Beverage containers (cans, bottles) — No over-the-counter cash refunds.",
                49.94600455767401,
                -119.41563876242473
        ));
        return list;
    }
}

