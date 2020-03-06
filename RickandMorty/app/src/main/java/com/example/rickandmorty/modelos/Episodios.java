package com.example.rickandmorty.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Episodios {

    @SerializedName("info")
    @Expose
    public Info info;

    @SerializedName("results")
    @Expose
    public ArrayList<Results_Episodios> results = null;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public ArrayList<Results_Episodios> getResults() {
        return results;
    }

    public void setResults(ArrayList<Results_Episodios> results) {
        this.results = results;
    }
}
