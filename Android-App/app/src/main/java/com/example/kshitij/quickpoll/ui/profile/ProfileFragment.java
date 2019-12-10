package com.example.kshitij.quickpoll.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.Utilities.Constants;
import com.example.kshitij.quickpoll.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LoggedInUser userObj;
    private static String TAG = "ProfileFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        final TextView displayName = root.findViewById(R.id.profile_displayNameTextView);
        final TextView emailId = root.findViewById(R.id.profile_emailIdTextView);
        final TextView dob = root.findViewById(R.id.profile_dobTextView);
        final TextView gender = root.findViewById(R.id.profile_genderTextView);
        final ImageView profileImage = root.findViewById(R.id.profile_imageView);


        db.collection(Constants.USER_COLLECTION).document("/"+currentUser.getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Document found in the offline cache
                DocumentSnapshot document = task.getResult();
                userObj = new LoggedInUser(currentUser.getUid(), document.getData().get(Constants.FIRST_NAME).toString(),
                        document.getData().get(Constants.LAST_NAME).toString(), document.getData().get(Constants.EMAIL).toString(), document.getData().get(Constants.DOB).toString(),
                        document.getData().get(Constants.GENDER).toString());
                displayName.setText(userObj.getDisplayName());
                emailId.setText(userObj.getEmail());
                dob.setText(userObj.getDob());
                gender.setText(userObj.getGender());

                Log.d(TAG, "Cached document data: " + document.getData().get(Constants.FIRST_NAME));
            } else {
                Log.d(TAG, "Cached get failed: ", task.getException());
            }
                });

        //final TextView textView = root.findViewById(R.id.text_home);
        /*profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}