package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class ProductPageActivity extends AppCompatActivity {

    private Product thisProduct;
    private Order order = new Order();
    private TextView sellerInfo;
    private int tempStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        Intent i = getIntent();
        thisProduct = (Product)i.getSerializableExtra("Product");
        order = (Order)i.getSerializableExtra("order");
//        Log.v("FirstA","ProductA" + order.productList.size());


        // Populate information on single product page
        final ImageView pImage = findViewById(R.id.SingleProductImage);
        final TextView pName = findViewById(R.id.SingleProductName);
        final TextView pDesc = findViewById(R.id.SingleProductDesc);
        final TextView pCost = findViewById(R.id.SingleProductCost);
        final TextView pStock = findViewById(R.id.SingleProductStock);
        final Button buyNow = findViewById(R.id.SingleProductBuyNow);
        sellerInfo = findViewById(R.id.sellerInfo);

        pName.setText(thisProduct.name);
        pDesc.setText(thisProduct.description);
        pCost.setText("₹ " +thisProduct.cost);
        tempStock = Integer.parseInt(thisProduct.stock);
        if (tempStock > 0) {
            pStock.setText(thisProduct.stock);
        }
        else pStock.setText("Out of Stock");
        Glide.with(ProductPageActivity.this).load(thisProduct.imgUri).into(pImage);

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(thisProduct.stock) > 0)
                {
                    if (order.productList.keySet().contains(thisProduct.id)){
                        order.productList.get(thisProduct.id).quantity = String.valueOf(Integer.parseInt(order.productList.get(thisProduct.id).quantity)+1);
                        order.productList.get(thisProduct.id).productTotalCost = Integer.parseInt(order.productList.get(thisProduct.id).cost) * Integer.parseInt(order.productList.get(thisProduct.id).quantity);
                    }
                    else {
                        Log.v("Leg", "Prod" + thisProduct.quantity);
                        thisProduct.quantity = "1";
                        thisProduct.productTotalCost = Integer.parseInt(thisProduct.cost);
                        order.productList.put(thisProduct.id,thisProduct);
                    }
                    addProductToOrder(order);
                    Toast.makeText(ProductPageActivity.this,"Product added to cart", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ProductPageActivity.this,"Product Out of Stock", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sellerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToSellerContactActivity();
            }
        });




    }

    private void transitionToSellerContactActivity() {
        Intent intent = new Intent(ProductPageActivity.this, SellerContactActivity.class);
        startActivity(intent);
    }

    public void addProductToOrder(Order order){
        Intent intent = new Intent(ProductPageActivity.this, DynamicShopPage.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }


}
