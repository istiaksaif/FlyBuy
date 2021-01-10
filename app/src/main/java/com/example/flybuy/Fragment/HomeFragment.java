package com.example.flybuy.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.flybuy.Adaptar.ProductItemAdapter;
import com.example.flybuy.Adaptar.SliderAdapterimage;
import com.example.flybuy.DepthPageTransformer;
import com.example.flybuy.Interface.IFirebaseLoad;
import com.example.flybuy.Model.SliderImageModel;
import com.example.flybuy.Model.ProductItemList;
import com.example.flybuy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment implements IFirebaseLoad{

    private ViewPager sliderViewPager;
    private LinearLayout sliderDots;
    private int dotsCount;
    private ImageView[] dots;
    private int currentPosition=0;
    private SliderAdapterimage sliderAdapterimage;
    private List<SliderImageModel> sliderList = new ArrayList<>();
    private Timer timer;

    private RecyclerView productRecyclerView;
    private ProductItemAdapter itemAdapter;
    private ArrayList<ProductItemList> productItemLists;
    private DatabaseReference productDatabaseRef,sliderDatabaseRef;
    private IFirebaseLoad iFirebaseLoad;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sliderDatabaseRef = FirebaseDatabase.getInstance().getReference();
        iFirebaseLoad = this;

        loadSlider();
        int displayWidth = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        sliderViewPager = (ViewPager)view.findViewById(R.id.sliderviewpager);
        sliderViewPager.setPageTransformer(true,new DepthPageTransformer());
        sliderViewPager.getLayoutParams().height = ((displayWidth/11)*4);

        sliderDots = (LinearLayout) view.findViewById(R.id.slide_dot);


        MyTimerTask();

        //products add part
        productRecyclerView = view.findViewById(R.id.productitemrecycler);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        productRecyclerView.setHasFixedSize(true);

        productDatabaseRef = FirebaseDatabase.getInstance().getReference();
        productItemLists = new ArrayList<>();

        GetDataFromFirebase();
        ClearAll();

    }

    private void loadSlider() {
        Query query = sliderDatabaseRef.child("bannerSlider");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot  snapshot: dataSnapshot.getChildren()){
                    SliderImageModel productItem = new SliderImageModel();
                    productItem.setImageUrl(snapshot.child("image").getValue().toString());

                    sliderList.add(productItem);
                }
                sliderAdapterimage = new SliderAdapterimage(getApplicationContext(),sliderList);
                sliderViewPager.setAdapter(sliderAdapterimage);
                sliderAdapterimage.notifyDataSetChanged();
                iFirebaseLoad.onFirebaseLoadSuccess(sliderList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iFirebaseLoad.onFirebaseLoadFailed(error.getMessage());
            }
        });
    }

    private void GetDataFromFirebase() {

        Query query = productDatabaseRef.child("products");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot  snapshot: dataSnapshot.getChildren()){
                    String productkey = snapshot.getKey();
                    ProductItemList productItem = new ProductItemList();
                    productItem.setProductImage(snapshot.child("image").getValue().toString());
                    productItem.setProductName(snapshot.child("name").getValue().toString());
                    productItem.setProductPrice(snapshot.child("price").getValue().toString());
                    productItem.setProductId(snapshot.child("productId").getValue().toString());

                    productItemLists.add(productItem);
                }
                itemAdapter = new ProductItemAdapter(getContext(),productItemLists);
                productRecyclerView.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ClearAll(){
        if (productItemLists!=null){
            productItemLists.clear();
            if (itemAdapter!=null){
                itemAdapter.notifyDataSetChanged();
            }
        }
        productItemLists = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
    
    private void MyTimerTask(){

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(currentPosition==sliderList.size())
                    currentPosition=0;
                sliderViewPager.setCurrentItem(currentPosition++,true);

            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },2000,4000);
    }

    @Override
    public void onFirebaseLoadSuccess(List<SliderImageModel> sliderList) {
        sliderAdapterimage = new SliderAdapterimage(getApplicationContext(),sliderList);
        sliderViewPager.setAdapter(sliderAdapterimage);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(getContext(),""+message,Toast.LENGTH_SHORT).show();
    }

}