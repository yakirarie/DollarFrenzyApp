package com.example.dollarfrenzy.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;

import com.example.dollarfrenzy.ClassObjects.Board;
import com.example.dollarfrenzy.R;

public class ScreenView extends View {
//    private Paint mPaint = new Paint();
    private Board mBoard;

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
//            float circleSize = Math.min(tileSizeX,tileSizeY)/2;
            for(int i=0 ;i<mBoard.getSize();i++){
                for(int j=0;j<mBoard.getSize();j++){
                    switch (mBoard.getMatrix()[i][j]){
                        case Board.FRUIT:
                            d = getResources().getDrawable(R.drawable.red, null);
                            d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                            d.draw(canvas);
                            break;
                        case Board.PLAYER:
                            d = getResources().getDrawable(R.drawable.green, null);
                            d.setBounds((int)(i*tileSizeX), (int)(j*tileSizeY), (int)((i+1)*tileSizeX), (int)((j+1)*tileSizeY));
                            d.draw(canvas);
                            break;
                    }

//                    canvas.drawCircle(i*tileSizeX+tileSizeX/2f , j*tileSizeY+tileSizeY/2f,circleSize,mPaint);
                }
            }

        }
    }
}
