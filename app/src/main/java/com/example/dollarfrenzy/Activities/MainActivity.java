package com.example.dollarfrenzy.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dollarfrenzy.ClassObjects.HomeWatcher;
import com.example.dollarfrenzy.ClassObjects.Player;
import com.example.dollarfrenzy.R;
import com.example.dollarfrenzy.Services.MusicService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    TextView chosenSize;
    SeekBar seekBar;
    HomeWatcher mHomeWatcher;
    AlertDialog dialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> scores = new HashMap<>();
    Map<String,Object> world = new HashMap<>();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.4F);
    SharedPreferences sharedPreferences;
    boolean sound;
    static int numberOfPlayerTypes= 3;
    public static boolean[] playerType = new boolean[numberOfPlayerTypes];
    ImageButton soundButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getCurrentScore();
        getWorldScore();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sound = sharedPreferences.getBoolean("sound",true);

        playerType[0] = sharedPreferences.getBoolean("green1", true);;
        for (int i=1 ;i<3;i++)
            playerType[i] = sharedPreferences.getBoolean("green" + (i + 1), false);



        soundButton = findViewById(R.id.imageButton);
        if (sound)
            soundButton.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
        else
            soundButton.setImageResource(android.R.drawable.ic_lock_silent_mode);

        //BIND Music Service
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        if (sound)
            startService(music);
        //Start HomeWatcher
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();
        SeekBar();

    }

    public void start(View view){
        view.startAnimation(buttonClick);
        int size = seekBar.getProgress()+3;
        Intent intent = new Intent(getApplicationContext(),Game.class);
        intent.putExtra("size",size);
        mHomeWatcher.stopWatch();
        startActivity(intent);
        finish();

    }

    public void getCurrentScore() {
        final DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getDisplayName());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    scores = documentSnapshot.getData();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Set<String> valueSet =  new HashSet<>();
                    for (String s : scores.keySet())
                        valueSet.add(s+"-"+scores.get(s));
                    editor.putStringSet("score",valueSet);
                    editor.apply();

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
                    world = documentSnapshot.getData();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Set<String> valueSet =  new HashSet<>();
                    for (String s : world.keySet())
                        valueSet.add(s+"-"+world.get(s));
                    editor.putStringSet("world",valueSet);
                    editor.apply();
                }



            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void watchScores(View view){
        view.startAnimation(buttonClick);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.high_scores,null);
        TextView msg = mView.findViewById(R.id.msg);
        msg.setMovementMethod(new ScrollingMovementMethod());
        TextView title = mView.findViewById(R.id.title);
        String head = title.getText().toString();
        title.setText((mAuth.getCurrentUser().getDisplayName())+"\n"+head);
        long[] scoreArray = sortScores();
        String fill = "";
        for (int i=0;i<scoreArray.length;i++)
            fill += (i+3)+"X"+(i+3)+" - "+scoreArray[i]+"\n";
        msg.setText(fill);
        builder.setView(mView);
        dialog = builder.create();
        dialog.show();


    }

    public void watchWorld(View view){
        view.startAnimation(buttonClick);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.world_scores,null);
        TextView msg = mView.findViewById(R.id.msg);
        msg.setMovementMethod(new ScrollingMovementMethod());
        String[] scoreArray = sortWorld();
        String fill = "";
        for (int i=0;i<scoreArray.length;i++)
            fill += (i+3)+"X"+(i+3)+" - "+scoreArray[i]+"\n";
        msg.setText(fill);
        builder.setView(mView);
        dialog = builder.create();
        dialog.show();


    }

    public void info(View view){
        view.startAnimation(buttonClick);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.info,null);
        builder.setView(mView);
        dialog = builder.create();
        dialog.show();
    }

    public void changePlayer(View view){
        view.startAnimation(buttonClick);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.change_player,null);
        final ImageView[] imvs = new ImageView[numberOfPlayerTypes];
        imvs[0] = mView.findViewById(R.id.green);
        imvs[1] = mView.findViewById(R.id.greenCowboy);
        imvs[2] = mView.findViewById(R.id.greenScar);
        builder.setView(mView);
        for (int i=0 ; i<numberOfPlayerTypes;i++) {
            if (playerType[i]) {
                imvs[i].setBackground(getDrawable(R.drawable.background_play));
                for (int j = 0; j < numberOfPlayerTypes; j++)
                    if (j!=i)
                        imvs[j].setBackground(null);

            }
        }
        dialog = builder.create();
        dialog.show();
        imvs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("green1",true);
                editor.putBoolean("green2",false);
                editor.putBoolean("green3",false);
                editor.apply();
                for (int i=0 ;i<3;i++)
                    playerType[i] = sharedPreferences.getBoolean("green" + (i + 1), false);
                imvs[0].setBackground(getDrawable(R.drawable.background_play));
                imvs[1].setBackground(null);
                imvs[2].setBackground(null);
            }
        });

        imvs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("green1",false);
                editor.putBoolean("green2",true);
                editor.putBoolean("green3",false);
                editor.apply();
                for (int i=0 ;i<3;i++)
                    playerType[i] = sharedPreferences.getBoolean("green" + (i + 1), false);
                imvs[0].setBackground(null);
                imvs[1].setBackground(getDrawable(R.drawable.background_play));
                imvs[2].setBackground(null);

            }
        });

        imvs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("green1",false);
                editor.putBoolean("green2",false);
                editor.putBoolean("green3",true);
                editor.apply();
                for (int i=0 ;i<3;i++)
                    playerType[i] = sharedPreferences.getBoolean("green" + (i + 1), false);
                imvs[0].setBackground(null);
                imvs[1].setBackground(null);
                imvs[2].setBackground(getDrawable(R.drawable.background_play));
            }
        });
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

    public void soundOnOff(View view){
        view.startAnimation(buttonClick);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sound = !sound;
        editor.putBoolean("sound",sound);
        editor.apply();
        if (sound){
            soundButton.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
            mServ.startMusic();

        }

        else{
            soundButton.setImageResource(android.R.drawable.ic_lock_silent_mode);
            mServ.stopMusic();
        }


    }
    //Bind/Unbind music service
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null && sound) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isInteractive();
        }

        if (!isScreenOn && sound) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }

    public long[] sortScores(){
        long[] scoresArray = new long[18];
        Set<String> valuesSet = sharedPreferences.getStringSet("score",new HashSet<String>());
        for (String s : valuesSet) {
            String board = s.split("-")[0];
            long value = Long.parseLong(s.split("-")[1]);
            int index = Integer.parseInt(board.split("X")[0]);
            scoresArray[index-3] = value;
        }
        return scoresArray;
    }

    public String[] sortWorld(){
        String[] scoresArray = new String[18];
        Set<String> valuesSet = sharedPreferences.getStringSet("world",new HashSet<String>());
        for (String s : valuesSet) {
            String board = s.split("-")[0];
            String name = s.split("-")[1];
            int index = Integer.parseInt(board.split("X")[0]);
            scoresArray[index-3] = name;
        }
        return scoresArray;
    }




}


