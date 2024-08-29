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

public class user_fragment extends Fragment {

    private ListView listViewUserDetails;
    private UserAdapter userAdapter;
    private List<String> userDetailsList;

    public user_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ListView and adapter
        listViewUserDetails = view.findViewById(R.id.listViewUser);
        userDetailsList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), userDetailsList);
        listViewUserDetails.setAdapter(userAdapter);

        // Fetch and display user details
        fetchAndDisplayUserDetails();
    }

    private void fetchAndDisplayUserDetails() {
        // Reference to the Firebase database node where user details are stored
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails");

        // Query users with usertype 0 and 1
        usersRef.orderByChild("usertype").startAt(0).endAt(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous user details
                userDetailsList.clear();

                // Iterate through the user data
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String name = userSnapshot.child("Name").getValue(String.class);
                    String email = userSnapshot.child("Email").getValue(String.class);
                    String branch = userSnapshot.child("Branch").getValue(String.class);

                    // Create a string representation of user details
                    String userDetails = "Name: " + name + "\nEmail: " + email + "\nBranch: " + branch;

                    // Add user details to the list
                    userDetailsList.add(userDetails);
                }

                // Notify the adapter of the data change
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                userDetailsList.clear();
                userDetailsList.add("Failed to fetch user details: " + databaseError.getMessage());
                userAdapter.notifyDataSetChanged();
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

