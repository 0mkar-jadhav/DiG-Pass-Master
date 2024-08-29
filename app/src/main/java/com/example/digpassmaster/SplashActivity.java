package com.example.digpassmaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    public static int SPLASH_TIMER = 5000;
    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dbref = FirebaseDatabase.getInstance().getReference().child("Principal");
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String log = snapshot.child("Login").getValue(String.class);
                            if(log.equals("Yes")){
                                Toast.makeText(SplashActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SplashActivity.this, Dashboard.class));
                                finish();
                            }else{
                                Intent intent = new Intent(SplashActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Intent intent = new Intent(SplashActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        }, 5000);
    }
}
