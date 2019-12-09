package com.example.kshitij.quickpoll.ui.survey;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kshitij.quickpoll.data.model.Survey;

public class SurveyViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;
    private MutableLiveData<Survey> surveyMutableLiveData;
    public SurveyViewModel(){
        mText = new MutableLiveData<>();
        surveyMutableLiveData = new MutableLiveData<>();
        mText.setValue("This is survey fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
