package com.k2thend.supervisor.utils;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import com.k2thend.supervisor.R;
import com.k2thend.supervisor.databinding.UtilsBinding;


public class Utils {

    /*
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

     */

    public Button button(Context context, @ColorRes int color, String text, int size){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        Button button = new Button(context);
        button.setBackgroundColor(context.getResources().getColor(color));
        button.setText(text);
        button.setTextSize(size);
        button.setLayoutParams(params);
        Toast.makeText(context, "button created", Toast.LENGTH_SHORT).show();
        return button;
    }

    public View textView(Context context, int color, String text, int size){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(context);
        textView.setBackgroundColor(color);
        textView.setText(text);
        textView.setTextSize(size);
        textView.setLayoutParams(params);
        Toast.makeText(context, "text created", Toast.LENGTH_SHORT).show();
        return textView;
    }


    public EditText editText(Context context, int color, String text, int size){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditText editText = new EditText(context);
        editText.setBackgroundColor(color);
        editText.setHint(text);
        editText.setTextSize(size);
        editText.setLayoutParams(params);
        editText.setTag(text);
        Toast.makeText(context, "text created", Toast.LENGTH_SHORT).show();
        return editText;
    }


}
