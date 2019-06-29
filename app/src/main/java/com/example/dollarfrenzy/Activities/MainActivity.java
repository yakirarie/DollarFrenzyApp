package com.example.dollarfrenzy.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dollarfrenzy.R;

public class MainActivity extends AppCompatActivity {
    TextView chosenSize;
    SeekBar seekBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SeekBar();

    }

    public void start(View view){
        int size = seekBar.getProgress()+3;
        Intent intent = new Intent(getApplicationContext(),Game.class);
        intent.putExtra("size",size);
        startActivity(intent);
    }

    public void SeekBar(){
        chosenSize = findViewById(R.id.chosenSize);
        seekBar = findViewById(R.id.seekBar);
        chosenSize.setText("Board Size: "+(seekBar.getProgress()+3)+"X"+(seekBar.getProgress()+3));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int prog;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                prog = i;
                chosenSize.setText("Board Size: "+(i+3)+"X"+(i+3));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                chosenSize.setText("Board Size: "+(prog+3)+"X"+(prog+3));
            }
        });

    }
}
