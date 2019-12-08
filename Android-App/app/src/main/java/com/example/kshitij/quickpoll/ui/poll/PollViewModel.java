package com.example.kshitij.quickpoll.ui.poll;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PollViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;
    public PollViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is poll fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
