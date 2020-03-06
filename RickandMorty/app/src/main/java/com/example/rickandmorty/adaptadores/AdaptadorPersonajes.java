package com.example.rickandmorty.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.rickandmorty.R;
import com.example.rickandmorty.modelos.Results;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorPersonajes extends RecyclerView.Adapter<AdaptadorPersonajes.ViewHolder> implements View.OnClickListener {

    List<Results> listData;
    ArrayList<Results> listFull;

    private Context context;
    private View.OnClickListener listener;
    private AdapaterInterfazPersonajes adapaterInterfazPersonajes;

    public AdaptadorPersonajes(List<Results> listData, Context context, AdapaterInterfazPersonajes adapaterInterfazPersonajes) {
        this.listData = listData;
        this.listFull = new ArrayList<>(listData);
        this.context = context;
        this.adapaterInterfazPersonajes = adapaterInterfazPersonajes;
    }

    public Filter buscarPersonaje() {
        return retornoBusqueda;
    }

    private Filter retornoBusqueda = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence peticion) {

            //se instancia un arreglo con la lista de datos
            List<Results> listaFiltrada = new ArrayList<>();

            if (peticion == null || peticion.length() == 0) { // se valida si no hay texto en el searchview y llena la lista completa
                listaFiltrada.addAll(listFull);

            } else { //se obtiene el valor que el Usuario escribe en el campo de searchview
                String valor = peticion.toString().toLowerCase().trim();

                for (Results datos : listFull) { // se agregan los registros que concidan con la ciudad escrita a el arreglo
                    if (datos.getName().toLowerCase().contains(valor)) {
                        listaFiltrada.add(datos);
                    }
                }
            }

            // se envia una respuesta con los resultados del filtro
            FilterResults filterResults = new FilterResults();
            filterResults.values = listaFiltrada;
            filterResults.count = listaFiltrada.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            adapaterInterfazPersonajes.tamanioBusqueda(filterResults.count);

            listData.clear(); // se vacia la lista primero
            listData.addAll((List) filterResults.values); //se llena la lista de terminales con el arreglo de datos filtrados

            notifyDataSetChanged(); // verifica si hay cambios
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptador_personajes, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Results p = listData.get(position);
        holder.nombre_list_personaje.setText(p.getName());

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(p.getImage())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.img_list_personaje);
    }

    public interface AdapaterInterfazPersonajes {
        void tamanioBusqueda(int tamanio);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void adicionarListaPersonajes(ArrayList<Results> listaPersonajes) {
        listData.addAll(listaPersonajes);
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_list_personaje;
        TextView nombre_list_personaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_list_personaje = itemView.findViewById(R.id.img_list_personaje);
            nombre_list_personaje = itemView.findViewById(R.id.nombre_list_personaje);

        }
    }
}
