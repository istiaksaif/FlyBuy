package com.example.flybuy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.flybuy.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private TextView name,email,phone;
    private Button logoutButton;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);


        email = findViewById(R.id.eamil);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.logout:
                        signOut();
                        break;
                }
            }
        });


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!= null){
            String pname = account.getDisplayName();
            String pEmail = account.getEmail();
            String pPhone = account.getId();

            name.setText(pname);
            email.setText(pEmail);
            phone.setText(pPhone);
        }
    }
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        googleSignInClient.signOut();
        Intent intent1 = new Intent(ProfileActivity.this,LogInActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();
    }
}