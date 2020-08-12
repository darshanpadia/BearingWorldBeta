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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editProfilePhoneNo, editProfileAddress, editProfileUsername;
    private Button btnSelectProfilePic, btnEditProfileSave;
    private ImageView edtProfilePic;
    private FirebaseAuth mAuth;
    private Bitmap bitmap; // ImageView image compression extension
    private User currentUser;
    private Order order = new Order();
    private boolean imageUpdated = false;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");
        phone = (String) i.getSerializableExtra("phone");
//        editProfilePhoneNo = findViewById(R.id.editProfilePhoneNo);
        editProfileAddress = findViewById(R.id.editProfileAddress);
        btnSelectProfilePic = findViewById(R.id.btnSelectProfilePic);
        btnEditProfileSave = findViewById(R.id.btnEditProfileSave);
        edtProfilePic = findViewById(R.id.edtProfilePic);
        editProfileUsername = findViewById(R.id.edtProfileUsername);

        Glide.with(EditProfileActivity.this).load("https://foodcraftaligarh.com/wp-content/uploads/2018/05/profile-placeholder.jpg").into(edtProfilePic);

        populateFields();



        btnEditProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtProfilePic.getDrawable() == null){
                    Toast.makeText(EditProfileActivity.this,"Add Your Profile Pic", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(editProfileUsername.getText().toString())|| (editProfileUsername.equals("")))  {
                    Toast.makeText(EditProfileActivity.this,"Enter Your Username", Toast.LENGTH_SHORT).show();
                }



//                else if (TextUtils.isEmpty(editProfileEmail.getText().toString())|| (editProfileEmail.equals(""))) {
//                    Toast.makeText(EditProfileActivity.this,"Enter Your E-mail", Toast.LENGTH_SHORT).show();
//                }

                else if (TextUtils.isEmpty(editProfileAddress.getText().toString())) {
                    Toast.makeText(EditProfileActivity.this,"Enter Address", Toast.LENGTH_SHORT).show();
                }

                else {
                    if(imageUpdated)
                    {
                        uploadImageToServer();
                    }
                    else {
                        saveProfileData(currentUser.imgUri);
                    }
                }
            }
        });
        btnSelectProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfilePic();
            }
        });
    }


    private void saveDataToProfile(){
        // save profile pic to profile
        uploadImageToServer();

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
                imageUpdated = true;
                edtProfilePic.setImageBitmap(bitmap);


            } catch (Exception e) {

                e.printStackTrace();
            }

        }


    }

    private void uploadImageToServer(){
        // Get the data from an ImageView as bytes
        edtProfilePic.setDrawingCacheEnabled(true);
        edtProfilePic.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("UserImages").child(mAuth.getUid()).child("profilePic.jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(EditProfileActivity.this, exception.toString(), Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                FirebaseStorage.getInstance().getReference().child("UserImages").child(mAuth.getUid()).child("profilePic.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(EditProfileActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                        saveProfileData(uri.toString());
                        Toast.makeText(EditProfileActivity.this,"Profile updated successfully", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    private void saveProfileData(final String imgUri) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                if (currentUser!= null){
//                    if(currentUser.imgUri != null){
//                        Glide.with(EditProfileActivity.this).load(currentUser.imgUri).into(edtProfilePic);
//                    }
                    currentUser.Uid = mAuth.getUid();
                    currentUser.imgUri = imgUri;
                    currentUser.username = editProfileUsername.getText().toString();
                    currentUser.address = editProfileAddress.getText().toString();
                    currentUser.email = mAuth.getCurrentUser().getEmail();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).setValue(currentUser);
                    transitionToUserProfile();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                if (currentUser!= null){
//                    if(currentUser.imgUri != null){
//                        Glide.with(EditProfileActivity.this).load(currentUser.imgUri).into(edtProfilePic);
//                    }
                    Glide.with(EditProfileActivity.this).load(currentUser.imgUri).into(edtProfilePic);
                    editProfileAddress.setText(currentUser.address);
                    editProfileUsername.setText(currentUser.username);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void transitionToUserProfile(){
        // transition to shop profile
        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("order",order);
        startActivity(intent);
    }


}
