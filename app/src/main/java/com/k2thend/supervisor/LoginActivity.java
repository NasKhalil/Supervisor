package com.k2thend.supervisor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // declaration des variables

    private EditText email;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      String emailString = email.getText().toString();
      String passwordString = password.getText().toString();

      // regex
        // Pattern namePattern = Pattern.compile("[a-zA-Z\\s]*");
        Pattern emailPattern =Pattern.compile("^[A-Za-z0-9._-]+@+[A-Za-z]+.+[com]");
        // test validation


        if(!emailPattern.matcher(emailString).matches()){
            email.setError("Invalid email");
        }

        if(passwordString.length() < 5){
            password.setError("Password must be 6 caracters at least");
        }

    }
}
