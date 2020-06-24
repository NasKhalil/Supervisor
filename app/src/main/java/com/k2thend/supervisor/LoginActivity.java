package com.k2thend.supervisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k2thend.supervisor.databinding.ActivityLoginBinding;
import com.k2thend.supervisor.model.User;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth; // declare an instance of FirebaseAuth
    private DatabaseReference mRefrence;
    private FirebaseDatabase mDatabase;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFireBase();


        binding.login.setOnClickListener(v -> LoginActivity.this.Login());

        binding.signInIntent.setOnClickListener(v -> LoginActivity.this.startActivity(new Intent(LoginActivity.this, SubscribeActivity.class)));

    }

    @Override
    public void onBackPressed() {
         new MaterialAlertDialogBuilder(this)
                 .setIcon(android.R.drawable.ic_dialog_alert)
                 .setTitle("EXIT")
                .setMessage("Are you sure you want to close application")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    private void Login (){
        if (!isEmpty()) {
            String mail = binding.email.getText().toString();
            String password = binding.password.getText().toString();
            if (validEmail() && validPassword()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.login.setVisibility(View.GONE);
                mAuth.signInWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful() ){
                                Log.e("uid",task.getResult().getUser().getUid());
                                String uid = task.getResult().getUser().getUid();
                                mRefrence.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user.getType() == 1){
                                            binding.progressBar.setVisibility(View.GONE);
                                            binding.login.setVisibility(View.VISIBLE);
                                            startActivity( new Intent(LoginActivity.this , NavigationActivity.class));
                                            finish();
                                        }else {
                                            binding.progressBar.setVisibility(View.GONE);
                                            binding.login.setVisibility(View.VISIBLE);
                                            startActivity( new Intent(LoginActivity.this , MainActivity.class));
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else
                            {
                                Snackbar.make(binding.getRoot(), task.getException().getMessage(),Snackbar.LENGTH_LONG).show();
                            }
                        });
            }

        }


/*
// TODO: FireBase Login
                startActivity( new Intent(LoginActivity.this , MainActivity.class));
                finish();
            }
        }

 */

    }

    private boolean isEmpty() {
        boolean empty = false ;

        if (TextUtils.isEmpty(binding.email.getText()))
        {
            empty = true ;
            binding.email.setError("Empty Field");
        }
        if ((TextUtils.isEmpty(binding.password.getText())))
        {
            empty = true ;
            binding.password.setError("Empty Field");
        }
        return  empty ;
    }
    private boolean validEmail(){
        boolean valid = true ;
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText()).matches())
        {
            valid = false ;
            binding.email.setError("Enter a valid Email");
        }

        return valid ;
    }
    private boolean validPassword (){
        boolean valide = true ;
        if (binding.password.getText().length() <6)
        {
            valide = false ;
            binding.password.setError("6 caracters minimun ");
        }
        return  valide ;
    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        //get la base de donné
        mDatabase = FirebaseDatabase.getInstance();
        // le curseur
        mRefrence = mDatabase.getReference("user");

    }


}