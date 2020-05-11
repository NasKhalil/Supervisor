package com.k2thend.supervisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.k2thend.supervisor.databinding.ActivitySubscribeBinding;
import com.k2thend.supervisor.model.User;


public class SubscribeActivity extends AppCompatActivity {
    private ActivitySubscribeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseStorage mStorage;
    private User mUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscribeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFirebase();
        binding.sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscribeActivity.this.signin();
            }
        });

    }

    private void signin() {
        if (!isEmpty()) {
            final String name = binding.name.getText().toString();
            final String mail = binding.email.getText().toString();
            final String pwd = binding.password.getText().toString();
            String ConfirmPwd = binding.confirmPass.getText().toString();

            if (confirmPassword() && validEmail()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser.setUid(mAuth.getCurrentUser().getUid());
                            mUser.setMail(mail);
                            mUser.setName(name);
                            mUser.setPwd(pwd);
                            mReference.child(mUser.getUid()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        binding.progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(SubscribeActivity.this, MainActivity.class));
                                        finish();
                                    }else
                                    {
                                        Snackbar.make(binding.getRoot(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            Snackbar.make(binding.getRoot(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }


    private boolean isEmpty() {
        boolean empty = false;

        if (TextUtils.isEmpty(binding.email.getText())) {
            empty = true;
            binding.email.setError("Empty Field");
        }
        if (TextUtils.isEmpty(binding.password.getText())) {
            empty = true;
            binding.password.setError("Empty Field");
        }
        if (TextUtils.isEmpty(binding.name.getText())) {
            empty = true;
            binding.name.setError("Empty Field");
        }
        if (TextUtils.isEmpty(binding.confirmPass.getText())) {
            empty = true;
            binding.confirmPass.setError("Empty Field");
        }

        return empty;
    }

    private boolean validEmail() {
        boolean valid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText()).matches()) {
            valid = false;
            binding.email.setError(" entrer un email Valide");
        }

        return valid;
    }

    private boolean validPassword() {
        boolean valide = true;
        if (binding.password.getText().length() < 6 && binding.confirmPass.getText().length() < 6) {
            valide = false;
            binding.password.setError("minimum 6 caractéres ");
            binding.confirmPass.setError("minimum 6 caractéres ");
        }
        return valide;
    }

    private boolean confirmPassword() {
        boolean confirm = true;

        if (!binding.password.getText().toString().equals(binding.confirmPass.getText().toString())) {
            confirm = false;
            binding.confirmPass.setError("Password Mismatch");
        }

        return confirm;
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance(); // initialize the Firebase instance
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("user");
        mStorage = FirebaseStorage.getInstance();
    }


}
