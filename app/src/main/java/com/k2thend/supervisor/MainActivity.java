package com.k2thend.supervisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
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
import com.k2thend.supervisor.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFirebase();
        getData();
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.getUserName();
        Log.e("name", sessionManager.getUserName());

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

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
                sessionManager.saveLoginDetails(mUser.getName(), mUser.getMail());
                Toast.makeText(MainActivity.this, sessionManager.getUserName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", databaseError.getMessage());
            }
        });
    }

    private void saveData(){
        Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
        createPdfDoc();
    }

    private void sendData(){
        binding.pLunchTime.clearCheck();
        binding.pEvening.clearCheck();
        binding.checkoutArea.clearCheck();
        binding.nbOfCleaningStuff.getText().clear();
        binding.remarqs.getText().clear();
        binding.problems.getText().clear();
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

    private void createPdfDoc() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint mPaint = new Paint();

        PdfDocument.PageInfo mPageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page mPage = pdfDocument.startPage(mPageInfo);

        Canvas canvas = mPage.getCanvas();

        canvas.drawText(sessionManager.getUserName(),125, 10, mPaint);
        canvas.drawText("pdf test", 40, 50, mPaint);
        String problem = binding.problems.getText().toString();
        canvas.drawText(problem, 40, 100, mPaint);
        String datee =  android.text.format.DateFormat.format("yyyy-MM-dd hh:mm", new java.util.Date()).toString();
        canvas.drawText(datee, 40, 150, mPaint);

        //File file = new File(Environment.getDataDirectory(), File.separator + "myFuckingPdf.pdf");

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),File.separator+ "test.pdf");
        pdfDocument.finishPage(mPage);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Log.e("TAG", "createPdfDoc: "+ file.getPath() );
            pdfDocument.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance(); // initialize the Firebase instance
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        createPdfDoc();
    }
}
