package com.example.digpassmaster;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {

    private User userToDelete;
    private Context context;
    private List<User> userList;
    private DatabaseReference databaseReference;

    public UserListAdapter(@NonNull Context context, List<User> userList, DatabaseReference databaseReference) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
        this.databaseReference = databaseReference;
        this.userToDelete = userToDelete;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        }

        User currentUser = userList.get(position);

        TextView nameTextView = listItem.findViewById(R.id.nameTextView);
        nameTextView.setText("Name: " + currentUser.getName());

        TextView emailTextView = listItem.findViewById(R.id.emailTextView);
        emailTextView.setText("Email id: " + currentUser.getEmail());

        TextView deptTextView = listItem.findViewById(R.id.deptTextView);
        deptTextView.setText("Department: " + currentUser.getDept());

        TextView postTextView = listItem.findViewById(R.id.postTextView);
        postTextView.setText("Post: " + currentUser.getPost());

        Button buttonDelete = listItem.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the user from the list and database
                                deleteUser(currentUser);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });


        return listItem;
    }
    // Add this method to the UserListAdapter class
    private void deleteUser(User user) {
        userList.remove(user);
        notifyDataSetChanged();

        // Call the deleteUser method of the ManageUserActivity
//        if (context instanceof ManageUserActivity) {
//            ((ManageUserActivity) context).deleteUser(user);
//        }
    }

    }

