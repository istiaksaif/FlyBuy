package com.example.flybuy.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.flybuy.Adaptar.CartItemAdapter;
import com.example.flybuy.Adaptar.ProductItemAdapter;
import com.example.flybuy.Interface.IFirebaseLoad;
import com.example.flybuy.Model.CartItemModel;
import com.example.flybuy.Model.ProductItemList;
import com.example.flybuy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button checkOutButton;
    private CartItemAdapter itemAdapter;
    private ArrayList<CartItemModel> cartItemList;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String uid;
    private TextView subtotal,deliveryCharge,totalPrice;
    private int subTotal=0,extraCharge=1,finalTotalPrice=0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = view.findViewById(R.id.cartrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("carts");

        GetDataFromFirebase();
        checkOutButton = view.findViewById(R.id.checkoutbutton);

        subtotal = view.findViewById(R.id.subtotalprice);
        deliveryCharge = view.findViewById(R.id.deliveryprice);
        totalPrice = view.findViewById(R.id.producttotalprice);
        caculatateprice();
    }

    private void caculatateprice(){
        String cccex= uid+"_"+"calculate";
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
                int itemTotalPrice = 0;
                    for (DataSnapshot dataSnapshot : snapshot.child(uid).getChildren()) {
                        Map<String,Object> tp = (Map<String, Object>)dataSnapshot.getValue();
                        Object price = tp.get("itemtotalprice");
                        itemTotalPrice = Integer.parseInt(String.valueOf(price));

                        subTotal += itemTotalPrice;
                        finalTotalPrice = subTotal + extraCharge;
//                        HashMap<String, Object> totalresult = new HashMap<>();
//                        totalresult.put("subtotal", subTotal);
//                        totalresult.put("totalprice", finalTotalPrice);
//
//                        databaseReference.child(cccex).updateChildren(totalresult)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                    }
//                                });
                        subtotal.setText("$"+String.valueOf(subTotal));
                        deliveryCharge.setText("$"+String.valueOf(extraCharge));
                        totalPrice.setText("$"+String.valueOf(finalTotalPrice));
                    }
//                }
//                subtotal.setText(String.valueOf(snapshot.child(cccex).child("subtotal").getValue().toString()));
//                totalPrice.setText(String.valueOf(snapshot.child(cccex).child("totalprice").getValue().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetDataFromFirebase() {

        Query query = databaseReference.child(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot  snapshot: dataSnapshot.getChildren()){
                    CartItemModel productItem = new CartItemModel();
                    productItem.setImage(snapshot.child("image").getValue().toString());
                    productItem.setName(snapshot.child("name").getValue().toString());
                    productItem.setPrice(snapshot.child("price").getValue().toString());
                    productItem.setProductId(snapshot.child("productId").getValue().toString());
                    productItem.setQuantity(snapshot.child("quantity").getValue().toString());
                    productItem.setItemtotalprice(snapshot.child("itemtotalprice").getValue().toString());

                    cartItemList.add(productItem);
                }
                itemAdapter = new CartItemAdapter(getActivity(),cartItemList);
                recyclerView.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void ClearAll(){
        if (cartItemList!=null){
            cartItemList.clear();
            if (itemAdapter!=null){
                itemAdapter.notifyDataSetChanged();
            }
        }
        cartItemList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }
}