package com.example.kshitij.quickpoll.ui.survey;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kshitij.quickpoll.R;

public class SurveyFragment extends Fragment {

    private SurveyViewModel surveyViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        surveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        View root = inflater.inflate(R.layout.survey_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_survey);
        surveyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
