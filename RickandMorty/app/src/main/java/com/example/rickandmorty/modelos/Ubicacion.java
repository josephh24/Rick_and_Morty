package com.example.rickandmorty.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Ubicacion {

    @SerializedName("info")
    @Expose
    public Info info;

    @SerializedName("results")
    @Expose
    public ArrayList<Results_Ubicacion> results = null;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public ArrayList<Results_Ubicacion> getResults() {
        return results;
    }

    public void setResults(ArrayList<Results_Ubicacion> results) {
        this.results = results;
    }
}
