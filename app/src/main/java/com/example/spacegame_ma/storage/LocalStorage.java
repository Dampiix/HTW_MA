package com.example.spacegame_ma.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage implements LocalStorageI {

    private SharedPreferences pref;

    public LocalStorage(Context context){
        pref = context.getSharedPreferences("game", Context.MODE_PRIVATE);
    }

    @Override
    public boolean compareScore(int score) {
        if (pref.getInt("highscore", 0) < score) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void saveNewHighscore(int score) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("highscore", score);
            editor.apply();
    }


}
