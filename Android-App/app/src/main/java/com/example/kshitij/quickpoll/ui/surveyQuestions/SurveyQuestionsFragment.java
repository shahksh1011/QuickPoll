package com.example.kshitij.quickpoll.ui.surveyQuestions;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.data.model.Question;
import com.example.kshitij.quickpoll.data.model.Survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyQuestionsFragment extends Fragment {

    private static int i = 0, questionLength;
    private static Map<Integer, ?> hashMap;
    private static List<?> list;
    private TextView questionText;
    private RadioGroup radioGroup;
    private TextView textBox;
    private Button nextButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.survey_questions_fragment, container, false);
        hashMap = new HashMap<>();
        questionText = view.findViewById(R.id.question_text);
        nextButton = view.findViewById(R.id.button_next);
        Bundle bundle = getArguments();
        final Survey survey = (Survey) bundle.get("survey");
        list = survey.getQuestions();

        Log.d("WOO", String.valueOf(list.size()));
        for (Object question : list){
            Map<String, Object> map = new HashMap<>();
            map = (Map<String, Object>) question;
            Log.d("Instance of", question.toString() + " -- " + map.get("question"));
        }

//        if(i<list.size()){
//
//            questionText.setText(list.get(i).getQuestionText());
//        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeQuestion();
            }
        });
        return view;
    }

    public void changeQuestion(){
        if(i<list.size()){
            ++i;
//            questionText.setText(list.get(++i).getQuestionText());
        }
    }
}
