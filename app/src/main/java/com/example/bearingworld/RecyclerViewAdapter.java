package com.example.bearingworld;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Product> filteredProducts;
    private ArrayList<Product> productArrayList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Product> products){
        this.filteredProducts = products;
        this.mContext = context;
        this.productArrayList  = new ArrayList<>(products);
//        for (Product p : products)
//        {
//            productArrayListFull.add(p);
//        }+
        Log.v(TAG,"productArrayList Size" + filteredProducts.size());
        Log.v(TAG,"productArrayListFull Size" + productArrayList.size());
    }

    public void AddProducts(ArrayList<Product> ProductList){
        this.filteredProducts = new ArrayList<>(ProductList);
        this.productArrayList = new ArrayList<>(ProductList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name, desc, cost;
        Button buyNow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            desc = itemView.findViewById(R.id.productDescription);
            cost = itemView.findViewById(R.id.productCost);
            buyNow = itemView.findViewById(R.id.btnBuyProduct);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        this.productArrayListFull = new ArrayList<>(productArrayList);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        Log.v(TAG,"productArrayList Size" + filteredProducts.size());

        Log.v(TAG,"productArrayListFull Size" + productArrayList.size());
        Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(filteredProducts.get(position).imgUri)
                .into(holder.image);
        holder.name.setText(filteredProducts.get(position).name);
        holder.cost.setText("₹" + filteredProducts.get(position).cost);
        holder.desc.setText(filteredProducts.get(position).description);
        holder.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,filteredProducts.get(position).name, Toast.LENGTH_LONG).show();
                if (mContext instanceof DynamicShopPage) {
                    ((DynamicShopPage)mContext).singleProductPage(filteredProducts.get(position));
                }
                else if(mContext instanceof SellerShopPageActivity) {
                    ((SellerShopPageActivity)mContext).singleProductPage(filteredProducts.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> filteredList = new ArrayList<>();
            Log.v(TAG,"performFiltering : Started");
            Log.v(TAG, "Constraint: " + constraint.toString());
            if (constraint == null || constraint.length() == 0)
            {
                Log.v(TAG,"NO Constraint");
                filteredList.addAll(productArrayList);
                Log.v(TAG,"productArrayList Size" + filteredProducts.size());
                Log.v(TAG,"productArrayListFull Size" + productArrayList.size());
                Log.v(TAG,"filteredList Size" + filteredList.size());
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for  (Product p : productArrayList)
                {

                    if(p.name.toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(p);
                    }
                    Log.v(TAG,String.valueOf(filteredList.size()));
                }
            }
            Log.v(TAG,"filteredList Size" + String.valueOf(filteredList.size()));
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.v(TAG,"publishResults : Started");
            filteredProducts.clear();
            filteredProducts.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };


}
