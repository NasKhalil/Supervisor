package com.k2thend.supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class SubscribeActivity extends AppCompatActivity implements View.OnClickListener {

    // declaration des variables
    private EditText user_name;
    private EditText user_email;
    private EditText user_password;
    private EditText confirm_password;
    private Button sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_password =findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_pass);
        sign = findViewById(R.id.sign);

        sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String nameString = user_name.getText().toString();
        String emailString = user_email.getText().toString();
        String passwordString = user_password.getText().toString();
        String confirmPassString = confirm_password.getText().toString();


        // regex
        Pattern namePattern = Pattern.compile("[a-zA-Z\\s]*");
        Pattern emailPattern =Pattern.compile("^[A-Za-z0-9._-]+@+[A-Za-z]+.+[com]");
        // test validation

        if(!namePattern.matcher(nameString).matches()){
            user_name.setError("name must be only caracter");
        }

        if(!emailPattern.matcher(emailString).matches()){
            user_email.setError("Invalid email");
        }

        if(passwordString.length() < 5){
            user_password.setError("Password must be 6 caracters at least");
        }

        if(confirmPassString != passwordString){
            user_password.setError("wrong password");
        }
    }
}
