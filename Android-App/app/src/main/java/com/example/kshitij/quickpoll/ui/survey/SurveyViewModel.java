package com.example.kshitij.quickpoll.ui.survey;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SurveyViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;
    public SurveyViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is survey fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
