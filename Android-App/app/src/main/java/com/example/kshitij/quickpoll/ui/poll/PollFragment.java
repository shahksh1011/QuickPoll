package com.example.kshitij.quickpoll.ui.poll;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kshitij.quickpoll.MainActivity;
import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.Utilities.Validation;
import com.example.kshitij.quickpoll.data.model.Poll;
import com.example.kshitij.quickpoll.data.model.Survey;
import com.example.kshitij.quickpoll.ui.codeScanner.ScanQRcodeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PollFragment extends Fragment {

    private PollViewModel pollViewModel;
    private Button scanCode;
    private Button submitCode;
    private EditText pollIdTxt;
    private final static int SCAN_REQUEST_CODE = 100;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    HashMap<String,Object> pollCheck;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pollViewModel = ViewModelProviders.of(this).get(PollViewModel.class);
        View root = inflater.inflate(R.layout.poll_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        pollCheck = new HashMap<>();
        submitCode = root.findViewById(R.id.poll_open_poll_btn);
        scanCode = root.findViewById(R.id.poll_scan_code_btn);
        db.collection("pollComplete")
                .document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.getData()!=null){
                        pollCheck = (HashMap<String, Object>) snapshot.getData();
                    }
                }
            }
        });
        pollIdTxt = root.findViewById(R.id.poll_room_id);
        scanCode.setOnClickListener(view -> startActivityForResult(new Intent(getActivity(), ScanQRcodeActivity.class),SCAN_REQUEST_CODE));

        submitCode.setOnClickListener(view -> {
            if(!Validation.isNullorEmpty(pollIdTxt.getText().toString())) {
                //startPoll(pollIdTxt.getText().toString());
                //Toast.makeText(getContext(), pollIdTxt.getText().toString(), Toast.LENGTH_SHORT).show();
                if(!pollCheck.containsKey(pollIdTxt.getText().toString())){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("poll_id", pollIdTxt.getText().toString());
                    Navigation.findNavController(view).navigate(R.id.pollQuestFragment,bundle);
                }else{
                    Toast.makeText(getContext(), "Already Submitted the Poll", Toast.LENGTH_SHORT).show();
                }

                //Navigation.findNavController(view).navigate(R.id.pollGraphFragment, bundle);
            }else{
                Toast.makeText(getContext(), R.string.error_room_number, Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String pollId = data.getStringExtra("result_poll_id");
                pollIdTxt.setText(pollId);
            }
        }
    }

    public void startPoll(String pollId){

    }
}
