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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddNewProduct extends AppCompatActivity {

    private EditText addNewProductName, addNewCost, addNewProductDesc, addNewProductStock;
    private Button btnAddNewSelectImg, btnAddNewIncreaseStock, btnAddNewDecreaseStock, btnAddNewSave;
    private ImageView addNewProductImg;
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private int productStock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        mAuth = FirebaseAuth.getInstance();

        addNewProductName = findViewById(R.id.addNewProductName);
        addNewCost = findViewById(R.id.addNewCost);
        addNewProductDesc = findViewById(R.id.addNewProductDesc);
        addNewProductStock = findViewById(R.id.addNewProductStock);

        btnAddNewSelectImg = findViewById(R.id.btnAddNewSelectImg);
        btnAddNewIncreaseStock = findViewById(R.id.btnAddNewIncreaseStock);
        btnAddNewDecreaseStock = findViewById(R.id.btnAddNewDecreaseStock);
        btnAddNewSave = findViewById(R.id.btnAddNewSave);

        addNewProductImg = findViewById(R.id.addNewProductImg);



        btnAddNewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNewProductImg.getDrawable() == null){
                    Toast.makeText(AddNewProduct.this,"Add Product Image", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(addNewProductName.getText().toString())) {
                    Toast.makeText(AddNewProduct.this,"Enter Product Name", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(addNewProductDesc.getText().toString())) {
                    Toast.makeText(AddNewProduct.this,"Enter Product Description", Toast.LENGTH_SHORT).show();
                }

                else {
                    saveDataToProductProfile();
                }
                //transitionToShopPage();
            }
        });

        btnAddNewIncreaseStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productStock = Integer.parseInt(addNewProductStock.getText().toString())+ 1;
                addNewProductStock.setText(String.valueOf(productStock));
            }
        });

        btnAddNewDecreaseStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productStock = Integer.parseInt(addNewProductStock.getText().toString())- 1;
                addNewProductStock.setText(String.valueOf(productStock));
            }
        });

        btnAddNewSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSelectImg();
            }
        });

    }

    private void addNewSelectImg()
    {
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

            addNewSelectImg();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            Uri chosenImageData = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageData);
                addNewProductImg.setImageBitmap(bitmap);


            } catch (Exception e) {

                e.printStackTrace();
            }

        }


    }

    private void saveDataToProductProfile()
    {

        uploadImageToServer();
    }

    private void transitionToSellerDashboard() {
        // transition to shop profile
        Intent intent = new Intent(AddNewProduct.this, SellerDashboard.class);
        startActivity(intent);
    }

    private void uploadImageToServer(){
        // Get the data from an ImageView as bytes
        addNewProductImg.setDrawingCacheEnabled(true);
        addNewProductImg.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        final String productID = FirebaseDatabase.getInstance().getReference().child("Products").push().getKey();
        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("product_images").child(productID).child("productImg.jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(AddNewProduct.this, exception.toString(), Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(AddNewProduct.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                // Uploading data to database
                FirebaseStorage.getInstance().getReference().child("product_images").child(productID).child("productImg.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Product newProduct = new Product();
                        newProduct.id = productID;
                        newProduct.imgUri = uri.toString();
                        newProduct.name = addNewProductName.getText().toString();
                        newProduct.cost = addNewCost.getText().toString();
                        newProduct.stock = addNewProductStock.getText().toString();
                        newProduct.description = addNewProductDesc.getText().toString();
                        FirebaseDatabase.getInstance().getReference().child("Products").child(productID).setValue(newProduct);
                        transitionToSellerDashboard();
                    }
                });

            }
        });

    }
}
