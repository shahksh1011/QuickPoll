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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.data.model.Question;
import com.example.kshitij.quickpoll.data.model.Survey;
import com.example.kshitij.quickpoll.enums.SurveyVariables;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyQuestionsFragment extends Fragment {

    private static int i = 0, questionLength;
    Map<String, Object> answer = new HashMap<>();
    FirebaseFirestore db;
//    private static boolean isAnswered;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private Map<String, View> activeMap;
    private static List<?> list;
    private TextView questionText;
    private RadioGroup radioGroup;
    private static String active = "Active";
    private EditText inputTextBox;
    List<String> options;
    LinearLayout checkBoxContainer;

    private TextView textBox;
    private List<Integer> checkBoxAnswer;

    private Button nextButton, submitButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.survey_questions_fragment, container, false);
        questionText = view.findViewById(R.id.question_text);
        nextButton = view.findViewById(R.id.button_next);
        submitButton = view.findViewById(R.id.button_submit);
        mAuth = FirebaseAuth.getInstance();
        checkBoxContainer = view.findViewById(R.id.checkbox_answer_layout);
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        answer = new HashMap<>();
        checkBoxAnswer = new ArrayList<>();
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
                checkAnswer(true);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
//                Log.d("Answer Size", String.valueOf(answer.size()));
                setAnswers(survey);
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
        if(i == list.size()-1){
            nextButton.setVisibility(View.INVISIBLE);
            nextButton.setClickable(false);
            submitButton.setClickable(true);
            submitButton.setVisibility(View.VISIBLE);
        }


    }

    public void checkAnswer(boolean flag){
        View activeView = activeMap.get(active);
        if (activeView == inputTextBox){
            if (inputTextBox.getText().toString().isEmpty()){
//                        Toast.makeText(getContext(),"Please enter a valid answer", Toast.LENGTH_LONG);
                inputTextBox.setError("Please Enter a valid answer");
            }else{
                answer.put(questionText.getText().toString(), inputTextBox.getText().toString());
                if (flag)
                    changeQuestion();
            }
        }else if (activeView == radioGroup){
            if (radioGroup.getCheckedRadioButtonId() == -1){
                Toast.makeText(getContext(), "Please Select an Option", Toast.LENGTH_LONG).show();

            }else{
                answer.put(questionText.getText().toString(), radioGroup.getCheckedRadioButtonId());
                if (flag)
                    changeQuestion();
            }
        }else if (activeView == checkBoxContainer){
            StringBuilder s = new StringBuilder();
            String prefix = "";
            for(int i: checkBoxAnswer) {
                s.append(prefix);
                prefix = ", ";
                s.append(String.valueOf(i));
            }
            answer.put(questionText.getText().toString(), s.toString());
            if (flag)
                changeQuestion();

        }
    }

    public void setInputOption(Map<String, ?> hashMap){
        String questionType = (String) hashMap.get(String.valueOf(SurveyVariables.type));
        switch (questionType){
            case "Text":
                activeMap.put(active, inputTextBox);
                checkBoxContainer.removeAllViews();
                checkBoxContainer.setVisibility(View.INVISIBLE);
                checkBoxAnswer = new ArrayList<>();
                radioGroup.setVisibility(View.INVISIBLE);
                radioGroup.removeAllViews();
                inputTextBox.setText("");
                inputTextBox.setVisibility(View.VISIBLE);
                break;
            case "Single":
                inputTextBox.setVisibility(View.INVISIBLE);
                checkBoxContainer.removeAllViews();
                checkBoxContainer.setVisibility(View.INVISIBLE);
                checkBoxAnswer = new ArrayList<>();
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
                inputTextBox.setVisibility(View.INVISIBLE);
                radioGroup.removeAllViews();
                radioGroup.setVisibility(View.INVISIBLE);
                checkBoxContainer.setVisibility(View.VISIBLE);
                checkBoxAnswer = new ArrayList<>();
                options = (List<String>) hashMap.get(String.valueOf(SurveyVariables.options));
                activeMap.put(active, checkBoxContainer);
                for(int k = 0;k<options.size();k++) {
                    CheckBox checkBox = new CheckBox(getContext());
                    checkBox.setId(k);
                    checkBox.setText(options.get(k));
                    checkBoxContainer.addView(checkBox);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (compoundButton.isChecked()){
                                checkBoxAnswer.add(compoundButton.getId());
                            }else{
                                checkBoxAnswer.remove(compoundButton.getId());
                            }
                        }
                    });
                }
                break;
        }
    }

    public void setAnswers(Survey survey){
        Map<String, Map<String, Object> > userAnswer = new HashMap<>();
        userAnswer.put(currentUser.getUid(), answer);
        db.collection("surveyAnswers")
                .document(survey.getSurveyId())
                .set(userAnswer, SetOptions.merge());
    }
}
