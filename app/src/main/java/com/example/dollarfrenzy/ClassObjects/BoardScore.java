package com.example.dollarfrenzy.ClassObjects;

import java.util.HashMap;
import java.util.Map;

public class BoardScore {
    private Map<String,Integer> Score = new HashMap<>();

    public BoardScore(){
        Score.put("3X3",0);
        Score.put("4X4",0);
        Score.put("5X5",0);
        Score.put("6X6",0);
        Score.put("7X7",0);
        Score.put("8X8",0);
        Score.put("9X9",0);
        Score.put("10X10",0);
        Score.put("11X11",0);
        Score.put("12X12",0);
        Score.put("13X13",0);
        Score.put("14X14",0);
        Score.put("15X15",0);
        Score.put("16X16",0);
        Score.put("17X17",0);
        Score.put("18X18",0);
        Score.put("19X19",0);
        Score.put("20X20",0);

    }


    public BoardScore(BoardScore bs){
        for (String s : bs.getScore().keySet())
            Score.put(s,bs.getScore().get(s));
    }

    public Map<String, Integer> getScore() {
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
