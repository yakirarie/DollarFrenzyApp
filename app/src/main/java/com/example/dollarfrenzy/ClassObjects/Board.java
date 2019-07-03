package com.example.dollarfrenzy.ClassObjects;


import java.util.Random;

public class Board {
    public static final char FRUIT  = '$';
    public static final char EMPTY  = '*';
    public static final char PLAYER = 'P';

    private int size;
    private char[][] matrix;

    public Board(int size){
        this.size = size;
        this.matrix = new char[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
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
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        while (matrix[x][y] == PLAYER || matrix[x][y] == FRUIT){
            x = r.nextInt(size);
            y = r.nextInt(size);
        }
        matrix[x][y] = FRUIT;
    }

    public int checkBoard(){
        int num_of_fruit = 0;
        for(int i =0;i<size;i++){
            for(int j = 0;j<size ;j++){
                if (matrix[i][j]==FRUIT)
                    num_of_fruit++;
            }
        }
        if(num_of_fruit>=size*size*0.5)
            return 1;
        if(num_of_fruit>=size*size*0.4)
            return 0;
        return -1;
    }

}
