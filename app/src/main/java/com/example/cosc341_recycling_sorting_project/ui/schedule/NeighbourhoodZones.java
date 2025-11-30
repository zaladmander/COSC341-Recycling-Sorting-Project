package com.example.cosc341_recycling_sorting_project.ui.schedule;

import java.util.HashMap;
import java.util.Map;

    public class NeighbourhoodZones {
        public static final Map<String, String> ZONES = new HashMap<>();

        static {
            ZONES.put("Ellison", "Monday A");
            ZONES.put("Rutland/East Glenmore", "Wednesday A");
            ZONES.put("Joe Rich", "Thursday A");
            ZONES.put("West Glenmore", "Monday B");
            ZONES.put("Downtown", "Monday B");
            ZONES.put("Mission", "Friday A");
        }

    }