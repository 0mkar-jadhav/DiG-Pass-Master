//package com.example.digpassmaster;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.journeyapps.barcodescanner.BarcodeEncoder;
//
//import java.io.ByteArrayOutputStream;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//import java.util.Properties;
//
//public class gatepassReq extends AppCompatActivity {
//
//    private DatabaseReference mUserDetailsDatabase;
//
//    private ListView listView;
//    private List<GatepassRequest> requestsList;
//    private RequestAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gatepass_req);
//
//        mUserDetailsDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails");
//
//
//        listView = findViewById(R.id.listView);
//        requestsList = new ArrayList<>();
//        adapter = new RequestAdapter(requestsList);
//        listView.setAdapter(adapter);
//
//        fetchRequests();
//    }
//
//    private void fetchRequests() {
//        mUserDetailsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    fetchRequestsFromSnapshot(userSnapshot);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(gatepassReq.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void fetchRequestsFromSnapshot(DataSnapshot userSnapshot) {
//        String name = userSnapshot.child("Name").getValue(String.class);
//        String email = userSnapshot.child("Email").getValue(String.class);
//        String mobile = userSnapshot.child("MobileNo").getValue(String.class);
//
//        for (DataSnapshot requestSnapshot : userSnapshot.child("Gatepass request").getChildren()) {
//            String reason = requestSnapshot.child("Reason").getValue(String.class);
//            String leavingTime = requestSnapshot.child("LeavingTime").getValue(String.class);
//            String passNumber = requestSnapshot.child("GatePassNumber").getValue(String.class);
//            String date=requestSnapshot.child("Date").getValue(String.class);
//            String gateid= requestSnapshot.getKey().toString();// Retrieve GatePassNumber
//
//            requestsList.add(new GatepassRequest(name, email, mobile, reason, leavingTime, passNumber, date, gateid));
//        }
//        Collections.sort(requestsList, new Comparator<GatepassRequest>() {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//            @Override
//            public int compare(GatepassRequest o1, GatepassRequest o2) {
//                try {
//                    Date date1 = sdf.parse(o1.getDate().substring(6));
//                    Date date2 = sdf.parse(o2.getDate().substring(6));
//                    return date2.compareTo(date1);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    return 0;
//                }
//            }
//        });
//        adapter.notifyDataSetChanged();
//    }
//
//    // Inner class for holding gate pass request data
//    private static class GatepassRequest {
//        private String name;
//        private String gateid;
//        private String email;
//        private String mobile;
//        private String reason;
//        private String leavingTime;
//        private String passNumber;
//        private String date;
//
//        public GatepassRequest(String name, String email, String mobile, String reason, String leavingTime, String passNumber, String date,String gateid) {
//            this.name = name;
//            this.gateid = gateid;
//            this.email = email;
//            this.mobile = mobile;
//            this.reason = reason;
//            this.leavingTime = leavingTime;
//            this.passNumber= passNumber;
//            this.date=date;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public String getMobile() {
//            return mobile;
//        }
//
//        public String getReason() {
//            return reason;
//        }
//
//        public String getLeavingTime() {
//            return leavingTime;
//        }
//
//        public String getPassNumber() {
//            return passNumber;
//        }
//        public String getDate(){
//            return date;
//        }
//
//        public String getGateid() {return gateid;}
//    }
//
//    // Adapter for ListView
//    private class RequestAdapter extends ArrayAdapter<GatepassRequest> {
//        public RequestAdapter(List<GatepassRequest> requests) {
//            super(gatepassReq.this, 0, requests);
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            View view = convertView;
//            if (view == null) {
//                view = LayoutInflater.from(getContext()).inflate(R.layout.request_item, parent, false);
//            }
//
//            // Get the current gate pass request
//            GatepassRequest request = getItem(position);
//
//            TextView gatepassNumberTextView = view.findViewById(R.id.textPassNumber);
//            gatepassNumberTextView.setText(request.getPassNumber());
//
//            TextView dateTextView = view.findViewById(R.id.textDate);
//            dateTextView.setText(request.getDate());
//
//            // Populate the TextViews with data
//            TextView nameTextView = view.findViewById(R.id.textName);
//            nameTextView.setText("Name: " + request.getName());
//
//            TextView emailTextView = view.findViewById(R.id.textEmail);
//            emailTextView.setText("Email: " + request.getEmail());
//
//            TextView mobileTextView = view.findViewById(R.id.textMobile);
//            mobileTextView.setText("Mobile: " + request.getMobile());
//
//            TextView reasonTextView = view.findViewById(R.id.textReason);
//            reasonTextView.setText("Reason: " + request.getReason());
//
//            TextView leavingTimeTextView = view.findViewById(R.id.textLeavingTime);
//            leavingTimeTextView.setText("Leaving Time: " + request.getLeavingTime());
//
//
//
//            // Set up accept button
//            Button acceptButton = view.findViewById(R.id.buttonAccept);
//            acceptButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Launch QRCodeActivity with user details
//                    launchQRCodeActivity(request.getPassNumber(), request.getName(), request.getEmail(), request.getMobile(), request.getReason(), request.getLeavingTime(), request.getDate(),request.gateid);
//                }
//            });
//
//            // Set up reject button
//            Button rejectButton = view.findViewById(R.id.buttonReject);
//            rejectButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Remove the request from the list
//                    requestsList.remove(request);
//                    // Notify the adapter of the data change
//                    adapter.notifyDataSetChanged();
//
//                    // Show a Toast message
//                    Toast.makeText(getContext(), "Request rejected", Toast.LENGTH_SHORT).show();
//
//                    // Send local notification
//                    sendNotification(request.getName());
//                }
//            });
//
//
//            return view;
//        }
//    }
//
//    private void launchQRCodeActivity(String passNumber,String name, String email, String mobile,String reason,String leavingTime, String date,String gateid) {
//        Intent intent = new Intent(this, QRCodeActivity.class);
//        intent.putExtra("pass number", passNumber);
//        intent.putExtra("name", name);
//        intent.putExtra("email", email);
//        intent.putExtra("mobile", mobile);
//        intent.putExtra("reason",reason);
//        intent.putExtra("leavingTime",leavingTime);
//        intent.putExtra("date", date);
//        intent.putExtra("gateid",gateid);
//        startActivity(intent);
//    }
//
//    private void sendNotification(String name) {
//        // Create a notification manager
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Define notification properties
//        String channelId = "reject_notification_channel";
//        String channelName = "Reject Notifications";
//
//        // Create a notification channel for API level 26+
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//            channel.setDescription("Notifications for rejected requests");
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        // Build the notification
//        Notification.Builder builder = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder = new Notification.Builder(this, channelId)
//                    .setSmallIcon(R.drawable.logo)
//                    .setContentTitle("Request Rejected")
//                    .setContentText("Request from " + name + " has been rejected")
//                    .setAutoCancel(true);
//        }
//
//        // Show the notification
//        notificationManager.notify(0, builder.build());
//    }
//}
package com.example.digpassmaster;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class gatepassReq extends AppCompatActivity {

    private DatabaseReference mUserDetailsDatabase;
    private ListView listView;
    private List<GatepassRequest> requestsList;
    private RequestAdapter adapter;
    Loading_alert loader = new Loading_alert(gatepassReq.this);

    private int admintype;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatepass_req);

        if (getIntent().getExtras()!=null){
            admintype=getIntent().getIntExtra("usertype",2);

        }

        mUserDetailsDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails");
        listView = findViewById(R.id.listView);
        requestsList = new ArrayList<>();
        adapter = new RequestAdapter(requestsList);
        listView.setAdapter(adapter);
        fetchRequests();


    }

    private void fetchRequests() {
        mUserDetailsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    fetchRequestsFromSnapshot(userSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(gatepassReq.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchRequestsFromSnapshot(DataSnapshot userSnapshot) {
        if (userSnapshot.exists()) {
            String uid = userSnapshot.getKey();
            String name = userSnapshot.child("Name").getValue(String.class);
            String email = userSnapshot.child("Email").getValue(String.class);
            String mobile = userSnapshot.child("MobileNo").getValue(String.class);
            String dept = userSnapshot.child("Dept").getValue(String.class);



            for (DataSnapshot requestSnapshot : userSnapshot.child("Gatepass").getChildren()) {
                if (requestSnapshot.exists()) {
                    int app = requestSnapshot.child("Approval").getValue(Integer.class);
                    if (app == 3) {
                        String gatepassid = requestSnapshot.getKey();
                        String reason = requestSnapshot.child("Reason").getValue(String.class);
                        String leavingTime = requestSnapshot.child("LeavingTime").getValue(String.class);
                        String passNumber = requestSnapshot.child("GatePassNumber").getValue(String.class); // Retrieve GatePassNumber

                        requestsList.add(new GatepassRequest(uid, gatepassid, name, email, mobile, reason, leavingTime, passNumber));

                    }
                }else
                    Toast.makeText(this, "No gatepass request", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
        else
            Toast.makeText(this, "There are no valid user", Toast.LENGTH_SHORT).show();
    }

    // Inner class for holding gate pass request data
    private static class GatepassRequest {

        private String uid;
        private String gatepassid;
        private String name;
        private String email;
        private String mobile;
        private String reason;
        private String leavingTime;
        private String passNumber;

        public GatepassRequest(String uid, String gatepassid, String name, String email, String mobile, String reason, String leavingTime, String passNumber) {
            this.uid = uid;
            this.gatepassid = gatepassid;
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.reason = reason;
            this.leavingTime = leavingTime;
            this.passNumber= passNumber;
        }

        public String getUid(){return uid;}
        public String getGatepassid(){return gatepassid;}

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getMobile() {
            return mobile;
        }

        public String getReason() {
            return reason;
        }

        public String getLeavingTime() {
            return leavingTime;
        }

        public String getPassNumber() {
            return passNumber;
        }
    }

    // Adapter for ListView
    private class RequestAdapter extends ArrayAdapter<GatepassRequest> {
        public RequestAdapter(List<GatepassRequest> requests) {
            super(gatepassReq.this, 0, requests);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.request_item, parent, false);
            }

            loader.startalertdialog();
            // Get the current gate pass request
            GatepassRequest request = getItem(position);

            // Populate the TextViews with data
            TextView nameTextView = view.findViewById(R.id.textName);
            nameTextView.setText("Name: " + request.getName());

            TextView emailTextView = view.findViewById(R.id.textEmail);
            emailTextView.setText("Email: " + request.getEmail());

            TextView mobileTextView = view.findViewById(R.id.textMobile);
            mobileTextView.setText("Mobile: " + request.getMobile());

            TextView reasonTextView = view.findViewById(R.id.textReason);
            reasonTextView.setText("Reason: " + request.getReason());

            TextView leavingTimeTextView = view.findViewById(R.id.textLeavingTime);
            leavingTimeTextView.setText("Leaving Time: " + request.getLeavingTime());

            TextView gatepassNumberTextView = view.findViewById(R.id.textPassNumber);
            gatepassNumberTextView.setText(request.getPassNumber());

            // Set up accept button
            Button acceptButton = view.findViewById(R.id.buttonAccept);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch QRCodeActivity with user details
//                    launchQRCodeActivity(request.getName(), request.getEmail(), request.getMobile());

                    String gatepassid = request.getGatepassid();
                    uid  = request.getUid();

                    mUserDetailsDatabase.child(uid).child("Gatepass").child(gatepassid).child("Approval").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int app= snapshot.getValue(Integer.class);
                            app++;
                            mUserDetailsDatabase.child(uid).child("Gatepass").child(gatepassid).child("Approval").setValue(app);
                            sendNotification(request.getName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    finish();
                    startActivity(getIntent());
                }
            });

            // Set up reject button
            Button rejectButton = view.findViewById(R.id.buttonReject);
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String gatepassid = request.getGatepassid();
                    String uid = request.getUid();
                    //remove request
                    mUserDetailsDatabase.child(uid).child("Gatepass").child(gatepassid).removeValue();
                    // Show a Toast message
                    Toast.makeText(getContext(), "Request rejected", Toast.LENGTH_SHORT).show();



                    finish();
                    startActivity(getIntent());



                    // Send local notification
                    sendNotification(request.getName());
                }
            });
            loader.closealertdialog();

            return view;
        }
    }


    // Method to generate a QR code using ZXing library


    private void sendNotification(String applicantName) {

        mUserDetailsDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails").child(uid);

        mUserDetailsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String token = snapshot.child("token").getValue().toString();

                    try {
                        JSONObject jsonObject= new JSONObject();


                        JSONObject notification = new JSONObject();
                        notification.put("title","Gate-pass Request");
                        notification.put("body","New Gate-pass request from " + applicantName);

                        JSONObject dataobj = new JSONObject();
                        dataobj.put("usertype",3);

                        jsonObject.put("to",token);
                        jsonObject.put("data",dataobj);
                        jsonObject.put("notification",notification);


                        callAPI(jsonObject);
//                        Toast.makeText(student_application.this, "Request send successfully", Toast.LENGTH_SHORT).show();



                    }catch (Exception e){
                        Toast.makeText(gatepassReq.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                        Log.d("Exception",e.toString());

                    }
                }else {
                    Toast.makeText(gatepassReq.this, "Admin is offline", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(gatepassReq.this, "no notification", Toast.LENGTH_SHORT).show();

            }
        });


    }

    void callAPI(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization","key="+R.string.firebsekey)
                .addHeader("Content-Type","application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}
