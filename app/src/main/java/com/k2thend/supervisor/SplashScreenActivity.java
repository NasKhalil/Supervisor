package com.k2thend.supervisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.k2thend.supervisor.databinding.NewSplashScreenBinding;
import com.k2thend.supervisor.model.User;

public class SplashScreenActivity extends AppCompatActivity {
    private NewSplashScreenBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;


    public static int TIME_OUT = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = NewSplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initFireBase();

        binding.linearLayout2.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                isConnected();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });
        binding.linearLayout2.startLayoutAnimation();

        //old splash_screen with handler

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME_OUT);*/
    }


    private void isConnected() {

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            mRefrence.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getType() == 1) {
                        startActivity(new Intent(SplashScreenActivity.this, NavigationActivity.class));
                        finish();

                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            startActivity(new Intent(SplashScreenActivity.this , LoginActivity.class));
            finish();
        }

    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        //get la base de donné
        mDatabase = FirebaseDatabase.getInstance();
        // le curseur
        mRefrence = mDatabase.getReference("user");

    }


}
