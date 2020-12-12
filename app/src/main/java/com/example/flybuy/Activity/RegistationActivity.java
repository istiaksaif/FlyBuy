package com.example.flybuy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flybuy.Model.User;
import com.example.flybuy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegistationActivity extends AppCompatActivity {

    private TextView signin;
    private TextInputEditText fullName,email,phone,password,passwordRepeat;
    private Button registrationButton;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        fullName = findViewById(R.id.name);
        email = findViewById(R.id.eamil);
        password = findViewById(R.id.pass);
        passwordRepeat = findViewById(R.id.passretype);
        registrationButton = findViewById(R.id.reg_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
                }
        });

        //intent Login page
        signin = findViewById(R.id.signinactivity);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistationActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Register() {
        String FullName = fullName.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Password_re = passwordRepeat.getText().toString();

        if (TextUtils.isEmpty(FullName)){
            Toast.makeText(RegistationActivity.this, "please enter your Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(Email)){
            Toast.makeText(RegistationActivity.this, "please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(Password)){
            Toast.makeText(RegistationActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(Password_re)){
            Toast.makeText(RegistationActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Password.equals(Password_re)){
            passwordRepeat.setError("password not match");
            return;
        }
        else if (Password.length()<8){
            Toast.makeText(RegistationActivity.this, "password week", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!isVallidEmail(Email)){
            email.setError("Envalid email");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(RegistationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User userhelp = new User(FullName,Email,Password);
                            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userhelp);
                            Toast.makeText(RegistationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistationActivity.this,ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegistationActivity.this, "Authentication Failed "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistationActivity.this,RegistationActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    private Boolean isVallidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}