package com.example.dollarfrenzy.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game extends AppCompatActivity {
    private ScreenView screenView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public int size = 0;
    Map<String,Object> Score = new HashMap<>();
    Map<String,Object> World = new HashMap<>();
    AlertDialog dialog;
    SharedPreferences sharedPreferences;
    AudioAttributes attrs = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
    final SoundPool sp = new SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attrs)
            .build();
    boolean[] retMove;
    boolean sound;
    int volume;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sound = sharedPreferences.getBoolean("sound",true);
        if (sound){
            volume = 1;
        }
        else
            volume = 0;
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
                        sp.play(soundIds[0], volume, volume, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], volume, volume, 1, 0, 1);
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
                        sp.play(soundIds[0], volume, volume, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], volume, volume, 1, 0, 1);
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
                        sp.play(soundIds[0], volume, volume, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], volume, volume, 1, 0, 1);
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
                        sp.play(soundIds[0], volume, volume, 1, 0, 1);
                    p.getB().addFruit();
                    Player.turns++;
                    if (p.checkBoard()==1){
                        screenView.imageNum = 1;
                        screenView.setOnTouchListener(null);
                        sp.play(soundIds[dialog()], volume, volume, 1, 0, 1);
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
                    if(documentSnapshot!=null){
                        Score = documentSnapshot.getData();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Set<String> valueSet =  new HashSet<>();
                        for (String s : Score.keySet())
                            valueSet.add(s+"-"+Score.get(s));
                        editor.putStringSet("score",valueSet);
                        editor.apply();
                    }
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
                    if(documentSnapshot!=null){
                        World = documentSnapshot.getData();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Set<String> valueSet =  new HashSet<>();
                        for (String s : World.keySet())
                            valueSet.add(s+"-"+World.get(s));

                        editor.putStringSet("world",valueSet);
                        editor.apply();
                    }

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
        docRef.update(size+"X"+size,Player.turns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Set<String> valueSet =  new HashSet<>();
                for (String s : Score.keySet())
                    valueSet.add(s+"-"+Score.get(s));
                editor.putStringSet("score",valueSet);
                editor.apply();
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
        docRef.update(size+"X"+size,mAuth.getCurrentUser().getDisplayName()+" "+Player.turns).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Set<String> valueSet =  new HashSet<>();
                for (String s : World.keySet())
                    valueSet.add(s+"-"+World.get(s));
                editor.putStringSet("world",valueSet);
                editor.apply();
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

        Set<String> valuesSet = sharedPreferences.getStringSet("score",new HashSet<String>());
        for (String s : valuesSet) {
            String board = s.split("-")[0];
            long value = Long.parseLong(s.split("-")[1]);
            int index = Integer.parseInt(board.split("X")[0]);
            Score.put((index-3)+"X"+(index-3),value);
        }
    }

    public void initWorld(){
        Set<String> valuesSet = sharedPreferences.getStringSet("world",new HashSet<String>());
        for (String s : valuesSet) {
            String board = s.split("-")[0];
            String name = s.split("-")[1];
            int index = Integer.parseInt(board.split("X")[0]);
            World.put((index-3)+"X"+(index-3),name);
        }
    }
}
