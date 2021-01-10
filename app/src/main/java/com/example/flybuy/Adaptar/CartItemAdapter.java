package com.example.flybuy.Adaptar;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flybuy.Model.CartItemModel;
import com.example.flybuy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private int intquantity,itemTotalPrice,itemprice,subTotal,extraCharge=1,finalTotalPrice;
    String cartId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("carts").child(cartId);
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("carts");
    private Context context;
    private ArrayList<CartItemModel> cartItem;
    public CartItemAdapter(Context context, ArrayList<CartItemModel> cartItem) {
        this.context = context;
        this.cartItem = cartItem;


    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_card, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        String ccc= cartId+"_"+cartItem.get(position).getProductId();
//        String cccex= cartId+"_"+"calculate";

        holder.itemname.setText(cartItem.get(position).getName());
        holder.itemprice.setText("$"+cartItem.get(position).getPrice());
        Glide.with(context).load(cartItem.get(position).getImage()).into(holder.itemimage);
        holder.quantityitem.setText(cartItem.get(position).getQuantity());
        holder.itemtotalprice.setText("$"+cartItem.get(position).getItemtotalprice());

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = cartItem.get(position).getQuantity();
                intquantity =Integer.parseInt(quantity.toString());

                String itemPrice = cartItem.get(position).getPrice();
                itemprice =Integer.parseInt(itemPrice.toString());

                if(intquantity>0){
                    intquantity--;
                }
                itemTotalPrice = intquantity*itemprice;
                HashMap<String, Object> result = new HashMap<>();
                result.put("quantity", intquantity);
                result.put("itemtotalprice",itemTotalPrice);

                databaseReference.child(ccc).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });

//                subTotal = subTotal+itemTotalPrice;
//                finalTotalPrice=subTotal+extraCharge;
//                HashMap<String, Object> totalresult = new HashMap<>();
//                totalresult.put("subtotal", subTotal);
//                totalresult.put("totalprice",finalTotalPrice);
//
//                databaseReference1.child(cccex).updateChildren(totalresult)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                            }
//                        });
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = cartItem.get(position).getQuantity();
                intquantity =Integer.parseInt(quantity.toString());

                String itemPrice = cartItem.get(position).getPrice();
                itemprice =Integer.parseInt(itemPrice.toString());

                intquantity++;
                itemTotalPrice = intquantity*itemprice;
                HashMap<String, Object> result = new HashMap<>();
                result.put("quantity", intquantity);
                result.put("itemtotalprice",itemTotalPrice);

                databaseReference.child(ccc).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
//                subTotal = subTotal+itemTotalPrice;
//                finalTotalPrice=subTotal+extraCharge;
//                HashMap<String, Object>totalresult = new HashMap<>();
//                totalresult.put("subtotal", subTotal);
//                totalresult.put("totalprice",finalTotalPrice);
//
//                databaseReference1.child(cccex).updateChildren(totalresult)
//                        .addOnSuccessListener(new OnSuccessListener<Void>(){
//                            @Override
//                            public void onSuccess(Void aVoid){
//
//                            }
//                        });
            }
        });
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(ccc).removeValue();
                Toast.makeText(context,"Item Removed from Cart",Toast.LENGTH_SHORT).show();
                cartItem.remove(position);
                notifyItemChanged(position);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return cartItem.size();
    }

    public static final class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemname, itemprice, quantityitem, itemtotalprice;
        private ImageView itemimage, minus, plus, cross;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.product_name);
            itemprice = itemView.findViewById(R.id.product_price);
            itemimage = itemView.findViewById(R.id.product_image);
            quantityitem = itemView.findViewById(R.id.itemcount);
            itemtotalprice = itemView.findViewById(R.id.itemtotalprice);
            minus = itemView.findViewById(R.id.itemminus);
            plus = itemView.findViewById(R.id.itemplus);
            cross = itemView.findViewById(R.id.itemdelete);
        }
    }
}
