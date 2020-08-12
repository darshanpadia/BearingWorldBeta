package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    private TextView profilePageUsername, profilePageEmail, profilePagePhoneNo, profilePageAddress;
    private Button btnProfilePageEditProfile, btnProfilePageSignOut, btnGoToShopPage, btnMyOrders, btnAccDeact;
    private ImageView profilePageProfilePic;
    private FirebaseAuth mAuth;
    private User currentUser;
    private Order order = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");
        mAuth = FirebaseAuth.getInstance();
        profilePageUsername = findViewById(R.id.profilePageUsername);
        profilePageEmail = findViewById(R.id.profilePageEmail);
        profilePageAddress = findViewById(R.id.profilePageAddress);
        profilePagePhoneNo = findViewById(R.id.profilePagePhoneNo);
        btnProfilePageEditProfile = findViewById(R.id.btnProfilePageEditProfile);
        btnProfilePageSignOut = findViewById(R.id.btnProfilePageSignOut);
        profilePageProfilePic = findViewById(R.id.profilePageProfilePic);
        btnGoToShopPage = findViewById(R.id.btnGoToShopPage);
        btnMyOrders = findViewById(R.id.btnProfilePageMyOrders);
        btnAccDeact = findViewById(R.id.btnAccDeact);

        btnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToMyOrdersPage();
            }
        });

        btnGoToShopPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToShopPage();
            }
        });
        btnProfilePageEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToEditProfileActivity();
            }
        });
        btnGoToShopPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToShopPage();
            }
        });
        btnProfilePageSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                transitionToMainActivity();
            }
        });

        btnAccDeact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToAccountDeactivationActivity();
            }
        });

        // Get value of username and set it in textview
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.getValue(User.class);
                if(currentUser.imgUri != null){
                    Glide.with(UserProfile.this).load(currentUser.imgUri).into(profilePageProfilePic);
                }
                profilePagePhoneNo.setText(currentUser.phone);
                profilePageAddress.setText(currentUser.address);
                profilePageUsername.setText(currentUser.username);
                profilePageEmail.setText(currentUser.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void transitionToShopPage() {
        Intent intent = new Intent(this, DynamicShopPage.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    private void transitionToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void transitionToEditProfileActivity(){
        Intent intent = new Intent(UserProfile.this, EditProfileActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    public void transitionToMyOrdersPage(){
        Intent intent = new Intent(UserProfile.this, MyOrdersUser.class);
        startActivity(intent);
    }

    private void transitionToAccountDeactivationActivity() {
        Intent intent = new Intent(UserProfile.this, AccountDeactivationActivity.class);
        startActivity(intent);
    }

}
