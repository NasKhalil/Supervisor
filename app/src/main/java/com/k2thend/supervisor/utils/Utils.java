package com.k2thend.supervisor.utils;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.k2thend.supervisor.R;
import com.k2thend.supervisor.databinding.UtilsBinding;


public class Utils extends AppCompatActivity {

    private UtilsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UtilsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getApplicationContext());

                String[] utils = new String[] {"EditText", "Button", "FAB", "RadioButton"};

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(getApplicationContext(),
                                R.layout.drop_down_item,
                                R.id.drop,
                                utils);

                AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.exposed_dropdown);
                autoCompleteTextView.setAdapter(adapter);

                dialog.setContentView(autoCompleteTextView);
                dialog.show();


            }
        });
    }

    public void button(int color, String text, int size){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        Button button = new Button(this);
        button.setBackgroundColor(color);
        button.setText(text);
        button.setTextSize(size);
        button.setText("Button created");
        button.setLayoutParams(params);
    }


}
