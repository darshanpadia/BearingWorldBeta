package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

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

import android.os.Bundle;

public class SellerEditProductActivity extends AppCompatActivity {

    private EditText SellerEditProductName, SellerEditCost, SellerEditProductDesc, SellerEditProductStock;
    private Button btnSellerEditSelectImg, btnSellerEditIncreaseStock, btnSellerEditDecreaseStock, btnSellerEditSave,btnRemoveProduct;
    private ImageView SellerEditProductImg;
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private Product thisProduct;
    private Boolean imageUpdated = false;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_edit_product);

        Intent i = getIntent();
        thisProduct = (Product) i.getSerializableExtra("Product");

        mAuth = FirebaseAuth.getInstance();
        SellerEditProductImg = findViewById(R.id.SellerEditProductImg);
        SellerEditProductName = findViewById(R.id.SellerEditProductName);
        SellerEditCost = findViewById(R.id.SellerEditCost);
        SellerEditProductDesc = findViewById(R.id.SellerEditProductDesc);
        SellerEditProductStock = findViewById(R.id.SellerEditProductStock);
        btnSellerEditSelectImg = findViewById(R.id.btnSellerEditSelectImg);
        btnSellerEditIncreaseStock = findViewById(R.id.btnSellerEditIncreaseStock);
        btnSellerEditDecreaseStock = findViewById(R.id.btnSellerEditDecreaseStock);
        btnSellerEditSave = findViewById(R.id.btnSellerEditSave);
        btnRemoveProduct = findViewById(R.id.btnSellerRemoveProduct);

        btnSellerEditSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProductPic();
            }
        });

        btnSellerEditIncreaseStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseStock();
            }
        });

        btnSellerEditDecreaseStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseStock();
            }
        });

        btnRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref = FirebaseDatabase.getInstance().getReference().child("Products").child(thisProduct.id);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            snapshot.getRef().removeValue();
                            transitionToSellerShopPage();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnSellerEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUpdated)
                {
                    uploadImageToServer();
                }
                saveProductData(thisProduct.imgUri);

            }
        });

        populateFields();




    }

    private void transitionToSellerShopPage()
    {
        Intent intent = new Intent(SellerEditProductActivity.this, SellerShopPageActivity.class);
        startActivity(intent);
    }

    private void uploadImageToServer(){
        // Get the data from an ImageView as bytes
        SellerEditProductImg.setDrawingCacheEnabled(true);
        SellerEditProductImg.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("product_images").child(thisProduct.id).child(thisProduct.id + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(SellerEditProductActivity.this, exception.toString(), Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(SellerEditProductActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                FirebaseStorage.getInstance().getReference().child("product_images").child(thisProduct.id).child(thisProduct.id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        saveProductData(uri.toString());
                    }
                });


            }
        });
    }



    private void saveProductData(String uri) {
        Product updatedProduct = new Product();
        updatedProduct.name = SellerEditProductName.getText().toString();
        updatedProduct.description = SellerEditProductDesc.getText().toString();
        updatedProduct.stock = SellerEditProductStock.getText().toString();
        updatedProduct.cost = SellerEditCost.getText().toString();
        updatedProduct.imgUri = uri;
        updatedProduct.id = thisProduct.id;
        FirebaseDatabase.getInstance().getReference().child("Products").child(thisProduct.id).setValue(updatedProduct);
        Toast.makeText(SellerEditProductActivity.this,"Updated successfully", Toast.LENGTH_SHORT).show();
        transitionToSellerDashboard();

    }

    private void transitionToSellerDashboard() {
        Intent intent = new Intent(SellerEditProductActivity.this, SellerDashboard.class);
        startActivity(intent);
    }

    private void decreaseStock() {
        int currentStock = Integer.parseInt(SellerEditProductStock.getText().toString());
        --currentStock;
        SellerEditProductStock.setText(String.valueOf(currentStock));
        thisProduct.stock = String.valueOf(currentStock);
    }

    private void increaseStock() {
        int currentStock = Integer.parseInt(SellerEditProductStock.getText().toString());
        ++currentStock;
        SellerEditProductStock.setText(String.valueOf(currentStock));
        thisProduct.stock = String.valueOf(currentStock);

    }

    private void populateFields() {
        SellerEditProductName.setText(thisProduct.name);
        SellerEditCost.setText(thisProduct.cost);
        SellerEditProductDesc.setText(thisProduct.description);
        SellerEditProductStock.setText(thisProduct.stock);
        Glide.with(SellerEditProductActivity.this).load(thisProduct.imgUri).into(SellerEditProductImg);

    }

    private void selectProductPic(){
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

            selectProductPic();

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
                SellerEditProductImg.setImageBitmap(bitmap);


            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }
}
