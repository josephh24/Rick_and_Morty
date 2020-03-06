package com.example.rickandmorty.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rickandmorty.R;
import com.example.rickandmorty.alert.AlertNoInternet;
import com.example.rickandmorty.alert.AlertUbicacion;
import com.example.rickandmorty.api.ApiRickAndMorty;
import com.example.rickandmorty.clases.Loader;
import com.example.rickandmorty.clases.SharedPref;
import com.example.rickandmorty.clases.Utilidades;
import com.example.rickandmorty.modelos.Personajes;
import com.example.rickandmorty.modelos.Ubicacion;
import com.example.rickandmorty.sqlite.ConexionSQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallesPersonajeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView volver_detalle_personaje, fav_detalle_personaje, img_personaje;
    private TextView nombre_personaje, id_personaje, fecha_personaje, status_personaje, especie_personaje, genero_personaje,
            origen_personaje, ubicacion_actual_personaje;
    private LinearLayout ver_origen, ver_ubicacion;

    SharedPref sharedpref;
    private Retrofit retrofit;
    private Loader load;
    private AlertUbicacion alertUbicacion;
    private AlertNoInternet alertNoInternet;
    private AlertNoInternet.myOnClickListener myOnClickListener;

    private String IMG = "";
    private String NOMBRE = "";
    private String ID = "";
    private String FECHA = "";
    private String STATUS = "";
    private String ESPECIE = "";
    private String GENERO = "";
    private String ORIGEN = "";
    private String UBICACION = "";
    private ArrayList<String> lista_episodios;

    public final Calendar c = Calendar.getInstance();

    private boolean btn_origen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref = new SharedPref(this);
        if (sharedpref.loadNightModeState()){
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_personaje);

        volver_detalle_personaje = findViewById(R.id.volver_detalle_personaje);
        fav_detalle_personaje = findViewById(R.id.fav_detalle_personaje);
        img_personaje = findViewById(R.id.img_personaje);
        nombre_personaje = findViewById(R.id.nombre_personaje);
        id_personaje = findViewById(R.id.id_personaje);
        fecha_personaje = findViewById(R.id.fecha_personaje);
        status_personaje = findViewById(R.id.status_personaje);
        especie_personaje = findViewById(R.id.especie_personaje);
        genero_personaje = findViewById(R.id.genero_personaje);
        origen_personaje = findViewById(R.id.origen_personaje);
        ubicacion_actual_personaje = findViewById(R.id.ubicacion_actual_personaje);
        ver_origen = findViewById(R.id.ver_origen);
        ver_ubicacion = findViewById(R.id.ver_ubicacion);

        volver_detalle_personaje.setOnClickListener(this);
        fav_detalle_personaje.setOnClickListener(this);
        ver_origen.setOnClickListener(this);
        ver_ubicacion.setOnClickListener(this);

        IMG = getIntent().getStringExtra("img");
        NOMBRE = getIntent().getStringExtra("nombre");
        ID = getIntent().getStringExtra("id");
        FECHA = getIntent().getStringExtra("fecha");
        STATUS = getIntent().getStringExtra("status");
        ESPECIE = getIntent().getStringExtra("especie");
        GENERO = getIntent().getStringExtra("genero");
        ORIGEN = getIntent().getStringExtra("origen");
        UBICACION = getIntent().getStringExtra("ultimo_origen");
        lista_episodios = getIntent().getStringArrayListExtra("lista_episodios");

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.app_base))
                .addConverterFactory(GsonConverterFactory.create()).build();

        load = new Loader(this);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();

        Glide.with(this)
                .load(IMG)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(img_personaje);

        nombre_personaje.setText(NOMBRE);
        id_personaje.setText(ID);
        fecha_personaje.setText(obtenerFecha(FECHA));
        status_personaje.setText(STATUS);
        especie_personaje.setText(ESPECIE);
        genero_personaje.setText(GENERO);
        origen_personaje.setText(ORIGEN);
        ubicacion_actual_personaje.setText(UBICACION);

        validaIcono();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.volver_detalle_personaje:
                onBackPressed();
                break;

            case R.id.fav_detalle_personaje:
                if (!validaPersonajeGuardado()){
                    guardarPersonaje();
                } else {
                    borrarPersonaje();
                }
                break;

            case R.id.ver_origen:
                btn_origen = true;
                obtenerUbicacion(ORIGEN);

                break;

            case  R.id.ver_ubicacion:
                btn_origen = false;
                obtenerUbicacion(UBICACION);
                break;
        }
    }

    /**
     * Jose Pablo Sanchez
     * Metodo que hace la consulta de la ubicacion
     */
    private void obtenerUbicacion(String ubicacion) {
        load.show();
        ApiRickAndMorty service = retrofit.create(ApiRickAndMorty.class);
        Call<Ubicacion> ubicacionCall = service.getLocalizacion(ubicacion);

        ubicacionCall.enqueue(new Callback<Ubicacion>() {
            @Override
            public void onResponse(@NonNull Call<Ubicacion> call, @NonNull Response<Ubicacion> response) {
                if (response.body() != null) {
                    load.dismiss();
                    Ubicacion ubicacion = response.body();

                    String id = ubicacion.getResults().get(0).getId()+"";
                    String fecha = ubicacion.getResults().get(0).getCreated();
                    String nombre = ubicacion.getResults().get(0).getName();
                    String tipo = ubicacion.getResults().get(0).getType();
                    String dimension = ubicacion.getResults().get(0).getDimension();

                    alertUbicacion = new AlertUbicacion(DetallesPersonajeActivity.this, id, obtenerFecha(fecha), nombre, tipo, dimension);
                    alertUbicacion.show();


                } else {
                    load.dismiss();
                    alertInternet();
                    Log.d("jijijiji", "onResponseBody");
                }

            }

            @Override
            public void onFailure(@NonNull Call<Ubicacion> call, @NonNull Throwable t) {
                load.dismiss();
                alertInternet();
                Log.d("jijijiji", "onFail: " + t.getMessage());

            }
        });
    }

    private String obtenerFecha(String fecha){
        String[] arrayFechaCompleta = fecha.split("T");

        //guardar la fecha
        String fecha_sin_formato = arrayFechaCompleta[0];

        //dividir la fecha sin formato
        String[] arrayFecha = fecha_sin_formato.split("-");

        //convertir cada valor a entero
        int dia = Integer.parseInt(arrayFecha[2]);
        int mes = Integer.parseInt(arrayFecha[1]);
        int anio = Integer.parseInt(arrayFecha[0]);

        //convertimos el campo fecha a tipo calendar
        c.set(anio, mes - 1, dia);

        String time_dia = new SimpleDateFormat("dd", Locale.getDefault()).format(c.getTime()); // obtenemo el dia numerico
        String time_mes = new SimpleDateFormat("MMM", Locale.getDefault()).format(c.getTime()); // obtenemos el mes en texto
        String time_anio = new SimpleDateFormat("yy", Locale.getDefault()).format(c.getTime()); // obtenemos el año numerico

        return time_dia + "/" + time_mes + "/" + time_anio; //introducimos el resultado en el campo fecha
    }

    private void validaIcono(){
        if (validaPersonajeGuardado()){
            fav_detalle_personaje.setImageResource(R.drawable.ic_fav_select);
        } else {
            fav_detalle_personaje.setImageResource(R.drawable.ic_like);
        }
    }

    private boolean validaPersonajeGuardado(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getApplicationContext(), Utilidades.NOMBRE_BD,null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {ID};
        String[] campos = {Utilidades.CAMPO_ID};

        try { // ya esta guardado
            Cursor cursor = db.query(Utilidades.TABLA_PERSONAJES, campos, Utilidades.CAMPO_ID + "=?", parametros, null, null, null);
            cursor.moveToFirst();

            Log.d("jijijiji", cursor.getString(0));

            cursor.close();
            return true;
        } catch (Exception e){ // el documento no existe
            return false;
        }
    }

    private void guardarPersonaje(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getApplicationContext(), Utilidades.NOMBRE_BD,null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_ID, ID);
        values.put(Utilidades.CAMPO_NOMBRE, NOMBRE);
        values.put(Utilidades.CAMPO_FECHA, FECHA);
        values.put(Utilidades.CAMPO_STATUS, STATUS);
        values.put(Utilidades.CAMPO_ESPECIE, ESPECIE);
        values.put(Utilidades.CAMPO_GENERO, GENERO);
        values.put(Utilidades.CAMPO_ORIGEN, ORIGEN);
        values.put(Utilidades.CAMPO_UBICACION, UBICACION);
        values.put(Utilidades.CAMPO_IMG, IMG);

        db.insert(Utilidades.TABLA_PERSONAJES,Utilidades.CAMPO_ID,values);
        db.close();

        Toast.makeText(getApplicationContext(), getString(R.string.guardado), Toast.LENGTH_SHORT).show();

        validaIcono();
    }

    private void borrarPersonaje() {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getApplicationContext(), Utilidades.NOMBRE_BD,null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {ID};

        db.delete(Utilidades.TABLA_PERSONAJES, Utilidades.CAMPO_ID + "=?", parametros);
        db.close();

        Toast.makeText(getApplicationContext(), getString(R.string.borrado), Toast.LENGTH_SHORT).show();

        validaIcono();
    }

    private void alertInternet(){
        // alert internet
        myOnClickListener = () -> {
            if (btn_origen){
                obtenerUbicacion(ORIGEN);
            } else {
                obtenerUbicacion(UBICACION);
            }
        };
        alertNoInternet = new AlertNoInternet(this, myOnClickListener);
        alertNoInternet.show();
    }
}
