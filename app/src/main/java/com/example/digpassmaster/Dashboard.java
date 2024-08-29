package com.example.digpassmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dbref = FirebaseDatabase.getInstance().getReference().child("Principal");
    }
    public void onGatepassClick(View view) {
        // Create an Intent to open the GatepassReq
        Intent intent = new Intent(this, gatepassReq.class);
        startActivity(intent);
    }

    public void onUserMonitorClick(View view) {
        // Create an Intent to open the ManageUserActivity
        Intent intent = new Intent(this, ManageUserActivity.class);
        startActivity(intent);
    }
    public void onCreateAdmin(View view){
        Intent intent=new Intent(this,CreateAdmin.class );
        startActivity(intent);
    }
    public void onLogout(View view) {
        // Create an Intent to open the GuardLoginActivity
        dbref.child("Login").setValue("No");
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }


}