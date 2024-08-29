//package com.example.digpassmaster;
//
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ManageUserActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private UserListAdapter adapter;
//    private List<User> userList;
//    private DatabaseReference usersRef;
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_user);
//
//        listView = findViewById(R.id.listView);
//        userList = new ArrayList<>();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        usersRef = databaseReference.child("User").child("UserDetails");
//        adapter = new UserListAdapter(this, userList, usersRef);
//        listView.setAdapter(adapter);
//
//        fetchAndDisplayUsers();
//    }
//
//    private void fetchAndDisplayUsers() {
//        usersRef.orderByChild("usertype").equalTo(2).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userList.clear(); // Clear the existing user list
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    String name = userSnapshot.child("Name").getValue(String.class);
//                    String email = userSnapshot.child("Email").getValue(String.class);
//                    String dept = userSnapshot.child("Dept").getValue(String.class);
//                    String post = userSnapshot.child("Post").getValue(String.class);
//                    String id = userSnapshot.getKey(); // Get user ID
//                    User user = new User(name, email, dept, post);
//                    user.setId(id);
//                    userList.add(user);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(ManageUserActivity.this, "Failed to fetch users: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Method to delete user from Firebase and local list
//    public void deleteUser(User user) {
//        userList.remove(user);
//        adapter.notifyDataSetChanged();
//
//        usersRef.child(user.getId()).removeValue()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ManageUserActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ManageUserActivity.this, "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}
package com.example.digpassmaster;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.Objects;

public class ManageUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Create adapter for ViewPager
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());

        // Add fragments to adapter
        adapter.addFragment(new Admin(), "Admin");
        adapter.addFragment(new user_fragment(), "User");

        // Set adapter to ViewPager
        viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }
}


