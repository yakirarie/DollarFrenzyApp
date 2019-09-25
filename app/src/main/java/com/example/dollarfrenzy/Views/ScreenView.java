package com.example.dollarfrenzy.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;


import android.util.AttributeSet;
import android.view.View;

import com.example.dollarfrenzy.Activities.MainActivity;
import com.example.dollarfrenzy.ClassObjects.Board;
import com.example.dollarfrenzy.R;

public class ScreenView extends View {
    private Board mBoard;
    public int imageNum = -1;

    public ScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setmBoard(Board b){ mBoard = new Board(b);}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable d = getResources().getDrawable(R.drawable.background_play, null);
        d.setBounds(getLeft(), getTop(), getRight(), getBottom());
        d.draw(canvas);

        if(mBoard != null){
            float tileSizeX = (float)getWidth()/mBoard.getSize();
            float tileSizeY = (float)getHeight()/mBoard.getSize();
            for(int i=0 ;i<mBoard.getSize();i++){
                for(int j=0;j<mBoard.getSize();j++){
                    switch (mBoard.getMatrix()[i][j]){
                        case Board.FRUIT:
                            if (imageNum == -1 || imageNum == 0){
                                d = getResources().getDrawable(R.drawable.red, null);
                                d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                d.draw(canvas);
                            }
                            else{
                                d = getResources().getDrawable(R.drawable.red_happy, null);
                                d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                d.draw(canvas);
                            }
                            break;
                        case Board.PLAYER:
                            if(MainActivity.playerType[0]){
                                if (imageNum == -1){
                                    d = getResources().getDrawable(R.drawable.green, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                                else if (imageNum == 0){
                                    d = getResources().getDrawable(R.drawable.green_worried, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                                else{
                                    d = getResources().getDrawable(R.drawable.green_lost, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                            }

                            if(MainActivity.playerType[1]){
                                if (imageNum == -1){
                                    d = getResources().getDrawable(R.drawable.green_cowboy, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                                else if (imageNum == 0){
                                    d = getResources().getDrawable(R.drawable.green_worried_cowboy, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                                else{
                                    d = getResources().getDrawable(R.drawable.green_lost_cowboy, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                            }

                            if(MainActivity.playerType[2]){
                                if (imageNum == -1){
                                    d = getResources().getDrawable(R.drawable.green_scar, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                                else if (imageNum == 0){
                                    d = getResources().getDrawable(R.drawable.green_worried_scar, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                                else{
                                    d = getResources().getDrawable(R.drawable.green_lost_scar, null);
                                    d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                    d.draw(canvas);
                                }
                            }

                            break;

                        case Board.FRUIT3:
                                d = getResources().getDrawable(R.drawable.red3, null);
                                d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                                d.draw(canvas);
                                break;
                    }

                }
            }

        }
    }
}
