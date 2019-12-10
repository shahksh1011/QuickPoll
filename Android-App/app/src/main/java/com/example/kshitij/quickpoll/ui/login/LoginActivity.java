package com.example.kshitij.quickpoll.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kshitij.quickpoll.MainActivity;
import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.Utilities.Constants;
import com.example.kshitij.quickpoll.ui.register.RegisterActivity;
import com.example.kshitij.quickpoll.Utilities.Validation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static String TAG = "LoginActivity";
    private LinearLayout loader;
    private Button registerButton;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        loginButton  = findViewById(R.id.login);
        registerButton  = findViewById(R.id.register);
        final TextView forgotPasswordButton = findViewById(R.id.forgot_password);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        loader = findViewById(R.id.login_header_progress);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loader.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
                userSignIn(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
            //loadingProgressBar.setVisibility(View.GONE);
            return false;
        });

        loginButton.setOnClickListener(v -> {
            closeKeyboard();
            loader.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            userSignIn(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            //loadingProgressBar.setVisibility(View.GONE);
        });

        registerButton.setOnClickListener(view -> {
            closeKeyboard();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        /*forgotPasswordButton.setOnClickListener(view -> {
            closeKeyboard();
            LayoutInflater layoutInflater = getLayoutInflater();
            final View v = layoutInflater.inflate(R.layout.forgot_password_dialog, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.CreateGroupDialogTheme);
            alertDialog.setTitle("Enter your email id :");
            EditText input = v.findViewById(R.id.dialog_email);
            input.setHint(R.string.prompt_email);

            alertDialog.setPositiveButton("Send",
                    (dialog, which) -> {

                    });
            alertDialog.setNegativeButton("Cancel",
                    (dialog, which) -> dialog.cancel());
            alertDialog.setView(v);
            alertDialog.show();
        });*/
        forgotPasswordButton.setOnClickListener(view -> {
            LayoutInflater layoutInflater = getLayoutInflater();
            final View v = layoutInflater.inflate(R.layout.forgot_password_dialog, null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.CreateGroupDialogTheme);
            alertDialog.setTitle("Enter Email Id :");
            final EditText input = v.findViewById(R.id.dialog_email);
            alertDialog.setPositiveButton("Send",
                    (dialog, which) -> {
                        String email = input.getText().toString();
                        Log.e(TAG, "Email = " + email);
                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(view.getContext(), "Email Sent Successfully to " + email, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(view.getContext(), Constants.UNABLE_SEND, Toast.LENGTH_LONG).show();
                                    }
                                });
                    });

            alertDialog.setNegativeButton("Cancel",
                    (dialog, which) -> dialog.cancel());
            alertDialog.setView(v);
            alertDialog.show();

        });
    }

    public void userSignIn(String email, String password){

        if (!isUserNameValid(email)) {
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_LONG).show();
            loader.setVisibility(View.GONE);
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
        }else if (!isPasswordValid(password)) {
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_LONG).show();
            loader.setVisibility(View.GONE);
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
        }else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    updateUiWithUser(mAuth.getCurrentUser());
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Log.d(TAG, task.getException().getMessage());
                    loader.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                    showLoginFailed(R.string.login_failed);
                }
            });
        }
    }
    private void updateUiWithUser(FirebaseUser currentUser) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        /*String welcome = getString(R.string.welcome) + currentUser.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();*/
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }



    private boolean isUserNameValid(String username) {
        if (Validation.isNullorEmpty(username)) {
            return false;
        }
        return Validation.isEmailValid(username);
    }
    private boolean isPasswordValid(String password) {
        if(Validation.isNullorEmpty(password)){
            return false;
        }
        return Validation.isPasswordValid(password);
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            updateUiWithUser(currentUser);
        }
    }
}
