package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText registerEmail,registerPhone, registerPassword, registerConfirmPassword,registerOTPedt;
    private Button btnSignUp,registerOTPbtn;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        registerEmail = findViewById(R.id.registerEmail);
        registerPhone = findViewById(R.id.registerPhone);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }



    private void signUp(){

        if (TextUtils.isEmpty(registerEmail.getText().toString())) {
            Toast.makeText(SignUpActivity.this,"Enter Your E-mail", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(registerPhone.getText().toString()) || (registerPhone.equals(""))) {
            Toast.makeText(SignUpActivity.this,"Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        }

        else if (registerPhone.getText().toString().length() != 10){
            Toast.makeText(SignUpActivity.this,"Enter a Valid(10 digit) Phone Number", Toast.LENGTH_SHORT).show();
        }

        else if(registerPassword.length()<6){
            Toast.makeText(SignUpActivity.this, "Password Too Short",Toast.LENGTH_SHORT).show();
        }
        else if (!registerPassword.getText().toString().equals(registerConfirmPassword.getText().toString())){
            Toast.makeText(SignUpActivity.this, "Passwords Don't Match",Toast.LENGTH_SHORT).show();
        }

        else {
            CharSequence email = registerEmail.getText().toString();
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                transitionToVerifyPhoneActivityActivity();
            }
            else {
                Toast.makeText(SignUpActivity.this, "Please enter correct Email Address",Toast.LENGTH_SHORT).show();
            }



        }

    }

    private void transitionToVerifyPhoneActivityActivity(){
        // uploadprofilepic add address phone no etc
        Intent intent = new Intent(this, VerifyPhoneActivity.class);
        intent.putExtra("phone",registerPhone.getText().toString());
        intent.putExtra("email",registerEmail.getText().toString());
        intent.putExtra("pass",registerPassword.getText().toString());
        startActivity(intent);
    }


}
