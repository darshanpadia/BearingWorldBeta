package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity {

    private Order order = new Order();
    private EditText username, userphone, useraddress;
    private TextView useremail;
    private Button btnproceed;
    private FirebaseAuth mAuth;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");

        username = findViewById(R.id.userDetailsUsername);
        userphone = findViewById(R.id.userDetailsPhoneNo);
        useremail = findViewById(R.id.userDetailsEmail);
        useraddress = findViewById(R.id.userDetailsAddress);
        btnproceed = findViewById(R.id.userDetailsProceed);
        mAuth = FirebaseAuth.getInstance();

        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(UserDetailsActivity.this,"Enter Your Username", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(userphone.getText().toString())) {
                    Toast.makeText(UserDetailsActivity.this,"Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                }

                else if (userphone.getText().toString().length() != 10){
                    Toast.makeText(UserDetailsActivity.this,"Enter a Valid(10 digit) Phone Number", Toast.LENGTH_SHORT).show();
                }
//                else if (TextUtils.isEmpty(useremail.getText().toString())) {
//                    Toast.makeText(UserDetailsActivity.this,"Enter Your E-mail", Toast.LENGTH_SHORT).show();
//                }
                else if (TextUtils.isEmpty(useraddress.getText().toString())) {
                    Toast.makeText(UserDetailsActivity.this,"Enter Your Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    order.userName = username.getText().toString();
                    order.userPhone = userphone.getText().toString();
                    order.userEmail = useremail.getText().toString();
                    order.userAddress = useraddress.getText().toString();
                    transitionToPlaceOrderActivity();
                }

            }
        });



        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);

                userphone.setText(currentUser.phone);
                useraddress.setText(currentUser.address);
                username.setText(currentUser.username);
                useremail.setText(currentUser.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void transitionToPlaceOrderActivity()
    {
        Intent intent = new Intent(UserDetailsActivity.this, PlaceOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

}
