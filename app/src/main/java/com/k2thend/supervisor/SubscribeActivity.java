package com.k2thend.supervisor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.storage.StorageReference;
import com.k2thend.supervisor.databinding.ActivitySubscribeBinding;
import com.k2thend.supervisor.model.User;

import java.io.IOException;


public class SubscribeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1000;
    private ActivitySubscribeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseStorage mStorage;
    private Uri filePath;
    private User mUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscribeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFirebase();
        binding.imageAdd.setOnClickListener(v -> choosePicture(binding.getRoot()));
        binding.sign.setOnClickListener(v -> SubscribeActivity.this.signin());

    }

    private void signin() {
        if (!isEmpty()) {
            final String name = binding.name.getText().toString();
            final String mail = binding.email.getText().toString();
            final String pwd = binding.password.getText().toString();
            final String phone = binding.phone.getText().toString();

            if (validPassword() && validEmail() && valideImage()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.sign.setVisibility(View.GONE);
                mAuth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mUser.setUid(mAuth.getCurrentUser().getUid());
                        mUser.setMail(mail);
                        mUser.setName(name);
                        mUser.setPwd(pwd);
                        mUser.setPhone(phone);
                        uploadImage();
                        mReference.child(mUser.getUid()).setValue(mUser).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful())
                            {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.sign.setVisibility(View.VISIBLE);
                                startActivity(new Intent(SubscribeActivity.this, NavigationActivity.class));
                                finish();
                            }else
                            {
                                Snackbar.make(binding.getRoot(), task1.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.sign.setVisibility(View.VISIBLE);
                        Snackbar.make(binding.getRoot(), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            getContentResolver().takePersistableUriPermission(filePath, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.imageAdd.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
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
        if (TextUtils.isEmpty(binding.phone.getText())) {
            empty = true;
            binding.phone.setError("Empty Field");
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
        if (binding.password.getText().length() < 6) {
            valide = false;
            binding.password.setError("minimum 6 caractéres ");
        }
        return valide;
    }

    public void choosePicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private boolean valideImage() {
        boolean valid = true;
        if (filePath == null) {
            valid = false;
            Snackbar.make(binding.imageAdd, "Ajouter une Image ", Snackbar.LENGTH_LONG);
        }
        return valid;
    }

    private void uploadImage() {
        StorageReference child = mStorage.getReference().child(mUser.getUid());
        child.putFile(filePath).continueWithTask(task -> child.getDownloadUrl()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mUser.setUrl(task.getResult().toString());
                saveUser();
            }
        });
    }

    private void saveUser() {
        String nom = binding.name.getText().toString();
        mUser.setName(nom);
        mUser.setMail(binding.email.getText().toString());
        mUser.setType(0);
        mReference.child(mUser.getUid()).setValue(mUser).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                binding.progressBar.setVisibility(View.GONE);
                startActivity(new Intent(SubscribeActivity.this, NavigationActivity.class));
                finish();
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.getRoot(), task.getException().getMessage(), Snackbar.LENGTH_LONG);
            }

        });

    }

        private void initFirebase() {
        mAuth = FirebaseAuth.getInstance(); // initialize the Firebase instance
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("user");
        mStorage = FirebaseStorage.getInstance();
    }


}
