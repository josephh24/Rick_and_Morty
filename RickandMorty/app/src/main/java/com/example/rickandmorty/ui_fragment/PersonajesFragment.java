package com.example.rickandmorty.ui_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rickandmorty.adaptadores.AdaptadorPersonajes;
import com.example.rickandmorty.alert.AlertFiltroPersonajes;
import com.example.rickandmorty.alert.AlertNoInternet;
import com.example.rickandmorty.activity.DetallesPersonajeActivity;
import com.example.rickandmorty.clases.Loader;
import com.example.rickandmorty.R;
import com.example.rickandmorty.api.ApiRickAndMorty;
import com.example.rickandmorty.modelos.Personajes;
import com.example.rickandmorty.modelos.Results;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonajesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv_personajes;
    private ImageView btn_filtro_personaje, anim_no_wifi_inicio;
    private SearchView search_personajes;
    private TextView sin_personajes;
    private RelativeLayout content_no_wifi;
    private Button btn_reintentar;

    private Retrofit retrofit;
    private LinearLayoutManager layoutManager;
    private AdaptadorPersonajes adaptadorPersonajes;

    private AlertNoInternet alertNoInternet;
    private AlertNoInternet.myOnClickListener myOnClickListener;
    private AlertFiltroPersonajes alertFiltroPersonajes;
    private AlertFiltroPersonajes.myOnClickListener onClickListener;

    private ArrayList<Results> listaPersonajes;
    ArrayList<String> milista_episodios;
    private Loader load;
    private boolean aptoParaCargar;
    private int pagina;
    private int total_paginas;
    private int pos_lista_adapter = 1;
    private String NOMBRE_FILTRO = "";
    private String STATUS_FILTRO = "";
    private boolean busqueda = false;


    public PersonajesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personajes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_personajes = view.findViewById(R.id.rv_personajes);
        btn_filtro_personaje = view.findViewById(R.id.btn_filtro_personaje);
        search_personajes = view.findViewById(R.id.search_personajes);
        sin_personajes = view.findViewById(R.id.sin_personajes);
        content_no_wifi = view.findViewById(R.id.content_no_wifi);
        btn_reintentar = view.findViewById(R.id.btn_reintentar);
        anim_no_wifi_inicio = view.findViewById(R.id.anim_no_wifi_inicio);

        btn_filtro_personaje.setOnClickListener(this);
        btn_reintentar.setOnClickListener(this);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.app_base))
                .addConverterFactory(GsonConverterFactory.create()).build();

        listaPersonajes = new ArrayList<>();
        milista_episodios = new ArrayList<String>();

        pagina = 1;
        alertInternet();
        load = new Loader(getContext());
        aptoParaCargar = true;
        obtenerPersonajes(pagina);

        // imagen circular de no internet
        Glide.with(getContext())
                .load(getContext().getResources().getDrawable(R.drawable.img_no_internet))
                .apply(RequestOptions.circleCropTransform())
                .into(anim_no_wifi_inicio);

    }

    /**
     * Jose Pablo Sanchez
     * Metodo que hace la consulta de todos los personajes
     */
    private void obtenerPersonajes(int pagina) {
        content_no_wifi.setVisibility(View.GONE);
        load.show();
        ApiRickAndMorty service = retrofit.create(ApiRickAndMorty.class);
        Call<Personajes> personajesCall = service.getPersonajes(String.valueOf(pagina));

        personajesCall.enqueue(new Callback<Personajes>() {
            @Override
            public void onResponse(@NonNull Call<Personajes> call,@NonNull Response<Personajes> response) {
                aptoParaCargar = true;
                if (response.body() != null) {
                    nextPage();
                    content_no_wifi.setVisibility(View.GONE);
                    load.dismiss();

                    Personajes personajes = response.body();
                    //ArrayList<Results> lista = personajes.getResults();
                    //adaptadorPersonajes.adicionarListaPersonajes(lista);

                    total_paginas = personajes.getInfo().getPages();

                    listaPersonajes.addAll(personajes.getResults());

                    for (int i = 0; i < personajes.getResults().size(); i++){
                        pos_lista_adapter = personajes.getResults().get(i).getId();
                    }

                    lista();
                    rv_personajes.scrollToPosition(pos_lista_adapter-22);


                } else {
                    load.dismiss();
                    if (listaPersonajes.isEmpty()){
                        content_no_wifi.setVisibility(View.VISIBLE);
                    } else {
                        if (!alertNoInternet.isShowing()){
                            alertNoInternet.show();
                        }
                    }
                    Log.d("jijijiji", "onResponseBody");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Personajes> call, @NonNull Throwable t) {
                aptoParaCargar = true;
                load.dismiss();
                if (listaPersonajes.isEmpty()){
                    content_no_wifi.setVisibility(View.VISIBLE);
                } else {
                    if (!alertNoInternet.isShowing()){
                        alertNoInternet.show();
                    }
                }
                Log.d("jijijiji", "onFail: " + t.getMessage());
            }
        });
    }

    /**
     * Jose Pablo Sanchez
     * Metodo que hace la consulta de personajes en base a filtros
     */
    private void obtenerFiltroBusqueda(int pagina, String nombre, String status) {
        content_no_wifi.setVisibility(View.GONE);
        load.show();
        ApiRickAndMorty service = retrofit.create(ApiRickAndMorty.class);
        Call<Personajes> personajesCall = service.getPersonajesFiltro(String.valueOf(pagina), nombre, status);

        personajesCall.enqueue(new Callback<Personajes>() {
            @Override
            public void onResponse(@NonNull Call<Personajes> call,@NonNull Response<Personajes> response) {
                aptoParaCargar = true;
                if (response.body() != null) {
                    nextPage();
                    content_no_wifi.setVisibility(View.GONE);
                    load.dismiss();

                    Personajes personajes = response.body();
                    total_paginas = personajes.getInfo().getPages();
                    listaPersonajes.addAll(personajes.getResults());

                    for (int i = 0; i < personajes.getResults().size(); i++){
                        pos_lista_adapter = personajes.getResults().get(i).getId();
                    }

                    lista();
                    rv_personajes.scrollToPosition(listaPersonajes.size()-22);

                    //Toast.makeText(getContext(), pagina+"", Toast.LENGTH_SHORT).show();


                } else {
                    load.dismiss();
                    if (busqueda){
                        Toast.makeText(getContext(), getString(R.string.sin_personajes), Toast.LENGTH_SHORT).show();
                        rv_personajes.setVisibility(View.GONE);
                        sin_personajes.setVisibility(View.VISIBLE);
                        alertFiltro();
                    } else {
                        if (listaPersonajes.isEmpty()){
                            content_no_wifi.setVisibility(View.VISIBLE);
                        } else {
                            if (!alertNoInternet.isShowing()){
                                alertNoInternet.show();
                            }
                        }
                    }
                    Log.d("jijijiji", "onResponseBody");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Personajes> call, @NonNull Throwable t) {
                aptoParaCargar = true;
                load.dismiss();
                if (listaPersonajes.isEmpty()){
                    content_no_wifi.setVisibility(View.VISIBLE);
                } else {
                    if (!alertNoInternet.isShowing()){
                        alertNoInternet.show();
                    }
                }
                Log.d("jijijiji", "onFail: " + t.getMessage());
            }
        });
    }

    private void lista() {
        //recycler
        rv_personajes.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 2);
        rv_personajes.setLayoutManager(layoutManager);

        adaptadorPersonajes = new AdaptadorPersonajes(listaPersonajes, getContext(), tamanio -> {

            if (tamanio == 0){
                rv_personajes.setVisibility(View.GONE);
                sin_personajes.setVisibility(View.VISIBLE);
            } else {
                rv_personajes.setVisibility(View.VISIBLE);
                sin_personajes.setVisibility(View.GONE);
            }
        });

        busquedaText();

        rv_personajes.setAdapter(adaptadorPersonajes);

        adaptadorPersonajes.setOnClickListener(view -> {
            int pos = rv_personajes.getChildLayoutPosition(view);

            Intent intent = new Intent(getContext(), DetallesPersonajeActivity.class);
            intent.putExtra("img", listaPersonajes.get(pos).getImage());
            intent.putExtra("nombre", listaPersonajes.get(pos).getName());
            intent.putExtra("id", listaPersonajes.get(pos).getId()+"");
            intent.putExtra("fecha", listaPersonajes.get(pos).getCreated());
            intent.putExtra("status", listaPersonajes.get(pos).getStatus());
            intent.putExtra("especie", listaPersonajes.get(pos).getSpecies());
            intent.putExtra("genero", listaPersonajes.get(pos).getGender());
            intent.putExtra("origen", listaPersonajes.get(pos).getOrigin().getName());
            intent.putExtra("ultimo_origen", listaPersonajes.get(pos).getLocation().getName());
            intent.putExtra("lista_episodios", milista_episodios = listaPersonajes.get(pos).getEpisode());
            startActivity(intent);
        });

        rv_personajes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (aptoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                            if (total_paginas >= pagina){
                                //fin
                                load.show();
                                aptoParaCargar = false;
                                if (busqueda){
                                    obtenerFiltroBusqueda(pagina, NOMBRE_FILTRO, STATUS_FILTRO);
                                } else {
                                    obtenerPersonajes(pagina);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_filtro_personaje:
                alertFiltro();
                break;

            case R.id.btn_reintentar:
                aptoParaCargar = true;
                obtenerPersonajes(pagina);
                break;
        }
    }

    private void nextPage(){
        pagina ++;
    }

    private void alertInternet(){
        // alert internet
        myOnClickListener = () -> {
            obtenerPersonajes(pagina);
        };
        alertNoInternet = new AlertNoInternet(getContext(), myOnClickListener);
    }

    private void alertFiltro(){
        //alert filtro busqueda
        onClickListener = (nombre, status) -> {
            sin_personajes.setVisibility(View.GONE);
            rv_personajes.setVisibility(View.VISIBLE);
            busqueda = true;
            NOMBRE_FILTRO = nombre;
            STATUS_FILTRO = status;
            listaPersonajes.clear();
            pagina = 1;
            obtenerFiltroBusqueda(pagina, nombre, status);
        };
        alertFiltroPersonajes = new AlertFiltroPersonajes(getContext(), onClickListener);
        alertFiltroPersonajes.show();
    }

    private void busquedaText(){
        search_personajes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adaptadorPersonajes.buscarPersonaje().filter(s);
                return false;
            }
        });
    }
}
