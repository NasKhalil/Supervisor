package com.k2thend.supervisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k2thend.supervisor.databinding.ActivityMainBinding;
import com.k2thend.supervisor.model.User;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private Data mData = new Data();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFirebase();
        getData();


        binding.logout.setOnClickListener(v -> {
            mAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        binding.send.setOnClickListener(v -> sendData());

        binding.save.setOnClickListener(v -> saveData());

    }


    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("EXIT")
                .setMessage("Are you sure you want to close application")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    private void getData(){
        mReference.child("user").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);
                binding.user.setText("welcome "+mUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    private void saveData(){
        Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    private void sendData(){

        Toast.makeText(MainActivity.this, "Data sent", Toast.LENGTH_SHORT).show();

        /*mData.setQuestion("hello");
        mReference.child("data").child(mAuth.getUid()).setValue(mData).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                Toast.makeText(MainActivity.this, "Data sent", Toast.LENGTH_SHORT).show();
            }else
            {
                Snackbar.make(binding.getRoot(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });*/
    }



    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance(); // initialize the Firebase instance
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

}
