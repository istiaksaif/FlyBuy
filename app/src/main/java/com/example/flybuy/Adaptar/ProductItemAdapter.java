package com.example.flybuy.Adaptar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.flybuy.Activity.ProductDetailsActivity;
import com.example.flybuy.Model.ProductItemList;
import com.example.flybuy.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {
    private static final String Tag = "RecyclerView";
    private Context context;
    private ArrayList<ProductItemList> mdata;

    public ProductItemAdapter(Context context, ArrayList<ProductItemList> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.card_product,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(mdata.get(position).getProductName());
        holder.itemPrice.setText("$"+mdata.get(position).getProductPrice());

        Glide.with(context).load(mdata.get(position).getProductImage()).placeholder(R.drawable.dropdown).error(R.drawable.errormessage).into(holder.itemImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productid",mdata.get(position).getProductId());
                intent.putExtra("productimg",mdata.get(position).getProductImage());
                intent.putExtra("productname",mdata.get(position).getProductName());
                intent.putExtra("productprice",mdata.get(position).getProductPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage,itemRating;
        TextView itemName, itemStory,itemPrice;
        FloatingActionButton cartButton,arButton;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.pimage);
            itemName = (TextView) itemView.findViewById(R.id.pname);
            itemPrice = (TextView) itemView.findViewById(R.id.pprice);
            cardView = (CardView) itemView.findViewById(R.id.productcard);
        }
    }
}
