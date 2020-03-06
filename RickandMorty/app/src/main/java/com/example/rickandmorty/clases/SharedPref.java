package com.example.rickandmorty.clases;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences mySharePref;
    public static final String NIGHTMODE="NightMode";

    public static int contador_pub = 0;

    public SharedPref(Context context){
        mySharePref = context.getSharedPreferences("preferencias_rick_and_morty", Context.MODE_PRIVATE);
    }
    //this method will save the nightMode state : True or False
    public void setNightModeState(boolean state){
        SharedPreferences.Editor editor = mySharePref.edit();
        editor.putBoolean(NIGHTMODE, state);
        editor.commit();
    }
    //this method will load night mode state
    public boolean loadNightModeState(){
        boolean state = mySharePref.getBoolean(NIGHTMODE, false);
        return state;
    }
}
