package com.example.flybuy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.flybuy.Model.User;
import com.example.flybuy.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Arrays;


public class LogInActivity extends AppCompatActivity{

    private TextInputEditText email,phone,password,popup_email;
    private Button logInButton,forgotButton;
    private TextView signup;
    private MaterialTextView forgotpassword;
    private GoogleSignInClient googleSignInClient;
    private FloatingActionButton signgoogle,signfb;
    private static  int RC_SIGN_IN =2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private CallbackManager callbackManager;
    private static final String TAG = "Authentication";
    private FirebaseAuth.AuthStateListener authStateListener;
    float v=0;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private LottieAnimationView cross;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        //signin by email
        email = findViewById(R.id.emaillogin);
        password = findViewById(R.id.passlogin);
        logInButton = findViewById(R.id.login_btn);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();


                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(LogInActivity.this, "please enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(LogInActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                    mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkIfSeller();
                            } else {
                                Toast.makeText(LogInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LogInActivity.this, LogInActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
        });

        //signin by google part
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        signgoogle = findViewById(R.id.sign_google);
        signgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //signin by facebook part
        FacebookSdk.sdkInitialize(LogInActivity.this);
        signfb = findViewById(R.id.sign_facebook);
        callbackManager = CallbackManager.Factory.create();
        signfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LogInActivity.this,
                        Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG,"onSuccess"+loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG,"OnCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG,"OnError"+error);
                    }
                });
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!= null){
                   pushData();
                }
                else {
                    updateUI(null);
                }
            }
        };


        //intent registration activity
        signup =(TextView) findViewById(R.id.registeractivity);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,RegistationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //forgot_password
        forgotpassword = findViewById(R.id.forgotpass);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createpopupDiaglog();
            }
        });


        //animation part
        signgoogle.setTranslationY(300);
        signfb.setTranslationY(300);
        signfb.setAlpha(v);
        signgoogle.setAlpha(v);

        signgoogle.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        signfb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();

    }

    private void checkIfSeller() {
        FirebaseUser user = mAuth.getCurrentUser();
        Query query = databaseReference.child(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("isUser").getValue(String.class).equals("Seller")) {
                        Intent intent = new Intent(LogInActivity.this, SellerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (snapshot.child("isUser").getValue(String.class).equals("User")) {
                        Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
//                        intent.putExtra("email",user.getEmail());
                        startActivity(intent);
                        finish();
                    }
//                    else{
//                        Toast.makeText(LogInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LogInActivity.this, LogInActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pushData();
                } else {
                    Toast.makeText(LogInActivity.this, "Login Failed"+task.getException()
                            .toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this,LogInActivity.class);
                    startActivity(intent);
                    updateUI(null);
                }
            }
        });
    }
    
    protected void onStart(){

        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn() {
        Intent signInintent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInintent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("Sign in failed", e.toString());
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().getAdditionalUserInfo().isNewUser()) {
                                pushData();
                            }else{
                                Intent intent = new Intent(LogInActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LogInActivity.this,"Login Failed"
                                    +task.getException().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this,LogInActivity.class);
                            startActivity(intent);
                            updateUI(null);
                        }
                    }
                });
    }

    private void pushData() {
        FirebaseUser user = mAuth.getCurrentUser();
        String pname = user.getDisplayName();
        String pEmail = user.getEmail();
        String isUser = "User";
        User userhelp = new User(pname,pEmail,null,isUser,"","","");
        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(userhelp);
        updateUI(user);
        finish();
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LogInActivity.this,HomeActivity.class);
        intent.putExtra("email",user.getEmail());
        startActivity(intent);
        finish();
    }

    public void createpopupDiaglog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup,null);
        popup_email = (TextInputEditText)contactPopupView.findViewById(R.id.emailforgot);
        forgotButton = (Button) contactPopupView.findViewById(R.id.forgot_btn);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword();
            }
        });

        cross = (LottieAnimationView) contactPopupView.findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void forgotpassword() {
        if(popup_email.getText().toString().equals("")){
            popup_email.setError("please fill");
        }
        else {
          mAuth.sendPasswordResetEmail(popup_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     Toast.makeText(LogInActivity.this, "please check your email and reset password", Toast.LENGTH_SHORT).show();
                     dialog.dismiss();
                 }
                 else{
                     Toast.makeText(LogInActivity.this, "Unsuccessful"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                     dialog.dismiss();
                 }
              }
          });
        }
    }

}