package com.example.dollarfrenzy.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dollarfrenzy.R;

public class MainActivity extends AppCompatActivity {
    EditText size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        size = findViewById(R.id.editText);
    }

    public void start(View view){
        String ETsize = size.getText().toString();
        if (ETsize.length()!=0 && Integer.parseInt(ETsize)>3&&Integer.parseInt(ETsize)<=40){
            Intent intent = new Intent(getApplicationContext(),Game.class);
            intent.putExtra("size",Integer.parseInt(ETsize));
            startActivity(intent);
        }
       else{
           Toast.makeText(this,"please enter a valid size",Toast.LENGTH_LONG).show();
        }
    }
}
