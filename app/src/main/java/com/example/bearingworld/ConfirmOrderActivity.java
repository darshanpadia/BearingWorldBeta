package com.example.bearingworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ConfirmOrderActivity extends AppCompatActivity {

    private Order order;
    private Button btnProceed,btnContinueShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");
        for (final Product product: order.productList.values()) {

            LinearLayout cartproductLL =findViewById(R.id.cartProductLL);
            LinearLayout SingleProductLL = new LinearLayout(getBaseContext());


            SingleProductLL.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams SingleProductLLParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            SingleProductLL.setLayoutParams(SingleProductLLParams);
            SingleProductLLParams.setMargins(dP(5),dP(10),dP(10),dP(0));

//            TextView name = new TextView(getBaseContext());


            //productImg
            ImageView productImg = new ImageView(getBaseContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dP(70), dP(100));
            productImg.setLayoutParams(layoutParams);
            layoutParams.setMargins(dP(0),dP(5),dP(5),dP(5));
            Glide.with(this)
                    .asBitmap()
                    .load(product.imgUri)
                    .into(productImg);




            // Single product Description LL
            LinearLayout productDescLL = new LinearLayout(getBaseContext());
            productDescLL.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams productDescLLParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            productDescLLParams.setMargins(dP(0),dP(10),dP(10),dP(0));
            productDescLL.setLayoutParams(productDescLLParams);



            // Product Name TextView
            TextView productName = new TextView(getBaseContext());
            LinearLayout.LayoutParams productNameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            // Making new layout params from product name Tv
            productName.setLayoutParams(productNameParams);
//            productName.setGravity(Gravity.CENTER_HORIZONTAL);
            productName.setText(product.name);
            productName.setTextColor(Color.BLACK);
            productDescLL.addView(productName);


            //productStock
            TextView productStock = new TextView(getBaseContext());
            LinearLayout.LayoutParams productStockParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            productStock.setLayoutParams(productStockParams);
            productStockParams.setMargins(dP(0),dP(10),dP(0),dP(10));
            productStock.setText("Stock: " + product.stock);
            productDescLL.addView(productStock);
            // Making new layout params from product name Tv
//            productName.setGravity(Gravity.CENTER_HORIZONTAL);


            // Product Cost
            final TextView productCost = new TextView(getBaseContext());
            LinearLayout.LayoutParams productCostParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            productCost.setTextColor(Color.parseColor("#008000"));
            productCost.setText("₹" + product.productTotalCost);
            productCost.setLayoutParams(productCostParams);
            productDescLL.addView(productCost);


            //QuantityLL
            LinearLayout quantityLL = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams quantityLLParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            quantityLL.setOrientation(LinearLayout.VERTICAL);
            quantityLL.setLayoutParams(quantityLLParams);
            quantityLLParams.setMargins(dP(0),dP(10),dP(10),dP(0));



            //quantity
            final EditText quantity = new EditText(getBaseContext());
            LinearLayout.LayoutParams quantityParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            quantity.setLayoutParams(quantityParams);
            quantity.setGravity(Gravity.CENTER_VERTICAL);
            quantity.setText(product.quantity);
            quantityLL.addView(quantity);


            //btnQuantityLL
            LinearLayout btnQuantityLL = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams btnQuantityLLParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            btnQuantityLL.setLayoutParams(btnQuantityLLParams);
            btnQuantityLL.setOrientation(LinearLayout.VERTICAL);




            //btnIncrease
            Button btnIncrease = new Button(getBaseContext());
            LinearLayout.LayoutParams btnIncreaseParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
//            btnIncrease.setPadding(dP(6),dP(6),dP(6),dP(6));
            btnIncrease.setText("+");
//            btnIncrease.setWidth(dP(10));
//            btnIncrease.setGravity(Gravity.CENTER_HORIZONTAL);
            btnIncrease.setLayoutParams(btnIncreaseParams);
            btnQuantityLL.addView(btnIncrease);



            //btndecrease
            Button btnDecrease = new Button(getBaseContext());
            LinearLayout.LayoutParams btnDecreaseParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            btnDecrease.setLayoutParams(btnDecreaseParams);
            btnDecrease.setText("-");
            btnQuantityLL.addView(btnDecrease);
           btnDecrease.setWidth(dP(10));


            //removeProduct
            TextView removeProductX = new TextView(getBaseContext());
            LinearLayout.LayoutParams removeProductXParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
//            quantityTV.setTextColor(Color.parseColor("#F44336"));
//            quantityTV.setTextSize(dP(25));
            removeProductX.setGravity(Gravity.TOP);
            removeProductXParams.setMargins(dP(10),0,0,0);
            removeProductX.setLayoutParams(removeProductXParams);
            removeProductX.setTextColor(Color.parseColor("#F44336"));
            removeProductX.setText("X");



            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable quant) {
                    if(!quant.toString().trim().equals(""))
                        if (Integer.parseInt(quant.toString().trim()) >0 && Integer.parseInt(quant.toString().trim()) > Integer.parseInt(product.stock)){
                            quantity.setText(product.stock);
                            product.quantity = quantity.getText().toString();
                            product.productTotalCost = Integer.parseInt(product.cost) * Integer.parseInt(product.quantity);
                            productCost.setText("₹" +product.productTotalCost);
                        }
                        else if(quant.toString().trim() == "0"){
                            quantity.setText("1");
                            product.quantity = quantity.getText().toString();
                            product.productTotalCost = Integer.parseInt(product.cost) * Integer.parseInt(product.quantity);
                            productCost.setText("₹" +product.productTotalCost);
                        }
                        else{
//                            quantity.setText(quant);
                            product.quantity = quantity.getText().toString().trim();
                            product.productTotalCost = Integer.parseInt(product.cost) * Integer.parseInt(product.quantity);
                            productCost.setText("₹" +product.productTotalCost);
                        }
                    else{

                    }

                }
            });


            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tempQuant = Integer.parseInt(quantity.getText().toString());
                    int tempStock = Integer.parseInt(product.stock);
                    if (tempStock > tempQuant) {
                        tempQuant = tempQuant + 1;
                    }
                    quantity.setText(Integer.toString(tempQuant));
//                    product.quantity = (Integer.toString(tempQuant));
//                    productCost.setText("₹" + (Integer.parseInt(product.cost)*tempQuant));
//                    product.productTotalCost = Integer.parseInt(product.cost)*tempQuant;
                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = Integer.parseInt(quantity.getText().toString());
                    if (temp > 1) {
                        temp = temp - 1;
                    }
                    quantity.setText(Integer.toString(temp));
//                    product.quantity = (Integer.toString(temp));
//                    productCost.setText("₹" + (Integer.parseInt(product.cost)*temp));
//                    product.productTotalCost = Integer.parseInt(product.cost)*temp;
                }

            });

            removeProductX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order.productList.remove(product.id);
                    refreshProductList();
                }
            });



            SingleProductLL.addView(productImg);
            SingleProductLL.addView(productDescLL);
            SingleProductLL.addView(quantityLL);
            SingleProductLL.addView(btnQuantityLL);
            SingleProductLL.addView(removeProductX);
            cartproductLL.addView(SingleProductLL);



//            name.setLayoutParams(nameParams);
//            name.setText(p.name);
//            SingleProductLL.addView(name);
//            cartproductLL.addView(SingleProductLL);


        }
        btnProceed = findViewById(R.id.btnProceed);
//        btnProceed.setText(order.totalAmount);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order.productList.size() > 0) {
                    transitionToUserDetailPage();
                }
                else{
                    Toast.makeText(ConfirmOrderActivity.this,"Your Cart is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnContinueShop = findViewById(R.id.btnContinueShop);
        btnContinueShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToShopPage();
            }
        });



    }

    private void refreshProductList()
    {
        Intent intent = new Intent(ConfirmOrderActivity.this, ConfirmOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    private void transitionToShopPage()
    {

        Intent intent = new Intent(ConfirmOrderActivity.this, DynamicShopPage.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }


    private void transitionToUserDetailPage() {
        Intent intent = new Intent(ConfirmOrderActivity.this, UserDetailsActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    private int dP(int P){
        // Converting From int(pixels) To DP
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, P, getResources().getDisplayMetrics());
        return dimensionInDp;
    }


}
