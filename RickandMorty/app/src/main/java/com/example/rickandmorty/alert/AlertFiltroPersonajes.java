package com.example.rickandmorty.alert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rickandmorty.R;
import com.example.rickandmorty.validaciones.ResponseOperation;
import com.example.rickandmorty.validaciones.Validaciones;
import com.google.android.material.textfield.TextInputEditText;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AlertFiltroPersonajes extends Dialog implements View.OnClickListener {
    private ImageView alert_cancel_filtro_personaje;
    private Button btn_filtro;
    private NiceSpinner nice_spinner;
    private myOnClickListener myListener;
    private TextInputEditText txt_correo_login;
    private Validaciones validarCampos = new Validaciones();

    private String NOMBRE = "";
    private String STATUS = "";

    public AlertFiltroPersonajes(Context context, myOnClickListener listener) {
        super( context, R.style.Theme_AppCompat_Dialog );
        this.myListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_filtro_personajes);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

        alert_cancel_filtro_personaje = findViewById(R.id.alert_cancel_filtro_personaje);
        btn_filtro = findViewById(R.id.btn_filtro);
        nice_spinner = findViewById(R.id.nice_spinner);
        txt_correo_login = findViewById(R.id.txt_correo_login);

        alert_cancel_filtro_personaje.setOnClickListener(this);
        btn_filtro.setOnClickListener(this);

        List<String> dataset = new LinkedList<>(Arrays.asList(" " ,"alive", "dead", "unknown"));
        nice_spinner.attachDataSource(dataset);

        nice_spinner.setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            // This example uses String, but your type can be any
            STATUS = parent.getItemAtPosition(position).toString();
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alert_cancel_filtro_personaje:
                dismiss();
                break;

            case R.id.btn_filtro:
                NOMBRE = txt_correo_login.getText().toString();
                if (isValidForm(NOMBRE)){
                    myListener.onButtonClick(NOMBRE, STATUS);
                    dismiss();
                }
                break;

        }
    }

    public interface myOnClickListener {
        void onButtonClick(String nombre, String status);
    }

    public boolean isValidForm(String nombre) {
        boolean isValid = true;
        ResponseOperation responseOperation;

        responseOperation = validarCampos.validaNombre(nombre, getContext());
        if (!responseOperation.getStatus()) {
            isValid = false;
            txt_correo_login.setError(responseOperation.getTexResponse());
        }

        return isValid;
    }
}
