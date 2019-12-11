package com.example.kshitij.quickpoll.ui.poll;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.kshitij.quickpoll.data.model.Poll;
import com.example.kshitij.quickpoll.data.model.Question;
import com.example.kshitij.quickpoll.data.model.Survey;
import com.example.kshitij.quickpoll.enums.PollVariables;
import com.example.kshitij.quickpoll.enums.SurveyVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PollQuestionFragment extends Fragment {

    private static String TAG = "PollQuestionFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView questionText;
    private RadioGroup radioGroup;
    private List<String> options;
    private LinearLayout checkBoxContainer;
    private Button nextSubmitButton;
    private Poll pollObj;
    private static List<Question> list;
    private static int i = 0;
    private Map<String, View> activeMap = new HashMap<>();
    private static String active = "Active";
    private Map<String, Object> answer = new HashMap<>();
    private List<Integer> checkBoxAnswer = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll_question, container, false);
        questionText = view.findViewById(R.id.poll_quest_fragment_question_text);
        radioGroup = view.findViewById(R.id.poll_quest_fragment_survey_questions_radioGroup);
        checkBoxContainer = view.findViewById(R.id.poll_quest_fragment_checkbox_answer_layout);
        nextSubmitButton = view.findViewById(R.id.poll_quest_fragment_button_next);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        if(bundle==null){
            Navigation.findNavController(view).navigate(R.id.surveyDescriptionFragment);
        }
        String pollId = (String) bundle.get("poll_id");
        if(pollId==null){
            Navigation.findNavController(view).navigate(R.id.surveyDescriptionFragment);
        }
        db.collection("polls").document(pollId)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        //Log.d(TAG, document.getBoolean(String.valueOf(PollVariables.lockQuestion)).toString());
                        pollObj = new Poll(document.getId(),document.getString(String.valueOf(PollVariables.pollName)),
                                document.getString(String.valueOf(PollVariables.createdBy)),
                                document.getDate(String.valueOf(PollVariables.createdDate)),
                                document.getString(String.valueOf(PollVariables.description)),
                                document.getBoolean(String.valueOf(PollVariables.lockQuestion)).booleanValue(),
                                document.getLong(String.valueOf(PollVariables.displayQuestion)).longValue(),
                                (List<Question>) document.get(String.valueOf(PollVariables.questions)));
                        list = pollObj.getQuestionList();
                        if(i<list.size()) {
                            Map<String, ?> hashMap = (Map<String, Object>) list.get(i);
                            questionText.setText((String) hashMap.get(String.valueOf(SurveyVariables.name)));
                            setInputOption(hashMap);
                            if(i == list.size()-1){
                                nextSubmitButton.setText(R.string.submit_button);
                            }else{
                                nextSubmitButton.setText(R.string.next);
                            }
                        }
                    }else{
                        Log.w(TAG, "Error getting poll documents.", task.getException());
                    }
                });
        if(list!=null){
            if(i<list.size()) {
                Map<String, ?> hashMap = (Map<String, Object>) list.get(i);
                questionText.setText((String) hashMap.get(String.valueOf(SurveyVariables.name)));
                setInputOption(hashMap);
            }
        }
        nextSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i>=list.size()-1){
                    checkAnswer(false);
                    setAnswers(pollObj);
                }else {
                    checkAnswer(true);
                }
            }
        });
        return  view;
    }


    public void changeQuestion(){
        if(i<list.size()){
            Map<String, ?> hashMap = (Map<String, Object>)list.get(++i);
            questionText.setText((String) hashMap.get(String.valueOf(SurveyVariables.name)));
            setInputOption(hashMap);
        }
        if(i == list.size()-1){
            nextSubmitButton.setText(R.string.submit_button);
        }else{
            nextSubmitButton.setText(R.string.next);
        }


    }

    public void checkAnswer(boolean flag){
        View activeView = activeMap.get(active);
        if (activeView == radioGroup){
            if (radioGroup.getCheckedRadioButtonId() == -1){
                Toast.makeText(getContext(), "Please Select an Option", Toast.LENGTH_LONG).show();
            }else{
                answer.put(questionText.getText().toString(),options.get(radioGroup.getCheckedRadioButtonId()));
                if (flag)
                    changeQuestion();
            }
        }else if (activeView == checkBoxContainer){
            StringBuilder s = new StringBuilder();
            String prefix = "";
            for(int i: checkBoxAnswer) {
                s.append(prefix);
                prefix = ", ";
                s.append(options.get(i));
            }
            answer.put(questionText.getText().toString(), s.toString());
            if (flag)
                changeQuestion();

        }
    }

    public void setInputOption(Map<String, ?> hashMap){
        String questionType = (String) hashMap.get(String.valueOf(SurveyVariables.type));
        switch (questionType){
            case "Single":
                checkBoxContainer.removeAllViews();
                checkBoxContainer.setVisibility(View.GONE);
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
                radioGroup.removeAllViews();
                radioGroup.setVisibility(View.GONE);
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

    public void setAnswers(Poll p){
        Map<String, Map<String, Object> > userAnswer = new HashMap<>();
        userAnswer.put(currentUser.getUid(), answer);
        db.collection("pollAnswers")
                .document(p.getId())
                .set(userAnswer, SetOptions.merge());
        /*Map<String, Object> pollGraphQuest = new HashMap<>();
        Map<String, Object> pollGraphAns = new HashMap<>();
        Map<String, Map<String, Object> > pollGraph = new HashMap<>();
        //pollGraph.put(questionText.getText().toString())
        db.collection("pollGraphAnswers")
                .document(p.getId())
                .set(pollGraph, );*/
        Map<String, Boolean> pollComplMap = new HashMap<>();
        pollComplMap.put(p.getId(), true);
        db.collection("pollComplete").document(currentUser.getUid()).set(pollComplMap, SetOptions.merge());
        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.nav_polls);
        /*Bundle bundle = new Bundle();
        bundle.putSerializable("poll_id", p.getId());

        Navigation.findNavController(getParentFragment().getView()).navigate(R.id.pollGraphFragment, bundle);*/
    }


}
