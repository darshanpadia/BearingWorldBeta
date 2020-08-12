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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerShopPageActivity extends AppCompatActivity {

    private ArrayList<Product> mProducts = new ArrayList<>();
    //    RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mProducts);
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mProducts);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_shop_page);

        initGetProducts();
        findViewById(R.id.sShopBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToSellerDashboard();
            }
        });
    }

    private void transitionToSellerDashboard() {
        Intent intent = new Intent(SellerShopPageActivity.this, SellerDashboard.class);
        startActivity(intent);
    }

    private void initGetProducts(){

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    mProducts.add(snap.getValue(Product.class));
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
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void singleProductPage(Product product){


        Intent intent = new Intent(SellerShopPageActivity.this, SellerEditProductActivity.class);
        intent.putExtra("Product", product);


        startActivity(intent);
    }
}
