package com.example.rickandmorty.clases;

public class Utilidades {

    //constantes campos tabla Personajes
    public static final String NOMBRE_BD="rick_and_morty_bd";
    public static final String TABLA_PERSONAJES = "Personajes";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_STATUS = "status";
    public static final String CAMPO_ESPECIE = "especie";
    public static final String CAMPO_GENERO = "genero";
    public static final String CAMPO_ORIGEN = "origen";
    public static final String CAMPO_UBICACION = "ubicacion";
    public static final String CAMPO_IMG = "imagen";

    public static final String CREAR_TABLA_PERSONAJES = "CREATE TABLE " + TABLA_PERSONAJES + "" +
            "(" + CAMPO_ID + " INTEGER, " + CAMPO_NOMBRE + " TEXT, " + CAMPO_FECHA + " TEXT, " +
            CAMPO_STATUS + " TEXT, " + CAMPO_ESPECIE + " TEXT, " + CAMPO_GENERO + " TEXT, " +
            CAMPO_ORIGEN + " TEXT, " + CAMPO_UBICACION + " TEXT, " + CAMPO_IMG + " TEXT)";
}
