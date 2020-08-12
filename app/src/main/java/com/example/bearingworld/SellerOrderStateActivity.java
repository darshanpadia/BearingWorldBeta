package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SellerOrderStateActivity extends AppCompatActivity {

    private TextView cOrderID,cOrderUsername,cOrderPhone,cOrderEmail,cOrderAddress,cOrderDatePlaced,cOrderDeliveryDate,cOrderTotal,cancel,dispatched,inTransit,delivered;
    private ArrayList<Product> productList = new ArrayList();
    private Order order;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_state);

        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");
        uid = (String) i.getSerializableExtra("uid");

        cOrderID = findViewById(R.id.cOrderID);
        cOrderUsername = findViewById(R.id.cOrderUsername);
        cOrderPhone = findViewById(R.id.cOrderPhone);
        cOrderEmail = findViewById(R.id.cOrderMail);
        cOrderAddress = findViewById(R.id.cOrderAddress);
        cOrderDatePlaced = findViewById(R.id.cOrderDatePlaced);
        cOrderDeliveryDate = findViewById(R.id.cOrderDeliveryDate);
        cOrderTotal = findViewById(R.id.cOrderTotal);
        cancel = findViewById(R.id.orderStateCancel);
        dispatched = findViewById(R.id.orderStateDispatched);
        inTransit = findViewById(R.id.orderStateInTransit);
        delivered = findViewById(R.id.orderStateDelivered);

        cOrderID.setText(order.id);
        cOrderUsername.setText(order.userName);
        cOrderAddress.setText(order.userAddress);
        cOrderPhone.setText(order.userPhone);
        cOrderEmail.setText(order.userEmail);
        cOrderDatePlaced.setText(order.dateOrderPlaced);
        cOrderDeliveryDate.setText(order.deliveryDate);
        cOrderTotal.setText("₹" +order.totalAmount);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.isActive = false;
                order.orderState = "Canceled";
                order.deliveryDate = "----";
                FirebaseDatabase.getInstance().getReference().child("Users").child(order.userID).child("orders").child(order.id).setValue(order);
                transitionToSellerOrdersPage();
            }
        });

        dispatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.isActive = true;
                order.orderState = "Dispatched";
                FirebaseDatabase.getInstance().getReference().child("Users").child(order.userID).child("orders").child(order.id).setValue(order);
                transitionToSellerOrdersPage();
            }
        });

        inTransit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.isActive = true;
                order.orderState = "In Transit";
                FirebaseDatabase.getInstance().getReference().child("Users").child(order.userID).child("orders").child(order.id).setValue(order);
                transitionToSellerOrdersPage();
            }
        });

        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.isActive = false;
                order.orderState = "Delivered";
                order.deliveryDate = "----";
                FirebaseDatabase.getInstance().getReference().child("Users").child(order.userID).child("orders").child(order.id).setValue(order);
                transitionToSellerOrdersPage();
            }
        });


        for (final Product product: order.productList.values() ) {
            LinearLayout cOrderProductList = findViewById(R.id.cOrderProductListLL);

            LinearLayout SingleProductLL = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams SingleProductLLParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            SingleProductLL.setLayoutParams(SingleProductLLParams);
            SingleProductLL.setOrientation(LinearLayout.HORIZONTAL);


            //product name Tv
            TextView productName = new TextView(getBaseContext());
            LinearLayout.LayoutParams productNameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            productNameParams.setMargins(dP(5), dP(10), dP(5), dP(0));
            productName.setLayoutParams(productNameParams);
            productName.setText(product.name + " * " + product.quantity);
            SingleProductLL.addView(productName);


            //product cost Tv
            TextView productCost = new TextView(getBaseContext());
            LinearLayout.LayoutParams productCostParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            productCost.setTextColor(Color.parseColor("#008000"));
            productCostParams.setMargins(dP(0), dP(10), 5, 0);
//                        product.productTotalCost = Integer.parseInt(product.cost) * Integer.parseInt(product.quantity);
            productCost.setText("₹" + product.productTotalCost);
            productCost.setLayoutParams(productCostParams);
            productCost.setGravity(Gravity.RIGHT);
            SingleProductLL.addView(productCost);

            //
            cOrderProductList.addView(SingleProductLL);
        }

    }

    private void transitionToSellerOrdersPage() {
        Intent intent = new Intent(SellerOrderStateActivity.this, MyOrdersSellerActivity.class);
        startActivity(intent);
    }

    private int dP(int P){
        // Converting From int(pixels) To DP
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, P, getResources().getDisplayMetrics());
        return dimensionInDp;
    }

}
