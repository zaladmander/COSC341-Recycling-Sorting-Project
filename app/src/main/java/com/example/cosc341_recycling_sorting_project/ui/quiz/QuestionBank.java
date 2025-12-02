package com.example.cosc341_recycling_sorting_project.ui.quiz;

import android.content.Context;
import android.content.res.Resources;

import com.example.cosc341_recycling_sorting_project.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionBank {

    /**
     * Builds the question list using string resources defined in res/values/strings.xml
     */
    public static List<Question> loadFromXml(Context context) {
        Resources res = context.getResources();

        String[] questionTexts = res.getStringArray(R.array.quiz_questions);
        String[] correctIndexStrings = res.getStringArray(R.array.quiz_correct_indexes);
        String[] referenceUrls = res.getStringArray(R.array.quiz_reference_links);

        List<Question> list = new ArrayList<>();

        for (int i = 0; i < questionTexts.length; i++) {

            int optionsArrayId = res.getIdentifier(
                    "quiz_q" + (i + 1) + "_options",
                    "array",
                    context.getPackageName()
            );

            if (optionsArrayId == 0) {
                // defensive: skip if options array is missing
                continue;
            }

            String[] options = res.getStringArray(optionsArrayId);
            int correctIndex = Integer.parseInt(correctIndexStrings[i]);
            String referenceUrl = referenceUrls[i];

            list.add(new Question(
                    questionTexts[i],
                    Arrays.asList(options),
                    correctIndex,
                    referenceUrl
            ));
        }

        return list;
    }
}
