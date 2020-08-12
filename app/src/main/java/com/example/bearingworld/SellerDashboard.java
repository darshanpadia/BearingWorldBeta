package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SellerDashboard extends AppCompatActivity {

    private Button btnSLogout, btnAddNewProduct,btnSOrders,btnEditProduct;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        btnSLogout = findViewById(R.id.btnSLogout);
        btnAddNewProduct = findViewById(R.id.btnAddNewProduct);
        btnEditProduct = findViewById(R.id.btnEditProduct);
        btnSOrders = findViewById(R.id.btnSOrders);

        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionSellerShopPageActivity();
            }
        });

        btnSLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sLogout();
            }
        });

        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToAddNewProductActivity();
            }
        });

        btnSOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToMyOrdersSellerActivity();
            }
        });


        mAuth= FirebaseAuth.getInstance();
    }

    private void transitionSellerShopPageActivity()
    {
        Intent intent = new Intent(SellerDashboard.this, SellerShopPageActivity.class);
        startActivity(intent);
    }

    private void transitionToMyOrdersSellerActivity()
    {
        Intent intent = new Intent(SellerDashboard.this, MyOrdersSellerActivity.class);
        startActivity(intent);
    }

    private void transitionToAddNewProductActivity()
    {
        Intent intent = new Intent(SellerDashboard.this, AddNewProduct.class);
        startActivity(intent);
    }

    private void sLogout()
    {
        mAuth.signOut();
        Intent intent = new Intent(SellerDashboard.this, MainActivity.class);
        startActivity(intent);
    }

}
