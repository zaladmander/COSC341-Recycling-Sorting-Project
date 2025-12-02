package com.example.cosc341_recycling_sorting_project.ui.quiz;

import java.util.List;

public class Question {
    public final String text;
    public final List<String> options; // size = 4
    public final int correctIndex;     // 0..3
    public final String referenceUrl;  // source link for this question

    public Question(String text, List<String> options, int correctIndex, String referenceUrl) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.referenceUrl = referenceUrl;
    }
}
