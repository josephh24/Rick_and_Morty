package com.example.rickandmorty.api;

import com.example.rickandmorty.modelos.Personajes;
import com.example.rickandmorty.modelos.Ubicacion;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRickAndMorty {

    String PERSONAJES = "character";
    String LOCALIZACION = "location";

    @GET(PERSONAJES)
    Call<Personajes> getPersonajes(@Query("page") String pagina);

    @GET(PERSONAJES)
    Call<Personajes> getPersonajesFiltro(@Query("page") String pagina, @Query("name") String nombre, @Query("status") String status);

    @GET(LOCALIZACION)
    Call<Ubicacion> getLocalizacion(@Query("name") String ubicacion);

}
