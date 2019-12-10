package com.example.kshitij.quickpoll.ui.surveyCycle;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.data.model.Survey;

public class SurveyDescriptionFragment extends Fragment {
    TextView descriptionText, timeLimitText;
    String description, time;
    Button takeSurvey;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.survey_description_fragment, container, false);
        final Bundle bundle = getArguments();
        final Survey survey = (Survey) bundle.get("survey");
        descriptionText = view.findViewById(R.id.description_editText);
        takeSurvey = view.findViewById(R.id.take_survey_button);
        timeLimitText = view.findViewById(R.id.time_to_complete_editText);
        descriptionText.setText(survey.surveyDescription);
        timeLimitText.setText(survey.timeToComplete);
        takeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getParentFragment().getView()).navigate(R.id.surveyQuestionsFragment, bundle);
            }
        });
        return view;
    }



}
