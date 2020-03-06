package com.example.rickandmorty.modelos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Results {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("species")
    @Expose
    public String species;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("gender")
    @Expose
    public String gender;

    @SerializedName("origin")
    @Expose
    public Origin origin;

    @SerializedName("location")
    @Expose
    public Location location;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("episode")
    @Expose
    public ArrayList<String> episode = null;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getEpisode() {
        return episode;
    }

    public void setEpisode(ArrayList<String> episode) {
        this.episode = episode;
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
