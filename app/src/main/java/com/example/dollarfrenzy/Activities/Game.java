package com.example.dollarfrenzy.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dollarfrenzy.ClassObjects.Board;
import com.example.dollarfrenzy.ClassObjects.Player;
import com.example.dollarfrenzy.Listeners.OnSwipeTouchListener;
import com.example.dollarfrenzy.R;
import com.example.dollarfrenzy.Views.ScreenView;

public class Game extends AppCompatActivity {
    private ScreenView screenView;
    AudioAttributes attrs = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
    final SoundPool sp = new SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(attrs)
            .build();
    boolean[] retMove;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        setContentView(R.layout.activity_game);
        screenView = findViewById(R.id.screenView);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        final int[] soundIds = new int[2];
        soundIds[0] = sp.load(getApplicationContext(), R.raw.redrush_eat, 1);
        soundIds[1] = sp.load(getApplicationContext(), R.raw.redrush_lost, 1);

        int size = getIntent().getIntExtra("size",3);
        Board board = new Board(size);
        final Player p = new Player(board,getApplicationContext());
        Player.turns = 0;
        screenView.setmBoard(p.getB());
        screenView.invalidate();
        screenView.setOnTouchListener(new OnSwipeTouchListener(Game.this) {
            public void onSwipeTop() {
                retMove = p.move("UP");
                if(retMove[0]){
                    if (retMove[1])
                        sp.play(soundIds[0], 1, 1, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
                        sp.play(soundIds[1], 1, 1, 1, 0, 1);
                        screenView.setOnTouchListener(null);
                        dialog();
                    }
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
            public void onSwipeRight() {
                retMove = p.move("RIGHT");
                if(retMove[0]){
                    if (retMove[1])
                        sp.play(soundIds[0], 1, 1, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
                        sp.play(soundIds[1], 1, 1, 1, 0, 1);
                        screenView.setOnTouchListener(null);
                        dialog();
                    }
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
            public void onSwipeLeft() {
                retMove = p.move("LEFT");
                if(retMove[0]){
                    if (retMove[1])
                        sp.play(soundIds[0], 1, 1, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
                        sp.play(soundIds[1], 1, 1, 1, 0, 1);
                        screenView.setOnTouchListener(null);
                        dialog();
                    }
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
            public void onSwipeBottom() {
                retMove = p.move("DOWN");
                if(retMove[0]){
                    if (retMove[1])
                        sp.play(soundIds[0], 1, 1, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
                        sp.play(soundIds[1], 1, 1, 1, 0, 1);
                        screenView.setOnTouchListener(null);
                        dialog();
                    }
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
        });
    }

    public void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
        View view = getLayoutInflater().inflate(R.layout.dialog,null);
        TextView msg = view.findViewById(R.id.msg);
        Button again = view.findViewById(R.id.again);
        Button back = view.findViewById(R.id.back);
        msg.setText("Score: "+Player.turns+"\nGood Job!");
        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.release();
    }
}
