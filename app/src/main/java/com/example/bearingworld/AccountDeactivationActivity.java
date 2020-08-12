package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.annotation.NonNull;

public class AccountDeactivationActivity extends AppCompatActivity {

    private EditText textAccDeactPass;
    private TextView textAccDeactEmail;
    private Button btnAccDeact;
    private Order order;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_account_deactivation);
        textAccDeactEmail = findViewById(R.id.textAccDeactEmail);
        textAccDeactPass = findViewById(R.id.textAccDeactPass);
        btnAccDeact = findViewById(R.id.btnAccDeact);
        textAccDeactEmail.setText(mAuth.getCurrentUser().getEmail());

        btnAccDeact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textAccDeactEmail.getText().toString().trim();
                String password = textAccDeactPass.getText().toString().trim();


                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplication(), "Please Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }



                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, password);
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.e("TAG", "onComplete: authentication complete");
                                    user.delete()
                                            .addOnCompleteListener (new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.e("TAG", "User account deleted.");
                                                        Toast.makeText(AccountDeactivationActivity.this, "User Account Deactivated",
                                                                Toast.LENGTH_SHORT).show();
                                                        transitionToMainActivity();
                                                    } else {
                                                        Log.e("TAG", "User account deletion unsucessful.");
                                                        Toast.makeText(AccountDeactivationActivity.this, "Account Deactivation failed. Please try again later.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(AccountDeactivationActivity.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }

    private void transitionToMainActivity() {
        Intent intent = new Intent(AccountDeactivationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

