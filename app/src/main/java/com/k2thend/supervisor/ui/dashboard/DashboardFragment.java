package com.k2thend.supervisor.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.util.Util;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.k2thend.supervisor.NavigationActivity;
import com.k2thend.supervisor.R;
import com.k2thend.supervisor.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFireBase();
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout = new TextInputLayout(binding.root.getContext());
                textInputLayout.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                binding.root.addView(textInputLayout);


            }
        });
    }

    private void saveData() {
       int id = binding.FDM.getCheckedRadioButtonId();

       if(R.id.FDMPLT == id){
           if(binding.FDMPLT.isChecked()){

               Toast.makeText(getActivity(), binding.FDMPLT.getHint(), Toast.LENGTH_SHORT).show();
           }
       }

       if(binding.FDMPE.isChecked()){
           Toast.makeText(getActivity(), binding.FDMPE.getHint(), Toast.LENGTH_SHORT).show();
       }

    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();

        mRefrence = mDatabase.getReference("user");

    }


}
