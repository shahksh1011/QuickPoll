package com.example.kshitij.quickpoll.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kshitij.quickpoll.MainActivity;
import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.Utilities.Constants;
import com.example.kshitij.quickpoll.Utilities.Validation;
import com.example.kshitij.quickpoll.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private static String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        final EditText emailEditText = findViewById(R.id.register_email);
        final EditText passwordEditText = findViewById(R.id.register_password);
        final EditText confirmPasswordEditText = findViewById(R.id.register_confirm_password);
        final Button registerButton = findViewById(R.id.register_btn);
        final EditText firstNameEditTest = findViewById(R.id.register_first_name);
        final EditText lastNameEditTest = findViewById(R.id.register_last_name);
        final ProgressBar loadingProgressBar = findViewById(R.id.register_loading);


        registerButton.setOnClickListener(view -> {
            closeKeyboard();
            loadingProgressBar.setVisibility(View.VISIBLE);
            validateFields(emailEditText.getText().toString(), passwordEditText.getText().toString(), firstNameEditTest.getText().toString(),
                    lastNameEditTest.getText().toString(), confirmPasswordEditText.getText().toString());
            loadingProgressBar.setVisibility(View.GONE);
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private  boolean validateFields(String email, String password, String firstName, String lastName, String confirmPwd){
        if (!isEmailValid(email)) {
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_LONG).show();
        }else if (!isPasswordValid(password, confirmPwd)) {
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_LONG).show();
        }else if(!Validation.isNullorEmpty(firstName)){
            Toast.makeText(this, "First name "+ R.string.empty_error, Toast.LENGTH_LONG).show();
        }else if(!Validation.isNullorEmpty(lastName)){
            Toast.makeText(this, "Last name "+ R.string.empty_error, Toast.LENGTH_LONG).show();
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail:success");
                String id = mAuth.getCurrentUser().getUid();
                LoggedInUser userObj = new LoggedInUser(id, firstName, lastName, email);
                Map<String, Object> user = new HashMap<>();
                user.put(Constants.FIRST_NAME, userObj.getFirstName());
                user.put(Constants.LAST_NAME, userObj.getLastName());
                user.put(Constants.EMAIL, userObj.getEmail());

                firebaseFirestore.collection("users").document(userObj.getUserId()).set(user)
                        .addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            startActivity(intent);
                        }).addOnFailureListener(e -> {
                            Log.w(TAG, "Error writing document", e);
                        });
            }else {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean isEmailValid(String username) {
        if (Validation.isNullorEmpty(username)) {
            return false;
        }
        return Validation.isEmailValid(username);
    }
    private boolean isPasswordValid(String password, String confirmPwd) {
        if(Validation.isNullorEmpty(password) || Validation.isNullorEmpty(confirmPwd)){
            return false;
        }
        if(password.equals(confirmPwd)){
            return false;
        }
        return Validation.isPasswordValid(password);
    }

    private void updateUI(FirebaseUser user){

    }
}
