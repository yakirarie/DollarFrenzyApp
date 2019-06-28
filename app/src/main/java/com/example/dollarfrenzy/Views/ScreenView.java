package com.example.dollarfrenzy.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.dollarfrenzy.ClassObjects.Board;
import com.example.dollarfrenzy.R;

public class ScreenView extends View {
    private Paint mPaint = new Paint();
    private Board mBoard;

    public ScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setmBoard(Board b){ mBoard = new Board(b);}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mBoard != null){
            float tileSizeX = (float)getWidth()/mBoard.getSize();
            float tileSizeY = (float)getHeight()/mBoard.getSize();
            float circleSize = Math.min(tileSizeX,tileSizeY)/2;
            for(int i=0 ;i<mBoard.getSize();i++){
                for(int j=0;j<mBoard.getSize();j++){
                    switch (mBoard.getMatrix()[i][j]){
                        case Board.EMPTY:
                            mPaint.setColor(ContextCompat.getColor(getContext(),R.color.canvas));
                            break;
                        case Board.FRUIT:
                            mPaint.setColor(Color.RED);
                            break;
                        case Board.PLAYER:
                            mPaint.setColor(Color.GREEN);
                            break;
                    }

                    canvas.drawCircle(i*tileSizeX+tileSizeX/2f , j*tileSizeY+tileSizeY/2f,circleSize,mPaint);
                }
            }

        }
    }
}
