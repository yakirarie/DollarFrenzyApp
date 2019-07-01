package com.example.dollarfrenzy.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
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
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    TextView chosenSize;
    SeekBar seekBar;
    HomeWatcher mHomeWatcher;
    AlertDialog dialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> scores = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCurrentScore();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //BIND Music Service
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
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
                    if(documentSnapshot!=null) {
                        scores = documentSnapshot.getData();

                    }
                    else
                        Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"hara",Toast.LENGTH_LONG).show();

                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void watchScores(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.high_scores,null);
        TextView msg = mView.findViewById(R.id.msg);
        msg.setMovementMethod(new ScrollingMovementMethod());
        TextView title = mView.findViewById(R.id.title);
        String head = title.getText().toString();
        title.setText((mAuth.getCurrentUser().getDisplayName())+" "+head);
        if (scores==null)
            msg.setText("no high scores yet");
        else{
            long[] scoreArray = sortScores();
            String fill = "";
            for (int i=0;i<scoreArray.length;i++){
                fill += (i+3)+"X"+(i+3)+" - "+scoreArray[i]+"\n";
            }
            msg.setText(fill);

        }
        builder.setView(mView);
        dialog = builder.create();
        dialog.show();


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

        if (mServ != null) {
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

        if (!isScreenOn) {
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
        long[] scoresArray = new long[17];
        if(scores!=null) {
            for (String s : scores.keySet()) {
//                scoresArray[ Character.getNumericValue(s.charAt(0)) - 3] = (long) scores.get(s);
                Toast.makeText(getApplicationContext(), s.charAt(0) + "", Toast.LENGTH_LONG).show();
            }
        }
        return scoresArray;
    }


}


