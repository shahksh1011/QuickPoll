package com.example.kshitij.quickpoll.ui.survey;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.adapters.SurveyAdapter;
import com.example.kshitij.quickpoll.data.model.Question;
import com.example.kshitij.quickpoll.data.model.Survey;
import com.example.kshitij.quickpoll.enums.SurveyVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SurveyFragment extends Fragment implements SurveyAdapter.SurveyFragmentInterface {

    private SurveyViewModel surveyViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Survey> list;
    RecyclerView recyclerView;
    SurveyAdapter surveyAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        surveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        final View root = inflater.inflate(R.layout.survey_fragment, container, false);

        list = new ArrayList<>();
        db.collection("surveys")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Survey survey=new Survey();
//                                public Survey(String surveyId, String createdBy, String surveyDescription, Date surveyCreated, Date surveryExpiry, List<?> questions, String surveyName)
                                Survey survey = new Survey(document.getId(),
                                        document.getString(String.valueOf(SurveyVariables.createdBy)),
                                        document.getString(String.valueOf(SurveyVariables.description)),
                                        document.getDate(String.valueOf(SurveyVariables.createdDate)),
                                        document.getDate(String.valueOf(SurveyVariables.expiry)),
                                        (List<?>) document.get(String.valueOf(SurveyVariables.questions)),
                                        document.getString(String.valueOf(SurveyVariables.surveyName)),
                                        document.getString(String.valueOf(SurveyVariables.timeToComplete)));
                                list.add(survey);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Log.d(TAG, "List Size" + list.size());
                            recyclerView = root.findViewById(R.id.recyvlerview_surveys);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            surveyAdapter = new SurveyAdapter(getContext(), list, SurveyFragment.this, root);
                            recyclerView.setAdapter(surveyAdapter);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

//        surveyViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onSurveyClick(Survey survey, View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("survey", survey);
        Navigation.findNavController(view).navigate(R.id.surveyDescriptionFragment,bundle);
    }
}
