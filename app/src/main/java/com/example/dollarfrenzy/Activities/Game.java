package com.example.dollarfrenzy.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;

import com.example.dollarfrenzy.ClassObjects.Board;
import com.example.dollarfrenzy.ClassObjects.Player;
import com.example.dollarfrenzy.Listeners.OnSwipeTouchListener;
import com.example.dollarfrenzy.R;
import com.example.dollarfrenzy.Views.ScreenView;

public class Game extends AppCompatActivity {
    private ScreenView screenView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(true);


        setContentView(R.layout.activity_game);
        screenView = findViewById(R.id.screenView);
        int size = getIntent().getIntExtra("size",3);
        Board board = new Board(size);
        final Player p = new Player(board,getApplicationContext());
        Player.turns = 0;
        screenView.setmBoard(p.getB());
        screenView.invalidate();
        screenView.setOnTouchListener(new OnSwipeTouchListener(Game.this) {
            public void onSwipeTop() {
                if(p.move("UP")){
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
                        screenView.setOnTouchListener(null);
                        dialog();
                    }
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
            public void onSwipeRight() {
                if(p.move("RIGHT")){
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
                        screenView.setOnTouchListener(null);
                        dialog();
                    }
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
            public void onSwipeLeft() {
                if (p.move("LEFT")){
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
                        screenView.setOnTouchListener(null);
                        dialog();
                    }
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
            public void onSwipeBottom() {
                if(p.move("DOWN")){
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()){
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
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("You've Lost!")
                .setMessage("You've reached a score of "+Player.turns+"! very nice indeed.")
                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
