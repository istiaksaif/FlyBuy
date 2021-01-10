package com.example.flybuy.Adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flybuy.Interface.ItemClickListener;
import com.example.flybuy.Model.ProductItemList;
import com.example.flybuy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductItemDetailsAdapter extends RecyclerView.Adapter<ProductItemDetailsAdapter.ViewHolder> {
    private static final String Tag = "RecyclerView";
    private Context context;
    private ArrayList<ProductItemList> mdata;

    public ProductItemDetailsAdapter(Context context, ArrayList<ProductItemList> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_product_details,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(mdata.get(position).getProductName());
        holder.itemPrice.setText("$"+mdata.get(position).getProductPrice());

        Glide.with(context).load(mdata.get(position).getProductImage()).placeholder(R.drawable.dropdown).error(R.drawable.errormessage).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage,itemRating;
        TextView itemName, itemStory,itemPrice;
        FloatingActionButton cartButton,arButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.productimage);
            itemName = (TextView) itemView.findViewById(R.id.productname);
            itemPrice = (TextView) itemView.findViewById(R.id.productprice);
        }
    }
}
