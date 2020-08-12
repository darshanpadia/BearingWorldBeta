package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyOrdersSellerActivity extends AppCompatActivity {


    private HashMap<String, Product> myProductList = new HashMap<>();
    private HashMap<String, Order> myOrdersList = new HashMap<>();

//    private HashMap<String, User> userList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_seller);

        findViewById(R.id.sMyOrdersBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToSellerDashboard();
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,User> userList = new HashMap<>();
                for (DataSnapshot userid: dataSnapshot.getChildren()){
                    User user = userid.getValue(User.class);
                    userList.put(userid.getKey(),user);
                }

                for (User user : userList.values()) {
                    myOrdersList = user.orders;
                    final String uid = user.Uid;


                    for (final Order order : myOrdersList.values()) {
                        LinearLayout myOrdersLL = findViewById(R.id.myOrdersLL);
                        LinearLayout SingleOrderLL = new LinearLayout(getBaseContext());

                        SingleOrderLL.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams SingleOrderLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        SingleOrderLL.setLayoutParams(SingleOrderLLParams);
                        SingleOrderLLParams.setMargins(dP(10), dP(10), dP(10), dP(10));

                        //order id TV
                        TextView orderID = new TextView(getBaseContext());
                        LinearLayout.LayoutParams orderIDParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        orderID.setLayoutParams(orderIDParams);
                        orderID.setGravity(Gravity.CENTER_HORIZONTAL);
                        orderID.setText("Order ID: " + order.id);
                        orderID.setTextSize(dP(7));
                        orderID.setTextColor(Color.BLACK);
                        SingleOrderLL.addView(orderID);


                        //UserDetailLL
                        LinearLayout myOrderUserDetailLL = new LinearLayout(getBaseContext());
                        LinearLayout.LayoutParams UserDetailLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        UserDetailLLParams.setMargins(dP(0),dP(5),dP(0),dP(5));
                        myOrderUserDetailLL.setLayoutParams(UserDetailLLParams);
                        myOrderUserDetailLL.setGravity(Gravity.CENTER_HORIZONTAL);
                        myOrderUserDetailLL.setOrientation(LinearLayout.VERTICAL);

                        //usernameLL
                        LinearLayout userNameLL = new LinearLayout(getBaseContext());
                        LinearLayout.LayoutParams userNameLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        userNameLL.setLayoutParams(userNameLLParams);
                        userNameLL.setOrientation(LinearLayout.HORIZONTAL);



                        //usernameTag
                        TextView userNameTag = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userNameTagParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userNameTagParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userNameTag.setLayoutParams(userNameTagParams);
                        userNameTag.setText("Username: ");
                        userNameLL.addView(userNameTag);

                        //username
                        TextView userName = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userNameParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userNameParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userName.setLayoutParams(userNameParams);
                        userName.setText(order.userName);
                        userNameLL.addView(userName);

                        //
                        myOrderUserDetailLL.addView(userNameLL);


                        //useremailLL
                        LinearLayout userMailLL = new LinearLayout(getBaseContext());
                        LinearLayout.LayoutParams userMailLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        userMailLL.setLayoutParams(userMailLLParams);
                        userMailLL.setOrientation(LinearLayout.HORIZONTAL);

                        //usermailTag
                        TextView userMailTag = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userMailTagParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userMailTagParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userMailTag.setLayoutParams(userMailTagParams);
                        userMailTag.setText("E-mail: ");
                        userMailLL.addView(userMailTag);

                        //useremail
                        TextView userMail = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userMailParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userMailParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userMail.setLayoutParams(userMailParams);
                        userMail.setText(order.userEmail);
                        userMailLL.addView(userMail);

                        //
                        myOrderUserDetailLL.addView(userMailLL);


                        //userPhoneLL
                        LinearLayout userPhoneLL = new LinearLayout(getBaseContext());
                        LinearLayout.LayoutParams userPhoneLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        userPhoneLL.setLayoutParams(userPhoneLLParams);
                        userPhoneLL.setOrientation(LinearLayout.HORIZONTAL);

                        //userPhoneTag
                        TextView userPhoneTag = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userPhoneTagParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userPhoneTagParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userPhoneTag.setLayoutParams(userPhoneTagParams);
                        userPhoneTag.setText("Phone: ");
                        userPhoneLL.addView(userPhoneTag);

                        //userPhone
                        TextView userPhone = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userPhoneParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userPhoneParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userPhone.setLayoutParams(userPhoneParams);
                        userPhone.setText(order.userPhone);
                        userPhoneLL.addView(userPhone);

                        //
                        myOrderUserDetailLL.addView(userPhoneLL);


                        //userAddressLL
                        LinearLayout userAddressLL = new LinearLayout(getBaseContext());
                        LinearLayout.LayoutParams userAddressLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        userAddressLL.setLayoutParams(userAddressLLParams);
                        userAddressLL.setOrientation(LinearLayout.HORIZONTAL);

                        //useraddressTag
                        TextView userAddressTag = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userAddressTagParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userAddressTagParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userAddressTag.setLayoutParams(userAddressTagParams);
                        userAddressTag.setText("Address: ");
                        userAddressLL.addView(userAddressTag);

                        //userAddress
                        TextView userAddress = new TextView(getBaseContext());
                        LinearLayout.LayoutParams userAddressParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        userAddressParams.setMargins(dP(0), dP(5), dP(0), dP(0));
                        userAddress.setLayoutParams(userAddressParams);
                        userAddress.setText(order.userAddress);
                        userAddressLL.addView(userAddress);

                        //
                        myOrderUserDetailLL.addView(userAddressLL);


                        //
                        SingleOrderLL.addView(myOrderUserDetailLL);


                        //product list scrollview
                        ScrollView myOrderProductList = new ScrollView(getBaseContext());
                        ScrollView.LayoutParams productListParams = new ScrollView.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        //                    productListParams.setMargins(dP(10),dP(5),dP(10),dP(5));
                        myOrderProductList.setLayoutParams(productListParams);

                        //Product list LL
                        LinearLayout myOrderProductListLL = new LinearLayout(getBaseContext());
                        LinearLayout.LayoutParams productListLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        productListLLParams.setMargins(dP(10),dP(5),dP(10),dP(5));
                        myOrderProductListLL.setLayoutParams(productListLLParams);
                        myOrderProductListLL.setOrientation(LinearLayout.VERTICAL);


                        //populate product list
                        //                    myProductList = order.productList.values();

                        //populate product LL
                        for (final Product product : order.productList.values()) {


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
                            myOrderProductListLL.addView(SingleProductLL);
                        }

                        //
                        myOrderProductList.addView(myOrderProductListLL);

                        //
                        SingleOrderLL.addView(myOrderProductList);


                        //Date Layout
                        LinearLayout myOrderdateLL = new LinearLayout(getBaseContext());
                        myOrderdateLL.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams myOrderDateLLParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        myOrderdateLL.setLayoutParams(myOrderDateLLParams);
                        myOrderDateLLParams.setMargins(dP(5), dP(5), dP(5), dP(0));

                        //DOP
                        TextView orderPlaceDate = new TextView(getBaseContext());
                        LinearLayout.LayoutParams orderPlaceDateParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        orderPlaceDateParams.setMargins(dP(0), dP(2), dP(5), dP(0));
                        orderPlaceDate.setLayoutParams(orderPlaceDateParams);
                        orderPlaceDate.setText("Order Placed:\n" + order.dateOrderPlaced);
                        myOrderdateLL.addView(orderPlaceDate);


                        //DD
                        TextView deliveryDate = new TextView(getBaseContext());
                        LinearLayout.LayoutParams deliveryDateParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        deliveryDateParams.setMargins(dP(5), dP(2), 0, 0);
                        deliveryDate.setText("Expected Delivery Date:\n" + order.deliveryDate);
                        deliveryDate.setLayoutParams(deliveryDateParams);
                        deliveryDate.setGravity(Gravity.RIGHT);
                        myOrderdateLL.addView(deliveryDate);

                        //orderState
                        TextView orderState = new TextView(getBaseContext());
                        LinearLayout.LayoutParams isActiveParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        isActiveParams.setMargins(dP(5), dP(2), 0, 0);
                        orderState.setText(order.orderState);
//                        orderState.setTextColor(Color.WHITE);
                        if (orderState.getText().toString().equals("Canceled")){
                            orderState.setBackgroundColor(Color.RED);
                        }
                        else if (orderState.getText().toString().equals("Dispatched")){
                            int orange = Color.rgb(255, 165, 0);
                            orderState.setBackgroundColor(orange);
                        }
                        else if (orderState.getText().toString().equals("In Transit")){
                            orderState.setBackgroundColor(Color.YELLOW);
                        }
                        else if (orderState.getText().toString().equals("Delivered")){
                            orderState.setBackgroundColor(Color.GREEN);
                        }
                        orderState.setLayoutParams(isActiveParams);
                        orderState.setGravity(Gravity.CENTER_HORIZONTAL);
                        SingleOrderLL.addView(orderState);




                        //
                        SingleOrderLL.addView(myOrderdateLL);

                        //total amount
                        TextView totalAmount = new TextView(getBaseContext());
                        LinearLayout.LayoutParams totalAmountParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        totalAmount.setTextColor(Color.parseColor("#008000"));
                        totalAmountParams.setMargins(dP(5), dP(2), 0, 0);
                        totalAmount.setText("₹" + order.totalAmount);
                        totalAmount.setTextSize(dP(15));
                        totalAmount.setLayoutParams(totalAmountParams);
                        totalAmount.setGravity(Gravity.CENTER_HORIZONTAL);
                        SingleOrderLL.addView(totalAmount);



                        //Order Detail
                        TextView orderDetails = new TextView(getBaseContext());
                        LinearLayout.LayoutParams orderDetailsParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        orderDetails.setTextColor(Color.parseColor("#008000"));
                        orderDetailsParams.setMargins(dP(5), dP(2), 0, 0);
                        orderDetails.setText(">>>");
                        orderDetails.setTextSize(dP(10));
                        orderDetails.setBackgroundColor(Color.BLUE);
                        orderDetails.setTextColor(Color.WHITE);
                        orderDetails.setLayoutParams(orderDetailsParams);
                        orderDetails.setGravity(Gravity.CENTER_HORIZONTAL);
                        SingleOrderLL.addView(orderDetails);

                        orderDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                transitionToOrderDetailsPage(order,uid);
                            }
                        });


                        //
                        myOrdersLL.addView(SingleOrderLL);



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        populateOrders();


    }

    private void transitionToOrderDetailsPage(Order order, String uid)
    {
        Intent intent = new Intent(MyOrdersSellerActivity.this, SellerOrderStateActivity.class);
        intent.putExtra("order",order);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    private void transitionToSellerDashboard() {
        Intent intent = new Intent(MyOrdersSellerActivity.this, SellerDashboard.class);
        startActivity(intent);
    }


//
//
//
//        }
//    }
    private int dP(int P) {
        // Converting From int(pixels) To DP
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, P, getResources().getDisplayMetrics());
        return dimensionInDp;
    }


}