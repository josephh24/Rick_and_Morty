package com.example.rickandmorty.ui_fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rickandmorty.R;
import com.example.rickandmorty.activity.DetallesPersonajeActivity;
import com.example.rickandmorty.adaptadores.AdaptadorPersonajes;
import com.example.rickandmorty.clases.Utilidades;
import com.example.rickandmorty.modelos.Location;
import com.example.rickandmorty.modelos.Origin;
import com.example.rickandmorty.modelos.Results;
import com.example.rickandmorty.sqlite.ConexionSQLiteHelper;

import java.util.ArrayList;

public class FavoritosFragment extends Fragment {

    private SwipeRefreshLayout refresca_guardadas;
    private RecyclerView rv_personajes_guardadas;
    private SearchView search_personajes_fav;
    private TextView sin_personajes_fav;
    private ConexionSQLiteHelper conn;

    private LinearLayoutManager layoutManager;
    private AdaptadorPersonajes adaptadorPersonajes;
    private ArrayList<Results> listaPersonajes;

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favoritos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refresca_guardadas = view.findViewById(R.id.refresca_guardadas);
        rv_personajes_guardadas = view.findViewById(R.id.rv_personajes_guardadas);
        search_personajes_fav = view.findViewById(R.id.search_personajes_fav);
        sin_personajes_fav = view.findViewById(R.id.sin_personajes_fav);

        conn = new ConexionSQLiteHelper(getContext(), Utilidades.NOMBRE_BD, null, 1);
        consultarListaPersonajesGuardadas();

        refresca_guardadas.setColorSchemeResources(
                R.color.colorNav_inactivo,
                R.color.colorNav_activo,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        refresca_guardadas.setOnRefreshListener(this::consultarListaPersonajesGuardadas);

    }

    private void consultarListaPersonajesGuardadas() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Results personajes;
        Origin origin;
        Location location;
        listaPersonajes = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_PERSONAJES, null);

        while (cursor.moveToNext()){
            origin = new Origin();
            location = new Location();
            personajes = new Results();

            origin.setName(cursor.getString(6));
            location.setName(cursor.getString(7));

            personajes.setId(cursor.getInt(0));
            personajes.setName(cursor.getString(1));
            personajes.setCreated(cursor.getString(2));
            personajes.setStatus(cursor.getString(3));
            personajes.setSpecies(cursor.getString(4));
            personajes.setGender(cursor.getString(5));
            personajes.setOrigin(origin);
            personajes.setLocation(location);
            personajes.setImage(cursor.getString(8));

            listaPersonajes.add(personajes);
        }
        db.close();
        refresca_guardadas.setRefreshing(false);

        if (listaPersonajes.isEmpty()){
            rv_personajes_guardadas.setVisibility(View.GONE);
            sin_personajes_fav.setVisibility(View.VISIBLE);
        } else {
            rv_personajes_guardadas.setVisibility(View.VISIBLE);
            sin_personajes_fav.setVisibility(View.GONE);
        }

        lista();
    }

    private void lista() {
        //recycler
        rv_personajes_guardadas.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 2);
        rv_personajes_guardadas.setLayoutManager(layoutManager);

        adaptadorPersonajes = new AdaptadorPersonajes(listaPersonajes, getContext(), tamanio -> {
            if (tamanio == 0){
                rv_personajes_guardadas.setVisibility(View.GONE);
                sin_personajes_fav.setVisibility(View.VISIBLE);
            } else {
                rv_personajes_guardadas.setVisibility(View.VISIBLE);
                sin_personajes_fav.setVisibility(View.GONE);
            }
        });

        busquedaText();

        rv_personajes_guardadas.setAdapter(adaptadorPersonajes);

        adaptadorPersonajes.setOnClickListener(view -> {
            int pos = rv_personajes_guardadas.getChildLayoutPosition(view);

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
            //intent.putExtra("lista_episodios", milista_episodios = listaPersonajes.get(pos).getEpisode());
            startActivity(intent);
        });
    }

    private void busquedaText(){
        search_personajes_fav.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    @Override
    public void onResume() {
        super.onResume();
        listaPersonajes.clear();
        consultarListaPersonajesGuardadas();
    }
}
