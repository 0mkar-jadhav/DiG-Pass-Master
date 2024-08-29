
package com.example.digpassmaster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class CreateAdmin extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText, mobileNoEditText;
    private Spinner deptSpinner, postSpinner, divSpinner,yearSpinner;
    private Button signUpButton;
    private ImageView photo;
    private final int GALLERY_REQ_CODE = 1000;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;

    private FloatingActionButton uplbtn;
    private FirebaseAuth mAuth;
    Loading_alert loader = new Loading_alert(CreateAdmin.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initializeViews();

        // Set up spinner adapters
        setUpSpinnerAdapters();

        // Set click listeners
        setClickListeners();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.signup_name);
        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        mobileNoEditText = findViewById(R.id.signup_no);
        deptSpinner = findViewById(R.id.dept);
        postSpinner = findViewById(R.id.post);
        divSpinner = findViewById(R.id.div);
        yearSpinner = findViewById(R.id.year);
        signUpButton = findViewById(R.id.signup_button);
        uplbtn = findViewById(R.id.uplphoto);
        photo = findViewById(R.id.photo);
    }

    private void setUpSpinnerAdapters() {
        ArrayAdapter<CharSequence> d_adapter = ArrayAdapter.createFromResource(
                this,
                R.array.dept,
                android.R.layout.simple_spinner_item
        );
        d_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptSpinner.setAdapter(d_adapter);

        ArrayAdapter<CharSequence> p_adapter = ArrayAdapter.createFromResource(
                this,
                R.array.post,
                android.R.layout.simple_spinner_item
        );
        p_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postSpinner.setAdapter(p_adapter);

        ArrayAdapter<CharSequence> div_adapter = ArrayAdapter.createFromResource(
                this,
                R.array.div,
                android.R.layout.simple_spinner_item
        );
        div_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divSpinner.setAdapter(div_adapter);

        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(
                this,
                R.array.year,
                android.R.layout.simple_spinner_item
        );
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(year_adapter);
    }

    private void setClickListeners() {
        postSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                String post = item.toString();
                if (post.equals("Class Teacher")) {
                    divSpinner.setVisibility(View.VISIBLE);
                    yearSpinner.setVisibility(View.VISIBLE);
                } else {
                    divSpinner.setVisibility(View.GONE);
                    yearSpinner.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // Set click listeners
        photo.setOnClickListener(v -> openGallery());
        signUpButton.setOnClickListener(view -> signUp());
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, GALLERY_REQ_CODE);
    }

    private void signUp() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String post = postSpinner.getSelectedItem().toString();
        String dept = deptSpinner.getSelectedItem().toString();
        String mobileNo = mobileNoEditText.getText().toString();
        String div = divSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters long");
            passwordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mobileNo) || !Patterns.PHONE.matcher(mobileNo).matches()) {
            mobileNoEditText.setError("Enter a valid mobile number");
            mobileNoEditText.requestFocus();
            return;
        }

        if (dept.equals("Select Department")) {
            Toast.makeText(this, "Please select a department", Toast.LENGTH_SHORT).show();
            return;
        }

        if (post.equals("Select Post")) {
            Toast.makeText(this, "Please select a post", Toast.LENGTH_SHORT).show();
            return;
        }

        if (post.equals("Class Teacher") && (div.equals("Select Division") || year.equals("Select Year"))) {
            Toast.makeText(this, "Please select a division", Toast.LENGTH_SHORT).show();
            return;
        }

        // All fields are valid, proceed with signup
        checkAdminExists(email, password, name, post, dept, mobileNo, div,year);
    }

    private void checkAdminExists(String email, String password, String name, String post, String dept, String mobileNo, String div,String year) {
        DatabaseReference deptRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails");
        deptRef.orderByChild("Dept").equalTo(dept).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean adminExists = false;
                    boolean classTeacherExists = false;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.exists()) {
                            String existingPost = snapshot.child("Post").getValue(String.class);
                            String existingDiv = snapshot.child("Div").getValue(String.class);
                            String existingYear = snapshot.child("Year").getValue(String.class);

                            if (existingPost!=null && existingPost.equals("HOD")) {
                                // HoD already exists for this department
                                adminExists = true;
                            }

                            if(existingPost!=null && existingDiv!=null) {
                                if (existingPost.equals("Class Teacher") && existingDiv.equals(div) && existingYear.equals(year)) {
                                    // Class teacher already exists for this division
                                    classTeacherExists = true;
                                }
                            }
                        }
                    }

                    if (!adminExists) {
                        // No admin found for this department, proceed with creating a new admin as HoD
                        uploadImageAndData(email, password, name, post, dept, mobileNo, div,year);
                    } else if (!classTeacherExists && post.equals("Class Teacher")) {
                        // Admin exists but no class teacher found for this division, proceed with creating a new admin as class teacher
                        uploadImageAndData(email, password, name, post, dept, mobileNo, div,year);
                    } else {
                        // Admin already exists for this department or class teacher exists for this division
                        if (post.equals("HOD")) {
                            Toast.makeText(CreateAdmin.this, "Admin already exists for this department", Toast.LENGTH_SHORT).show();
                        } else if (post.equals("Class Teacher")) {
                            Toast.makeText(CreateAdmin.this, "Class Teacher already exists for division: " + div, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if (post.equals("HOD")) {
                        uploadImageAndData(email, password, name, post, dept, mobileNo, div,year);
                    } else if (post.equals("Class Teacher")) {
                        uploadImageAndData(email, password, name, post, dept, mobileNo, div,year);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(CreateAdmin.this, "Failed to check admin existence: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImageAndData(String email, String password, String name, String post, String dept, String mobileNo, String div,String year) {
        loader.startalertdialog();
        if (imageUri != null) {
            StorageReference ref = storageReference.child("images/" + name + "_profile.jpg");
            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle success
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            saveUserData(email, password, name, post, dept, mobileNo, imageUrl, div,year);
                        });
                    })
                    .addOnFailureListener(e -> {Toast.makeText(CreateAdmin.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                loader.closealertdialog();});
        } else {
            Toast.makeText(CreateAdmin.this, "Please select an image", Toast.LENGTH_SHORT).show();
            loader.closealertdialog();
        }
    }

    private void saveUserData(String email, String password, String name, String post, String dept, String mobileNo, String imageUrl, String div,String year) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAdmin.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        currentUser.sendEmailVerification().addOnCompleteListener(CreateAdmin.this, task1 -> {
                            if (task1.isSuccessful()) {
                                if (currentUser != null) {
                                    String userId = currentUser.getUid();
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child("UserDetails").child(userId);
                                    HashMap<String, Object> userData = new HashMap<>();
                                    userData.put("Email", email);
                                    userData.put("Name", name);
                                    userData.put("Dept", dept);
                                    userData.put("Post", post);
                                    userData.put("MobileNo", mobileNo);
                                    userData.put("ProfileImageUrl", imageUrl);
                                    userData.put("Password", password);
                                    if (post.equals("Class Teacher")) {
                                        userData.put("Div", div);
                                        userData.put("Year",year);
                                        userData.put("usertype", 2);
                                    } else
                                        userData.put("usertype", 3);
                                    userRef.setValue(userData)
                                            .addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {
                                                    Toast.makeText(CreateAdmin.this, "Admin Created successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(CreateAdmin.this, Dashboard.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(CreateAdmin.this, "Failed to save data: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    currentUser.delete();
                                                }
                                            });

                                }
                            } else {
                                Toast.makeText(CreateAdmin.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            loader.closealertdialog();
                        });
                    }

                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE && data != null) {
            imageUri = data.getData();
            photo.setImageURI(imageUri);
            photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photo.setClipToOutline(true);
        }
    }
}
