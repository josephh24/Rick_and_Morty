package com.example.rickandmorty.clases;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.rickandmorty.R;

public class Loader extends Dialog {

    private ImageView loading_image_view;

    public Loader(Context context) {
        super( context, R.style.ThemeOverlay_AppCompat_Dialog );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.cargando_dialog );
        setCancelable( false );

        loading_image_view = findViewById(R.id.loading_image_view);

        Glide.with(getContext()).asGif().load(R.drawable.gif_cargando).into(loading_image_view);

        Window window = getWindow();
        assert window != null;
        try {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}