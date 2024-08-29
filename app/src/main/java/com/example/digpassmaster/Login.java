package com.example.digpassmaster;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private EditText loginemail, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    private FirebaseAuth auth;
    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginemail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        auth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference().child("Principal");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = loginemail.getText().toString();
                String password1 = loginPassword.getText().toString();
                if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(password1)) {
                    Toast.makeText(Login.this, "Please Enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    login(email1,password1);
                }
            }
        });
    }



    private void login(String email1, String password1) {
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String user =snapshot.child("User").getValue(String.class);
                    String pass =snapshot.child("Pass").getValue(String.class);

                    if (email1.equals(user) && password1.equals(pass)){
                        dbref.child("Login").setValue("Yes");
                        cratetoken();
                        Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Dashboard.class));
                        finish();
                    }
                    else
                        Toast.makeText(Login.this, "Incorrect Email and Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void cratetoken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    String token = task.getResult();
                    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("Principal").child("token").setValue(token);
                }
            }
        });
    }


}

