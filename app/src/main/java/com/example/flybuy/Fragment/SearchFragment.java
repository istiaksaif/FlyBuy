package com.example.flybuy.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.flybuy.Adaptar.ProductItemAdapter;
import com.example.flybuy.Model.ProductItemList;
import com.example.flybuy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView productRecyclerView;
    private ProductItemAdapter itemAdapter;
    private ArrayList<ProductItemList> productItemLists=new ArrayList<>();
    private DatabaseReference searchDatabaseRef;
    private SearchView searchView;

    public SearchFragment() {
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchview);
        searchDatabaseRef = FirebaseDatabase.getInstance().getReference();
        productRecyclerView = view.findViewById(R.id.searchproductitemrecycler);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        productRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = searchDatabaseRef.child("products");
        if(query!= null){
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            ProductItemList productItem = new ProductItemList();
                            productItem.setProductImage(snapshot.child("image").getValue().toString());
                            productItem.setProductName(snapshot.child("name").getValue().toString());
                            productItem.setProductPrice(snapshot.child("price").getValue().toString());
                            productItem.setProductId(snapshot.child("productId").getValue().toString());

                            productItemLists.add(productItem);

                        }
                        itemAdapter = new ProductItemAdapter(getActivity(),productItemLists);
                        productRecyclerView.setAdapter(itemAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),"something wrong",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView !=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<ProductItemList> searchLists = new ArrayList<>();
        for(ProductItemList obj:productItemLists){
            if(obj.getProductName().toLowerCase().contains(str.toLowerCase())){
                searchLists.add(obj);
            }
        }
        itemAdapter = new ProductItemAdapter(getActivity(),searchLists);
        productRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }
}