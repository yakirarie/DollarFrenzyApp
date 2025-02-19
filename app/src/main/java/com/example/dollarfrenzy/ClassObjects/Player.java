package com.example.dollarfrenzy.ClassObjects;

import android.content.Context;
import android.widget.Toast;

import java.util.Random;

public class Player {


    private int locationX;
    private int locationY;
    private Board b;
    private Context context;
    public static int playerScore = 0;

    public Player(Board b, Context context){
        this.context = context;
        this.b = new Board(b);
        addPlayer();
        addFruit();
    }
    private void addPlayer(){
        Random r = new Random();
        int x = r.nextInt(b.getSize());
        int y = r.nextInt(b.getSize());
        locationX = x;
        locationY = y;
        b.getMatrix()[x][y] = Board.PLAYER;
    }
    private void addFruit(){
        b.addFruit();
    }

    public Board getB() {
        return b;
    }

    public int checkBoard(){
        return b.checkBoard();
    }

    public int[] move(String command){
        switch (command) {
            case "UP":
                if (locationY-1<0){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return new int[]{0,0};
                }
                else{
                    int fruit = 0;
                    if(b.getMatrix()[locationX][locationY-1] == Board.FRUIT)
                        fruit = 1;
                    else if (b.getMatrix()[locationX][locationY-1] == Board.FRUIT3)
                        fruit = 2;
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationY--;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return new int[]{1,fruit};
                }
            case "DOWN":
                if (locationY+1==b.getSize()){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return new int[]{0,0};
                }
                else{
                    int fruit = 0;
                    if(b.getMatrix()[locationX][locationY+1] == Board.FRUIT )
                        fruit = 1;
                    else if ( b.getMatrix()[locationX][locationY+1] == Board.FRUIT3)
                        fruit = 2;
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationY++;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return new int[]{1,fruit};
                }
            case "LEFT":
                if (locationX-1<0){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return new int[]{0,0};
                }
                else{
                    int fruit = 0;
                    if(b.getMatrix()[locationX-1][locationY] == Board.FRUIT )
                        fruit = 1;
                    else if (b.getMatrix()[locationX-1][locationY] == Board.FRUIT3)
                        fruit = 2;
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationX--;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return new int[]{1,fruit};
                }
            case "RIGHT":
                if (locationX+1==b.getSize()){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return new int[]{0,0};
                }
                else{
                    int fruit = 0;
                    if(b.getMatrix()[locationX+1][locationY] == Board.FRUIT)
                        fruit = 1;
                    else if (b.getMatrix()[locationX+1][locationY] == Board.FRUIT3)
                        fruit = 2;
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationX++;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return new int[]{1,fruit};
                }
        }
        return new int[]{0,0};
    }
}
