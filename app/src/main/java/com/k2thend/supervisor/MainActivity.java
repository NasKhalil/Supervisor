package com.k2thend.supervisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PrintAttributes;
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
import com.google.firebase.database.annotations.NotNull;
import com.k2thend.supervisor.databinding.ActivityMainBinding;
import com.k2thend.supervisor.model.User;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initFirebase();
        getData();

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

    private void pdf(){

        new CreatePdf(MainActivity.this)
                .setPdfName("FirstPdf")
                .openPrintDialog(true)
                .setContentBaseUrl(null)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("test test test")
                .setFilePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MyPdf.pdf")
                .setCallbackListener(new CreatePdf.PdfCallbackListener() {
                    @Override
                    public void onFailure(@NotNull String s) {
                        // handle error
                        Toast.makeText(MainActivity.this, "error "+ s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(@NotNull String s) {
                        // do your stuff here
                        Toast.makeText(MainActivity.this, "success " + s, Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    private void createPdfDoc() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint mPaint = new Paint();

        PdfDocument.PageInfo mPageInfo = new PdfDocument.PageInfo.Builder(250, 400, 1).create();
        PdfDocument.Page mPage = pdfDocument.startPage(mPageInfo);

        Canvas canvas = mPage.getCanvas();
        canvas.drawText("hello fuckin pdf", 40, 50, mPaint);

        //File file = new File(Environment.getDataDirectory(), File.separator + "myFuckingPdf.pdf");

        File file = new File(Environment.DIRECTORY_DOCUMENTS, "test.pdf");
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
