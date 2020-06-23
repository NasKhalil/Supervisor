package com.k2thend.supervisor.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.k2thend.supervisor.LoginActivity;
import com.k2thend.supervisor.databinding.FragmentProfileBinding;
import com.k2thend.supervisor.model.User;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseStorage mStorage;

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFirebase();
        getData();
        binding.logout.setOnClickListener(v -> logout());

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profileMotionLayout.startLayoutAnimation();
            }
        });

    }


    private void getData() {
        mReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User mUser = dataSnapshot.getValue(User.class);
                Glide.with(getActivity()).load(mUser.getUrl()).into(binding.profileImage);
                binding.email.setText(mUser.getMail());
                binding.name.setText(mUser.getName());
                binding.phone.setText(mUser.getPhone());
               // binding.progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    private void finishActivity() {
        if(getActivity() != null) {
            getActivity().finish();
        }
    }

    private  void logout(){
        new MaterialAlertDialogBuilder(requireContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure! you want to logout ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mAuth.signOut();
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                    finishActivity();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance(); // initialize the Firebase instance
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("user");
        mStorage = FirebaseStorage.getInstance();
    }
}