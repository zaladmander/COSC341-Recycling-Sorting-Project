package com.example.cosc341_recycling_sorting_project.ui.quiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.net.Uri;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cosc341_recycling_sorting_project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentQuiz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentQuiz extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentQuiz() {
        // Required empty public constructor
    }

    public static FragmentQuiz newInstance(String param1, String param2) {
        FragmentQuiz fragment = new FragmentQuiz();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // --------------------------------------------------------------------
    // QUIZ UI + LOGIC
    // --------------------------------------------------------------------
    private LinearLayout layoutStart;
    private ScrollView layoutQuiz;
    private Spinner spinnerQuestionCount;
    private Button btnStartQuiz, btnSubmit, btnNext;
    private TextView tvQuestionHeader, tvQuestionText, tvFeedback;
    private RadioGroup rgOptions;
    private RadioButton rb1, rb2, rb3, rb4;

    private List<Question> questions = new ArrayList<>();
    private int index = 0;
    private int score = 0;
    private int total = 0;

    // track if current question has already been submitted
    private boolean hasSubmittedCurrent = false;

    private ScrollView layoutResult;
    private TextView tvScoreResult;
    private Button btnReattempt;
    private TextView link1, link2, link3, link4, link5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Start layout
        layoutStart          = view.findViewById(R.id.layoutStart);
        layoutQuiz           = view.findViewById(R.id.layoutQuiz);
        spinnerQuestionCount = view.findViewById(R.id.spinnerQuestionCount);
        btnStartQuiz         = view.findViewById(R.id.btnStartQuiz);

        // Quiz layout
        tvQuestionHeader     = view.findViewById(R.id.tvQuestionHeader);
        tvQuestionText       = view.findViewById(R.id.tvQuestionText);
        rgOptions            = view.findViewById(R.id.rgOptions);
        rb1                  = view.findViewById(R.id.rbOption1);
        rb2                  = view.findViewById(R.id.rbOption2);
        rb3                  = view.findViewById(R.id.rbOption3);
        rb4                  = view.findViewById(R.id.rbOption4);
        btnSubmit            = view.findViewById(R.id.btnSubmit);
        btnNext              = view.findViewById(R.id.btnNext);
        tvFeedback           = view.findViewById(R.id.tvFeedback);

        setupStartScreen();
        setupListeners();

        layoutResult   = view.findViewById(R.id.layoutResult);
        tvScoreResult  = view.findViewById(R.id.tvScore);
        btnReattempt   = view.findViewById(R.id.btnReattempt);

        link1 = view.findViewById(R.id.link1);
        link2 = view.findViewById(R.id.link2);
        link3 = view.findViewById(R.id.link3);
        link4 = view.findViewById(R.id.link4);
        link5 = view.findViewById(R.id.link5);

        link1.setText("RDCO Recycling Overview");
        link1.setOnClickListener(v -> openUrl("https://www.rdco.com/en/living-here/recycling.aspx"));

        link2.setText("What Can Be Recycled (PDF)");
        link2.setOnClickListener(v -> openUrl("https://www.rdco.com/en/living-here/resources/Documents/What-Can-Be-Recycled-List.pdf"));

        link3.setText("Yard Waste Guide");
        link3.setOnClickListener(v -> openUrl("https://www.rdco.com/en/living-here/yard-waste.aspx"));

        link4.setText("Curbside Carts Guide");
        link4.setOnClickListener(v -> openUrl("https://www.rdco.com/en/living-here/curbside-carts.aspx"));

        link5.setText("Waste & Recycling Services");
        link5.setOnClickListener(v -> openUrl("https://www.rdco.com/en/living-here/waste-and-recycling.aspx"));

        return view;
    }

    // ---------------------- HELPER METHODS -----------------------------
    private void openUrl(String url) {
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browser);
    }

    private void setupStartScreen() {
        // Spinner options: 1 to 10
        String[] counts = {"1","2","3","4","5","6","7","8","9","10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                counts
        );
        spinnerQuestionCount.setAdapter(adapter);

        layoutStart.setVisibility(View.VISIBLE);
        layoutQuiz.setVisibility(View.GONE);
    }

    private void setupListeners() {
        // Start Quiz button
        btnStartQuiz.setOnClickListener(v -> startQuiz());

        // Enable Submit when an option is selected (only before submit)
        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (!hasSubmittedCurrent) {

                if (checkedId != -1) {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setBackgroundTintList(
                            getResources().getColorStateList(R.color.purple_500)
                    );
                    btnSubmit.setTextColor(Color.WHITE);

                } else {
                    btnSubmit.setEnabled(false);
                    btnSubmit.setBackgroundTintList(
                            getResources().getColorStateList(android.R.color.darker_gray)
                    );
                    btnSubmit.setTextColor(Color.BLACK);
                }
            }
        });

        // Submit answer
        btnSubmit.setOnClickListener(v -> handleSubmit());

        // Next question / Finish
        btnNext.setOnClickListener(v -> handleNext());
    }

    private void startQuiz() {
        int requested = Integer.parseInt(
                spinnerQuestionCount.getSelectedItem().toString()
        );

        // Load questions from string resources instead of hard-coded map
        List<Question> pool = QuestionBank.loadFromXml(requireContext());

        if (pool.isEmpty()) {
            Toast.makeText(getContext(),
                    "No questions available.", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(pool);
        questions = new ArrayList<>(pool.subList(0, Math.min(requested, pool.size())));

        index = 0;
        score = 0;
        total = questions.size();

        layoutStart.setVisibility(View.GONE);
        layoutQuiz.setVisibility(View.VISIBLE);

        bindQuestion();
    }

    private void bindQuestion() {
        if (questions.isEmpty() || index < 0 || index >= questions.size()) return;

        hasSubmittedCurrent = false;

        Question q = questions.get(index);
        tvQuestionHeader.setText("Question " + (index + 1) + " of " + total);
        tvQuestionText.setText(q.text);

        rb1.setText(q.options.get(0));
        rb2.setText(q.options.get(1));
        rb3.setText(q.options.get(2));
        rb4.setText(q.options.get(3));

        // reset selection / states
        rgOptions.clearCheck();
        rb1.setEnabled(true);
        rb2.setEnabled(true);
        rb3.setEnabled(true);
        rb4.setEnabled(true);

        // reset text colors in case previous question highlighted one in blue
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);

        btnSubmit.setEnabled(false);           // greyed out by default
        tvFeedback.setVisibility(View.GONE);   // hide message
        tvFeedback.setText("");
        btnNext.setVisibility(View.GONE);      // show only after submit
    }

    private void handleSubmit() {
        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId == -1 || hasSubmittedCurrent) {
            return;
        }

        int chosen =
                (checkedId == R.id.rbOption1) ? 0 :
                        (checkedId == R.id.rbOption2) ? 1 :
                                (checkedId == R.id.rbOption3) ? 2 : 3;

        Question q = questions.get(index);
        boolean correct = (chosen == q.correctIndex);

        hasSubmittedCurrent = true;

        // Figure out which RadioButton is the correct one
        RadioButton correctRb =
                (q.correctIndex == 0) ? rb1 :
                        (q.correctIndex == 1) ? rb2 :
                                (q.correctIndex == 2) ? rb3 : rb4;

        if (correct) {
            // Correct answer case
            score++;
            tvFeedback.setText("Good job! Correct answer. 👏");
            tvFeedback.setTextColor(Color.parseColor("#2E7D32")); // green

            // Also highlight correct answer in blue when right
            correctRb.setTextColor(Color.parseColor("#1A73E8"));
        } else {
            // Wrong answer case: show correct answer + link
            String correctText = q.options.get(q.correctIndex);
            String referenceUrl = q.referenceUrl;

            String html = "Oops! Wrong answer.<br>" +
                    "The correct answer is: <b><font color='#1A73E8'>" +
                    correctText +
                    "</font></b><br><br>" +
                    "<a href='" + referenceUrl + "'>Learn more here</a>";

            tvFeedback.setText(Html.fromHtml(html));
            tvFeedback.setMovementMethod(LinkMovementMethod.getInstance());
            tvFeedback.setTextColor(Color.parseColor("#C62828")); // red

            // Highlight the correct option in blue
            correctRb.setTextColor(Color.parseColor("#1A73E8"));
        }

        tvFeedback.setVisibility(View.VISIBLE);

        // lock the options so user can't change after submit
        rb1.setEnabled(false);
        rb2.setEnabled(false);
        rb3.setEnabled(false);
        rb4.setEnabled(false);

        // disable submit
        btnSubmit.setEnabled(false);
        btnSubmit.setBackgroundTintList(
                getResources().getColorStateList(android.R.color.darker_gray)
        );
        btnSubmit.setTextColor(Color.BLACK);

        // show Next/Finish button
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setText(index == total - 1 ? "Finish Quiz" : "Next Question");
    }

    private void handleNext() {
        if (!hasSubmittedCurrent) {
            // safety: ignore if user somehow hits Next before submitting
            return;
        }

        index++;
        if (index >= total) {
            showResult();
        } else {
            bindQuestion();
        }
    }

    private void showResult() {

        layoutQuiz.setVisibility(View.GONE);
        layoutStart.setVisibility(View.GONE);
        layoutResult.setVisibility(View.VISIBLE);

        tvScoreResult.setText("Your Score : " + score + "/" + total);

        if (score == total) {
            tvScoreResult.setTextColor(Color.parseColor("#1B8E24"));  // green
        } else {
            tvScoreResult.setTextColor(Color.parseColor("#C62828"));  // red
        }

        btnReattempt.setOnClickListener(v -> {
            layoutResult.setVisibility(View.GONE);
            layoutStart.setVisibility(View.VISIBLE);
        });
    }
}
