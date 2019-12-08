package com.example.kshitij.quickpoll.ui.poll;

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

public class PollFragment extends Fragment {

    private PollViewModel pollViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pollViewModel = ViewModelProviders.of(this).get(PollViewModel.class);
        View root = inflater.inflate(R.layout.poll_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_poll);
        pollViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });
        return root;
    }


}
