package com.example.rickandmorty.validaciones;

import android.content.Context;

import com.example.rickandmorty.R;

import java.util.regex.Pattern;

public class Validaciones {

    private String REGEX_NOMBRE = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ](?!.*([\\s])\\1)([a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*)+[a-zA-ZáéíóúÁÉÍÓÚñÑ]$";

    /**
     * Jose Pablo Sanchez
     * Metodo que valida el campo de enombre
     */
    public ResponseOperation validaNombre(String nombre, Context context) {
        Pattern patron = Pattern.compile(REGEX_NOMBRE);
        if (nombre.isEmpty()) {// validacion de campo vacio
            return new ResponseOperation(true, context.getString(R.string.validacion_correcto));
        }  else if (!patron.matcher(nombre).matches()){
            return new ResponseOperation(false, context.getString(R.string.validacion_formato));
        }
        return new ResponseOperation(true, context.getString(R.string.validacion_correcto));
    }
}
