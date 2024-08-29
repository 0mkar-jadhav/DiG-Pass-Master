package com.example.digpassmaster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Admin extends Fragment {

    private ListView listViewAdminDetails;
    private UserAdapter AdminAdapter;
    private List<String> AdminDetailsList;

    public Admin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ListView and adapter
        listViewAdminDetails = view.findViewById(R.id.listViewAdmin);
        AdminDetailsList = new ArrayList<>();
        AdminAdapter = new UserAdapter(getContext(), AdminDetailsList);
        listViewAdminDetails.setAdapter(AdminAdapter);

        // Fetch and display user details
        fetchAndDisplayUserDetails();
    }

    private void fetchAndDisplayUserDetails() {
        // Reference to the Firebase database node where user details are stored
        DatabaseReference adminsRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails");

        // Query users with usertype 0 and 1
        adminsRef.orderByChild("usertype").equalTo(2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous user details
                AdminDetailsList.clear();

                // Iterate through the user data
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String name = userSnapshot.child("Name").getValue(String.class);
                    String email = userSnapshot.child("Email").getValue(String.class);
                    String post = userSnapshot.child("Post").getValue(String.class);
                    String dept = userSnapshot.child("Dept").getValue(String.class);

                    // Create a string representation of user details
                    String adminDetails = "Name: " + name + "\nEmail: " + email + "\nPost: " + post +"\nDepartment: "+dept;

                    // Add user details to the list
                    AdminDetailsList.add(adminDetails);
                }

                // Notify the adapter of the data change
                AdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                AdminDetailsList.clear();
                AdminDetailsList.add("Failed to fetch user details: " + databaseError.getMessage());
                AdminAdapter.notifyDataSetChanged();
            }
        });
    }

    // Adapter for ListView
    private class UserAdapter extends ArrayAdapter<String> {
        public UserAdapter(Context context, List<String> userDetailsList) {
            super(context, 0, userDetailsList);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item_1, parent, false);
            }

            // Get user details
            final String userDetails = getItem(position);

            // Populate TextView with user details
            TextView userDetailsTextView = view.findViewById(R.id.textUserDetails);
            userDetailsTextView.setText(userDetails);

            // Set up delete button click listener
            Button deleteButton = view.findViewById(R.id.buttonDelete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle delete button click
                    deleteUser(position);
                }
            });

            return view;
        }

        private void deleteUser(int position) {
            // Implement logic to delete the user at the given position
            // You'll need to get the user ID or some unique identifier to delete the user from the database
            // Once you have the ID, you can use Firebase Database APIs to delete the user
            // For example:
            // DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails").child(USER_ID);
            // userRef.removeValue();
        }
    }
}

