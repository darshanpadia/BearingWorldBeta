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

import java.util.ArrayList;
import java.util.HashMap;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView cOrderID,cOrderUsername,cOrderPhone,cOrderEmail,cOrderAddress,cOrderDatePlaced,cOrderDeliveryDate,cOrderTotal,cOrderShopPage;
    private ArrayList<Product> productList = new ArrayList();
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");

        cOrderID = findViewById(R.id.cOrderID);
        cOrderUsername = findViewById(R.id.cOrderUsername);
        cOrderPhone = findViewById(R.id.cOrderPhone);
        cOrderEmail = findViewById(R.id.cOrderMail);
        cOrderAddress = findViewById(R.id.cOrderAddress);
        cOrderDatePlaced = findViewById(R.id.cOrderDatePlaced);
        cOrderDeliveryDate = findViewById(R.id.cOrderDeliveryDate);
        cOrderTotal = findViewById(R.id.cOrderTotal);
        cOrderShopPage = findViewById(R.id.cOrderShopPage);

        cOrderID.setText("Order ID: " + order.id);
        cOrderUsername.setText("Username: " + order.userName);
        cOrderAddress.setText("Address: " + order.userAddress);
        cOrderPhone.setText("Phone No: " + order.userPhone);
        cOrderEmail.setText("E-mail: " + order.userEmail);
        cOrderDatePlaced.setText(order.dateOrderPlaced);
        cOrderDeliveryDate.setText(order.deliveryDate);
        cOrderTotal.setText("₹" +order.totalAmount);

        cOrderShopPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToShopPage();
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

    private void transitionToShopPage() {
        Intent intent = new Intent(OrderConfirmationActivity.this, DynamicShopPage.class);
        startActivity(intent);
    }

    private int dP(int P){
        // Converting From int(pixels) To DP
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, P, getResources().getDisplayMetrics());
        return dimensionInDp;
    }
}
