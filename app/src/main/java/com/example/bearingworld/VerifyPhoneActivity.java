package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class    VerifyPhoneActivity extends AppCompatActivity {

    private String verificationID;
    private FirebaseAuth mAuth;
    private Order order;
    DatabaseReference ref;
    private FirebaseUser currentUser;
    private String mVerificationId;
    private EditText code;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Button submit;
    private String phone;
    private String email,pass;
    DatabaseReference ref2;
    private Button btnSendCode,btnSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        Intent i = getIntent();
        phone = (String) i.getSerializableExtra("phone");
        email = (String) i.getSerializableExtra("email");
        pass = (String) i.getSerializableExtra("pass");

        btnSendCode = findViewById(R.id.btnSendCode);
        btnSubmit = findViewById(R.id.btnSubmit);
        mAuth = FirebaseAuth.getInstance();
        code = findViewById(R.id.code);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(code.getText().toString())){
                    Toast.makeText(VerifyPhoneActivity.this, "Enter OTP" , Toast.LENGTH_LONG).show();
                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });



        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNo(phone);
            }
        });



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("VerifyPhoneSuccess", "placeOrderWithCredential:success");
                            Toast.makeText(VerifyPhoneActivity.this, "Phone No Verified Successfully!" , Toast.LENGTH_LONG).show();

                            createAccount();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d("VerifyPhoneFailed", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyPhoneActivity.this, "The verification code entered was invalid!" , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void transitionToEditProfileActivity() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    final User user = new User();
                    user.Uid = mAuth.getUid();
                    user.phone = phone;
                    user.email = email;
                    user.imgUri = "https://foodcraftaligarh.com/wp-content/uploads/2018/05/profile-placeholder.jpg";
                    FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).setValue(user);

                    Toast.makeText(VerifyPhoneActivity.this, "Signing Up Successfull", Toast.LENGTH_LONG).show();
                    transitionToEditProfileActivity();
                } else {
                    Toast.makeText(VerifyPhoneActivity.this, "Signing Up Failed", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public void verifyPhoneNo(String phone){


        String phoneNumber = "+91"+ phone;
        Toast.makeText(VerifyPhoneActivity.this, "OTP has been sent to "+ phone, Toast.LENGTH_LONG).show();


        final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                createAccount();
//                setContentView(R.layout.user_profile);
                Toast.makeText(VerifyPhoneActivity.this, "Phone No Verified Successfully!" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(VerifyPhoneActivity.this, "Phone No Verification FAILED", Toast.LENGTH_LONG).show();
                Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();



            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d("VeirfyPhone onCodeSent", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


}
