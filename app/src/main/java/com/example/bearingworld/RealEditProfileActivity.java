package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RealEditProfileActivity extends AppCompatActivity {

    private EditText realEditUsername, realEditPhoneNo, realEditAddress;
    private Button btnRealEditChooseImage, btnRealEditSave, btnAccDeact;
    private ImageView realEditProfilePic;
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private User currentUser;
    private Boolean newProfilePicSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        realEditAddress = findViewById(R.id.realEditAddress);
        realEditPhoneNo = findViewById(R.id.realEditPhoneNo);
        realEditUsername = findViewById(R.id.realEditUsername);
        btnRealEditChooseImage = findViewById(R.id.btnRealEditChooseImage);
        btnRealEditSave = findViewById(R.id.btnRealEditSave);
        realEditProfilePic = findViewById(R.id.realEditProfilePic);
        btnAccDeact = findViewById(R.id.btnAccDeact);
        if (currentUser != null){
            populateFields();
        }


        // Firebase Storage code to get profile picture
        btnRealEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realEditProfilePic.getDrawable() == null){
                    Toast.makeText(RealEditProfileActivity.this,"Add Your Profile Pic", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(realEditUsername.getText().toString())|| (realEditUsername.equals("")))  {
                    Toast.makeText(RealEditProfileActivity.this,"Enter Your Username", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(realEditPhoneNo.getText().toString()) || (realEditPhoneNo.equals(""))) {
                    Toast.makeText(RealEditProfileActivity.this,"Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                }


                else if (TextUtils.isEmpty(realEditAddress.getText().toString())) {
                    Toast.makeText(RealEditProfileActivity.this,"Enter Address", Toast.LENGTH_SHORT).show();
                }

                else {
                    uploadImageToServer();
                }

            }
        });
        btnRealEditChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfilePic();
            }
        });






    }

    private void populateFields() {
        // Get value of username and set it in textview
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                Glide.with(RealEditProfileActivity.this).load(currentUser.imgUri).into(realEditProfilePic);
                realEditPhoneNo.setText(currentUser.phone);
                realEditAddress.setText(currentUser.address);
                realEditUsername.setText(currentUser.username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void transitionToUserProfile() {
        Intent intent = new Intent(RealEditProfileActivity.this, UserProfile.class);
        startActivity(intent);
    }

    private void saveDataToProfile(){
        // save phone, adress and profile pic to profile
        User user = new User();
        user.email = mAuth.getCurrentUser().getEmail();
        user.username = realEditUsername.getText().toString();
        user.address = realEditAddress.getText().toString();
        user.phone = realEditPhoneNo.getText().toString();
        user.Uid = mAuth.getUid();
        user.orders = currentUser.orders;
//        user.imgUri = uri.to;
        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).setValue(user);
        transitionToUserProfile();

    }

    private void selectProfilePic(){
        // select profile pic
        if (Build.VERSION.SDK_INT < 23) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        } else if (Build.VERSION.SDK_INT >= 23)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);

            } else {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            selectProfilePic();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            Uri chosenImageData = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageData);
                newProfilePicSelected = true;
                realEditProfilePic.setImageBitmap(bitmap);


            } catch (Exception e) {

                e.printStackTrace();
            }

        }


    }

    private void uploadImageToServer(){
        // Get the data from an ImageView as bytes
        realEditProfilePic.setDrawingCacheEnabled(true);
        realEditProfilePic.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("UserImages").child(mAuth.getUid()).child("profilePic.jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(RealEditProfileActivity.this, exception.toString(), Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(RealEditProfileActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                saveDataToProfile();


            }
        });
    }


}
