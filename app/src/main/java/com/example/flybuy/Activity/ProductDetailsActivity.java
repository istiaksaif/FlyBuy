package com.example.flybuy.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flybuy.Adaptar.TabViewPagerAdapter;
import com.example.flybuy.Fragment.AboutFragment;
import com.example.flybuy.Fragment.FeatureFragment;
import com.example.flybuy.Model.CartItemModel;
import com.example.flybuy.Model.ProductItemList;
import com.example.flybuy.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity {

    private RecyclerView productCategoryRecycler;
    private ArrayList<ProductItemList> productItemLists;


    private TabLayout tabLayout;
    private ViewPager tabviewPager;
    private String receiverProductId,receiveImage,receiveName,receivePrice;
    private TextView pName,pPrice,addCartButton;
    private ImageView pImage,arButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.colToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftarrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapstoolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setMinimumHeight(300);


        int displayWidth = getWindowManager().getDefaultDisplay().getHeight();

//        productDatabaseRef = FirebaseDatabase.getInstance().getReference("products");


        //////////Receive ProductId From HomeActivity
        pImage = (ImageView) findViewById(R.id.productimage);
        pName = findViewById(R.id.productname);
        pPrice = findViewById(R.id.productprice);
        pImage.getLayoutParams().height = ((displayWidth/6)*5);

        receiverProductId = getIntent().getStringExtra("productid");
        receiveImage = getIntent().getStringExtra("productimg");
        receiveName = getIntent().getStringExtra("productname");
        receivePrice = getIntent().getStringExtra("productprice");
        Picasso.get().load(receiveImage).into(pImage);
        pName.setText(receiveName);
        pPrice.setText("$"+receivePrice);

//        if(getIntent()!=null){
//            receiverProductId = getIntent().getStringExtra("pId");
//            if(!receiverProductId.isEmpty()){
//                GetDataFromFirebase(receiverProductId);
//            }
//        }
//        productItemLists = new ArrayList<>();

//        NestedScrollView nestedScrollView = (NestedScrollView) findViewById (R.id.nsv);
//        nestedScrollView.setFillViewport (true);

        tabLayout = (TabLayout)findViewById(R.id.tab);
        tabviewPager = (ViewPager)findViewById(R.id.tabviewpager);
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.AddFragment(new AboutFragment(),"About");
        tabViewPagerAdapter.AddFragment(new FeatureFragment(),"Feature");
        tabViewPagerAdapter.AddFragment(new FeatureFragment(),"More Image");
        tabviewPager.setAdapter(tabViewPagerAdapter);
        tabLayout.setupWithViewPager(tabviewPager);


        addCartButton = findViewById(R.id.cartbutton);
        addCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushProductToCart();
            }
        });

    }

    private void pushProductToCart() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        databaseReference = firebaseDatabase.getReference().child("carts").child(uid);
        String cartId = uid+"_"+receiverProductId;
        CartItemModel cartItemPush = new CartItemModel(cartId,receiverProductId,receiveName,receivePrice,receivePrice,"1",receiveImage);
        databaseReference.child(cartId).setValue(cartItemPush);
        Toast.makeText(getApplicationContext(),"Item Add to Cart Successfully",Toast.LENGTH_SHORT).show();
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();

        }return true;
    }
}