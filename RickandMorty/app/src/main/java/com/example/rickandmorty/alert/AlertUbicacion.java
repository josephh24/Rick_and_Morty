package com.example.rickandmorty.alert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rickandmorty.R;

public class AlertUbicacion extends Dialog implements View.OnClickListener {
    private ImageView alert_cancel_ubicacion;
    private TextView id_ubicacion, fecha_ubicacion, nombre_ubicacion, tipo_ubicacion, dimension_ubicacion;
    private String ID, FECHA, NOMBRE, TIPO, DIMENSION;

    public AlertUbicacion(Context context, String id, String fecha, String nombre, String tipo, String dimension) {
        super( context, R.style.Theme_AppCompat_Dialog );
        this.ID = id;
        this.FECHA = fecha;
        this.NOMBRE = nombre;
        this.TIPO = tipo;
        this.DIMENSION = dimension;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_ubicacion);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

        alert_cancel_ubicacion = findViewById(R.id.alert_cancel_ubicacion);
        id_ubicacion = findViewById(R.id.id_ubicacion);
        fecha_ubicacion = findViewById(R.id.fecha_ubicacion);
        nombre_ubicacion = findViewById(R.id.nombre_ubicacion);
        tipo_ubicacion = findViewById(R.id.tipo_ubicacion);
        dimension_ubicacion = findViewById(R.id.dimension_ubicacion);

        alert_cancel_ubicacion.setOnClickListener(this);

        id_ubicacion.setText(ID);
        fecha_ubicacion.setText(FECHA);
        nombre_ubicacion.setText(NOMBRE);
        tipo_ubicacion.setText(TIPO);
        dimension_ubicacion.setText(DIMENSION);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alert_cancel_ubicacion:
                dismiss();
                break;
        }
    }
}
