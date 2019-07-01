package com.example.dollarfrenzy.ClassObjects;

import java.util.HashMap;
import java.util.Map;

public class BoardScore {
    private HashMap<String,Integer> Score;

    public BoardScore(){

    }


    public BoardScore(BoardScore bs){
        this.Score = bs.Score;
    }
    public BoardScore(HashMap<String,Integer> map){
        Score = map;
    }
    public HashMap<String, Integer> getScore() {
        return Score;
    }

    public void copy(HashMap<String,Integer> map){
        if (map!=null){
            for (String s : map.keySet())
                if(map.get(s)==null)
                    Score.put(s,0);
                else
                    Score.put(s,map.get(s));
        }
    }
}
