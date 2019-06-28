package com.example.dollarfrenzy.ClassObjects;

import android.content.Context;
import android.widget.Toast;

import java.util.Random;

public class Player {


    private int locationX;
    private int locationY;
    private Board b;
    private Context context;
    public static int turns = 0;

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

    public boolean checkBoard(){
        return b.checkBoard();
    }

    public boolean move(String command){
        switch (command) {
            case "UP":
                if (locationY-1<0){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return false;
                }
                else{
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationY--;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return true;
                }
            case "DOWN":
                if (locationY+1==b.getSize()){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return false;
                }
                else{
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationY++;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return true;
                }
            case "LEFT":
                if (locationX-1<0){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return false;
                }
                else{
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationX--;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return true;
                }
            case "RIGHT":
                if (locationX+1==b.getSize()){
                    Toast.makeText(context,"You cant move there!",Toast.LENGTH_LONG).show();
                    return false;
                }
                else{
                    b.getMatrix()[locationX][locationY] = Board.EMPTY;
                    locationX++;
                    b.getMatrix()[locationX][locationY] = Board.PLAYER;
                    return true;
                }
        }
        return false;
    }
}
