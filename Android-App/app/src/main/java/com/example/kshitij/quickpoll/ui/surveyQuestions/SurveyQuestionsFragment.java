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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.data.model.Question;
import com.example.kshitij.quickpoll.data.model.Survey;
import com.example.kshitij.quickpoll.enums.SurveyVariables;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyQuestionsFragment extends Fragment {

    private static int i = 0, questionLength;
    Map<String, Object> answer = new HashMap<>();
//    private static boolean isAnswered;
    private Map<String, View> activeMap;
    private static List<?> list;
    private TextView questionText;
    private RadioGroup radioGroup;
    private static String active = "Active";
    private EditText inputTextBox;
    List<String> options;

    private TextView textBox;

    private Button nextButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.survey_questions_fragment, container, false);
        questionText = view.findViewById(R.id.question_text);
        nextButton = view.findViewById(R.id.button_next);
        Bundle bundle = getArguments();
        activeMap = new HashMap<>();

        final Survey survey = (Survey) bundle.get("survey");
//        isAnswered = false;
        list = survey.getQuestions();
        radioGroup = view.findViewById(R.id.survey_questions_radioGroup);
        inputTextBox = view.findViewById(R.id.survey_questions_textBoxInput);
        if(i<list.size()){

            Map<String, ?> hashMap = (Map<String, Object>)list.get(i);
            questionText.setText((String) hashMap.get(String.valueOf(SurveyVariables.name)));
            setInputOption(hashMap);

        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View activeView = activeMap.get(active);
                if (activeView == questionText){
                    if (questionText.getText().toString().isEmpty()){
                        inputTextBox.setError("Enter Answer");
                    }else{
                        changeQuestion();
                    }
                }else if (activeView == radioGroup){
                    if (radioGroup.getCheckedRadioButtonId() == -1){
                        Snackbar.make(getView(), "Please Select an Option", Snackbar.LENGTH_LONG).isShown();
                    }else{
                        changeQuestion();
                    }
                }
//                changeQuestion();
            }
        });
        return view;
    }

    public void changeQuestion(){
        if(i<list.size()){
            Map<String, ?> hashMap = (Map<String, Object>)list.get(++i);
            questionText.setText((String) hashMap.get(String.valueOf(SurveyVariables.name)));
            setInputOption(hashMap);
//            questionText.setText(list.get(++i).getQuestionText());
        }
        if(i == list.size()-1)
            nextButton.setVisibility(View.INVISIBLE);
    }

    public void setInputOption(Map<String, ?> hashMap){
        String questionType = (String) hashMap.get(String.valueOf(SurveyVariables.type));
        switch (questionType){
            case "Text":
                activeMap.put(active, inputTextBox);

                radioGroup.setVisibility(View.INVISIBLE);
                radioGroup.removeAllViews();
                inputTextBox.setText("");
                inputTextBox.setVisibility(View.VISIBLE);
                break;
            case "Single":
                inputTextBox.setVisibility(View.INVISIBLE);
                radioGroup.removeAllViews();
                radioGroup.setVisibility(View.VISIBLE);
                options = (List<String>) hashMap.get(String.valueOf(SurveyVariables.options));
                activeMap.put(active, radioGroup);
                for(int k = 0;k<options.size();k++) {
                    RadioButton button = new RadioButton(getContext());
                    button.setId(k);
                    button.setText(options.get(k));
                    radioGroup.addView(button);
                }
                break;
            case "Multiple":

                break;
        }
    }
}
