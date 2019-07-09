package com.example.dollarfrenzy.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dollarfrenzy.ClassObjects.Board;
import com.example.dollarfrenzy.ClassObjects.Player;
import com.example.dollarfrenzy.Listeners.OnSwipeTouchListener;
import com.example.dollarfrenzy.R;
import com.example.dollarfrenzy.Views.ScreenView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Game extends AppCompatActivity {
    private ScreenView screenView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public int size = 0;
    Map<String,Object> Score = new HashMap<>();
    Map<String,Object> World = new HashMap<>();
    AlertDialog dialog;

    AudioAttributes attrs = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
    final SoundPool sp = new SoundPool.Builder()
            .setMaxStreams(4)
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
        initScore();
        initWorld();
        setContentView(R.layout.activity_game);
        screenView = findViewById(R.id.screenView);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        final int[] soundIds = new int[4];
        soundIds[0] = sp.load(getApplicationContext(), R.raw.redrush_eat, 1);
        soundIds[1] = sp.load(getApplicationContext(), R.raw.redrush_lost, 1);
        soundIds[2] = sp.load(getApplicationContext(), R.raw.redrush_newscore, 1);
        soundIds[3] = sp.load(getApplicationContext(), R.raw.redrush_worldscore, 1);
        size = getIntent().getIntExtra("size",3);
        Board board = new Board(size);
        final Player p = new Player(board,getApplicationContext());
        Player.turns = 0;
        getCurrentScore();
        getWorldScore();
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
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], 1, 1, 1, 0, 1);
                    }
                    else if (p.checkBoard()==0)
                        screenView.imageNum = 0;
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
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], 1, 1, 1, 0, 1);
                    }
                    else if (p.checkBoard()==0)
                        screenView.imageNum = 0;
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
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], 1, 1, 1, 0, 1);
                    }
                    else if (p.checkBoard()==0)
                        screenView.imageNum = 0;
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
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], 1, 1, 1, 0, 1);
                    }
                    else if (p.checkBoard()==0)
                        screenView.imageNum = 0;
                    screenView.setmBoard(p.getB());
                    screenView.invalidate();
                }

            }
        });
    }

    public int dialog(){
        boolean isHighScore = false;
        boolean isWorldScore = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
        View view = getLayoutInflater().inflate(R.layout.dialog,null);
        TextView msg = view.findViewById(R.id.msg);
        ImageView starSmall = view.findViewById(R.id.starSmall);
        ImageView starBig = view.findViewById(R.id.starBig);
        Button again = view.findViewById(R.id.again);
        Button back = view.findViewById(R.id.backbtn);
        if (Score.get(size+"X"+size) instanceof  Integer){
            if (((Integer)Score.get(size+"X"+size)).intValue()>=Player.turns)
                msg.setText("Score: "+Player.turns+"\nGood Job!");

            else{
                if(Integer.parseInt(((String)World.get(size+"X"+size)).split(" ")[((String)World.get(size+"X"+size)).split(" ").length-1])<Player.turns){
                    msg.setText("Score: "+Player.turns+"\nNew World Record!");
                    starBig.setVisibility(View.VISIBLE);
                    World.put(size+"X"+size,mAuth.getCurrentUser().getDisplayName()+" "+Player.turns);
                    Score.put(size+"X"+size,Player.turns);
                    isWorldScore = true;
                    updateWorld();
                    updateDB();
                }
                else {
                    msg.setText("Score: "+Player.turns+"\nNew Record!");
                    starSmall.setVisibility(View.VISIBLE);
                    Score.put(size+"X"+size,Player.turns);
                    isHighScore = true;
                    updateDB();
                }
            }
        }
        else{
            if (((Long)Score.get(size+"X"+size)).intValue()>=Player.turns)
                msg.setText("Score: "+Player.turns+"\nGood Job!");
            else{
                if(Integer.parseInt(((String)World.get(size+"X"+size)).split(" ")[((String)World.get(size+"X"+size)).split(" ").length-1])<Player.turns){
                    msg.setText("Score: "+Player.turns+"\nNew World Record!");
                    starBig.setVisibility(View.VISIBLE);
                    World.put(size+"X"+size,mAuth.getCurrentUser().getDisplayName()+" "+Player.turns);
                    Score.put(size+"X"+size,Player.turns);
                    isWorldScore = true;
                    updateWorld();
                    updateDB();
                }
                else{
                    msg.setText("Score: "+Player.turns+"\nNew Record!");
                    starSmall.setVisibility(View.VISIBLE);
                    Score.put(size+"X"+size,Player.turns);
                    isHighScore = true;
                    updateDB();
                }

            }

        }


        builder.setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        if (isHighScore)
            return 2;
        if (isWorldScore)
            return 3;
        return 1;

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
    public void getCurrentScore() {
        final DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getDisplayName());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if(documentSnapshot!=null)
                        Score = documentSnapshot.getData();
                    else
                        Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_LONG).show();

                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void getWorldScore() {
        final DocumentReference docRef = db.collection("Users").document("Top");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if(documentSnapshot!=null)
                        World = documentSnapshot.getData();
                    else
                        Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_LONG).show();

                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void updateDB(){
        final DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getDisplayName());
        docRef.set(Score).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error writing document - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void updateWorld(){
        final DocumentReference docRef = db.collection("Users").document("Top");
        docRef.set(World).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error writing document - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void initScore(){
        Score.put("3X3",0);
        Score.put("4X4",0);
        Score.put("5X5",0);
        Score.put("6X6",0);
        Score.put("7X7",0);
        Score.put("8X8",0);
        Score.put("9X9",0);
        Score.put("10X10",0);
        Score.put("11X11",0);
        Score.put("12X12",0);
        Score.put("13X13",0);
        Score.put("14X14",0);
        Score.put("15X15",0);
        Score.put("16X16",0);
        Score.put("17X17",0);
        Score.put("18X18",0);
        Score.put("19X19",0);
        Score.put("20X20",0);
    }

    public void initWorld(){
        World.put("3X3","null 0");
        World.put("4X4","null 0");
        World.put("5X5","null 0");
        World.put("6X6","null 0");
        World.put("7X7","null 0");
        World.put("8X8","null 0");
        World.put("9X9","null 0");
        World.put("10X10","null 0");
        World.put("11X11","null 0");
        World.put("12X12","null 0");
        World.put("13X13","null 0");
        World.put("14X14","null 0");
        World.put("15X15","null 0");
        World.put("16X16","null 0");
        World.put("17X17","null 0");
        World.put("18X18","null 0");
        World.put("19X19","null 0");
        World.put("20X20","null 0");
    }
}
