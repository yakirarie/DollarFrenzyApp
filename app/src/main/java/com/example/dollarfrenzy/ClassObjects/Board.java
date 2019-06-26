package com.example.dollarfrenzy.ClassObjects;


import java.util.Random;

public class Board {
    public static final char FRUIT  = '$';
    public static final char EMPTY  = '*';
    public static final char PLAYER = 'P';
    public static final char WALLS_LR = '|';
    public static final char WALLS_UD = '-';

    private int size;
    private char[][] matrix;

    public Board(int size){
        this.size = size;
        this.matrix = new char[size][size];
        for (int i = 0; i < size; i++){
            matrix[i][0] = WALLS_LR;
            matrix[i][size-1] = WALLS_LR;
        }
        for (int j = 1; j < size; j++){
            matrix[0][j] = WALLS_UD;
            matrix[size-1][0] = WALLS_UD;
        }
        for (int i = 1; i < size-1; i++)
            for (int j = 1; j < size-1; j++)
                matrix[i][j] = EMPTY;
    }

    public Board(Board b){
        this.size = b.size;
        this.matrix = new char[b.size][b.size];
        for (int i = 0; i < b.size; i++)
            for (int j = 0; j < b.size; j++)
                matrix[i][j] = b.getMatrix()[i][j];
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public int getSize() {
        return size;
    }

    public void addFruit(){
        Random r = new Random();
        int x = r.nextInt(size-2)+1;
        int y = r.nextInt(size-2)+1;
        while (matrix[x][y] == PLAYER || matrix[x][y] == FRUIT){
            x = r.nextInt(size-2)+1;
            y = r.nextInt(size-2)+1;
        }
        matrix[x][y] = FRUIT;
    }

    boolean checkBoard(){
        int num_of_fruit = 0;
        for(int i =0;i<size;i++){
            for(int j = 0;j<size ;j++){
                if (matrix[i][j]==FRUIT)
                    num_of_fruit++;
            }
        }
        return num_of_fruit>=(size-2)*(size-2)*0.5;
    }

}
