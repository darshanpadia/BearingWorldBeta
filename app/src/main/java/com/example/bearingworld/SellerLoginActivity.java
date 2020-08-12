package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private EditText sLogin_page_password;
    private Button btnSLogin;
    private TextView textNotASeller;
    private FirebaseAuth mAuth;
    private String sellerID = "seller@bearingworld.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);


        sLogin_page_password = findViewById(R.id.sLogin_page_password);
        btnSLogin = findViewById(R.id.btnSLogin);
        textNotASeller = findViewById(R.id.textNewUser);

        btnSLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        textNotASeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToMainActivity();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    private void transitionToMainActivity()
    {
        Intent intent = new Intent(SellerLoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void login()
    {
        mAuth.signInWithEmailAndPassword( sellerID, sLogin_page_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    transitionToSellerDashboardActivity();
                    Toast.makeText(SellerLoginActivity.this, "Signing In Successfull", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SellerLoginActivity.this, "Signing In Failed", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void transitionToSellerDashboardActivity()
    {
        Intent intent = new Intent(SellerLoginActivity.this, SellerDashboard.class);
        startActivity(intent);
    }
}
