package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class PlaceOrderActivity extends AppCompatActivity {

    private Order order = new Order();
    private TextView placeOrderUsernameTV,placeOrderPhoneTV,placeOrderEmailTV,placeOrderAddressTV, placeOrderTotalTV;
    private Button btnPlaceOrder;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");
        String phone;
        Window.Callback mcallbacks;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        order.userID = currentUser.getUid();

        //Getting Id's
        placeOrderUsernameTV = findViewById(R.id.placeOrderUsenameTV);
        placeOrderEmailTV = findViewById(R.id.placeOrderEMailTV);
        placeOrderPhoneTV = findViewById(R.id.placeOrderPhoneTV);
        placeOrderAddressTV = findViewById(R.id.placeOrderAddressTV);

        //
        placeOrderTotalTV = findViewById(R.id.placeOrderTotalTV);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // fill User Info
        placeOrderUsernameTV.setText(order.userName);
        placeOrderEmailTV.setText(order.userEmail);
        placeOrderPhoneTV.setText(order.userPhone);
        placeOrderAddressTV.setText(order.userAddress);

        //populate product LL
        for (final Product product: order.productList.values() ){
            LinearLayout placeOrderProductLL = findViewById(R.id.placeOrderProductLL);
            LinearLayout SingleProductLL = new LinearLayout(getBaseContext());
            SingleProductLL.setOrientation(LinearLayout.HORIZONTAL);


            //product name Tv
            TextView productName = new TextView(getBaseContext());
            LinearLayout.LayoutParams productNameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            productNameParams.setMargins(dP(5),dP(2),dP(5),dP(0));
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
            productCostParams.setMargins(dP(5),dP(2),0,0);
            product.productTotalCost = Integer.parseInt(product.cost) * Integer.parseInt(product.quantity);
            productCost.setText("₹" + product.productTotalCost);
            productCost.setLayoutParams(productCostParams);
            productCost.setGravity(Gravity.RIGHT);


            //
            SingleProductLL.addView(productCost);
            placeOrderProductLL.addView(SingleProductLL);

            //calculate Total

            order.totalAmount = order.totalAmount + product.productTotalCost;


        }

        //Total amount
        placeOrderTotalTV.setText("₹" + String.valueOf(order.totalAmount));

        //btn
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transitionToConfirmOrderPage();

                generateorderdetails();
                transitionToConfirmOrderPage();
                reflectStockDetail();

            }
        });






    }

    private void transitionToVerifyPhoneActivity() {
        Intent intent = new Intent(PlaceOrderActivity.this, VerifyPhoneActivity.class);
//        intent.putExtra("phone","+91" +order.userPhone );
        intent.putExtra("order",order );

        startActivity(intent);
    }


    private void reflectStockDetail() {
        ref = FirebaseDatabase.getInstance().getReference().child("Products");
        for (Product product : order.productList.values()){
            int Stock = Integer.parseInt(product.stock);
            int quantity = Integer.parseInt(product.quantity);
            Stock -= quantity;

            ref.child(product.id).child("stock").setValue(String.valueOf(Stock));
        }

    }



    private void transitionToConfirmOrderPage()
    {
        Intent intent = new Intent(PlaceOrderActivity.this, OrderConfirmationActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    private void generateorderdetails()
    {
        final String orderID = FirebaseDatabase.getInstance().getReference().push().getKey();  ///.child("Users").child(currentUser.getUid()).child("orders")
        order.id = orderID;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        order.dateOrderPlaced = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        order.deliveryDate = sdf.format(cal.getTime());
        order.isActive = true;
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("orders").child(order.id).setValue(order);// .child("Users").child(currentUser.getUid())

//        Intent intent = new Intent(MainActivity.this, DynamicShopPage.class);
//        startActivity(intent);

    }

    private int dP(int P){
        // Converting From int(pixels) To DP
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, P, getResources().getDisplayMetrics());
        return dimensionInDp;
    }


}
