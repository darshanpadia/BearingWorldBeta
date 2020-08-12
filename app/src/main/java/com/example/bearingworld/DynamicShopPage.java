package com.example.bearingworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DynamicShopPage extends AppCompatActivity {

    //vars
    private Button btnCart, btnUserProfile;
    private ArrayList<Product> mProducts = new ArrayList<>();
    private static final String TAG = "DynamicShopPage2";
//    RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mProducts);
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mProducts);
    private Order order = new Order();
    private Order tempOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_shop_page);
        btnCart = findViewById(R.id.btnCart);
        btnUserProfile = findViewById(R.id.btnUserProfile);
        Intent i = getIntent();
        tempOrder = (Order) i.getSerializableExtra("order");
        if (tempOrder != null) {
            order = tempOrder;
        }
        initGetProducts();

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrderPage(order);
            }
        });

        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToUserProfile(order);
            }
        });


    }



    private void transitionToUserProfile(Order order)
    {
        Intent intent = new Intent(DynamicShopPage.this, UserProfile.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    private void initGetProducts(){
        Log.v(TAG, "Getting products from firebase");
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    mProducts.add(snap.getValue(Product.class));
                    Log.v(TAG, "Appended Product");
                }
                adapter.AddProducts(mProducts);
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.allProductsRecycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.v(TAG,"onQueryTextChange : Started");
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    // Jump to individual product page
    public void singleProductPage(Product product){


        Intent intent = new Intent(DynamicShopPage.this, ProductPageActivity.class);
        intent.putExtra("Product", product);
        intent.putExtra("order", order);


//        intent.putExtra("selectedProduct", product);
//        intent.putExtra("productArrayListOrder", productArrayListOrder);
        startActivity(intent);
    }

    public void ConfirmOrderPage(Order order){
        Intent intent = new Intent(DynamicShopPage.this, ConfirmOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }


}
