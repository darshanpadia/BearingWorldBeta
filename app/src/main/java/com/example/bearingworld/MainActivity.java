package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText login_page_email, login_page_password;
    private Button btnLogin;
    private TextView textNewUser,textForgotPass,textSeller;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_page_email = findViewById(R.id.login_page_email);
        login_page_password = findViewById(R.id.login_page_password);
        btnLogin = findViewById(R.id.btnLogin);
        textNewUser = findViewById(R.id.textNewUser);
        textSeller = findViewById(R.id.textSeller);
        textForgotPass = findViewById(R.id.login_forgot_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(login_page_email.getText().toString())) {

                }
                else {
                    login();
                }
            }
        });
        textNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        textForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToResetPasswordActivity();
            }
        });

        textSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToSellerLoginActivity();
            }
        });

        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

//        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());

        if (currentUser != null) {
                transitionToShopPageActivity();
            }

    }

    private void login(){
        // login method
        mAuth.signInWithEmailAndPassword(login_page_email.getText().toString(), login_page_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    transitionToShopPageActivity();
                    Toast.makeText(MainActivity.this, "Signing In Successfull", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Signing In Failed", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void signUp(){
        // signUp method
        transitionToSignUpActivity();
    }

    private void transitionToShopPageActivity(){
        // change the activity
        Intent intent = new Intent(MainActivity.this, DynamicShopPage.class);
        startActivity(intent);
    }

    private void transitionToSellerDashboard() {
        Intent intent = new Intent(this, SellerDashboard.class);
        startActivity(intent);
    }


    private void transitionToSignUpActivity(){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void transitionToSellerLoginActivity()
    {
        Intent intent = new Intent(MainActivity.this, SellerLoginActivity.class);
        startActivity(intent);
    }

    private void transitionToResetPasswordActivity()
    {

        Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

}
