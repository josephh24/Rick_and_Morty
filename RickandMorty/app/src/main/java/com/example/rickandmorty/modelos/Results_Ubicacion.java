package com.example.rickandmorty.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results_Ubicacion {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("dimension")
    @Expose
    public String dimension;

    @SerializedName("residents")
    @Expose
    public List<String> residents = null;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("created")
    @Expose
    public String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public List<String> getResidents() {
        return residents;
    }

    public void setResidents(List<String> residents) {
        this.residents = residents;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
