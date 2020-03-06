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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rickandmorty.R;

public class AlertNoInternet extends Dialog implements View.OnClickListener {
    private ImageView alert_cancel;
    private Button btn_reintentar_alert;
    private ImageView anim_no_wifi;
    private myOnClickListener myListener;

    public AlertNoInternet(Context context, myOnClickListener listener) {
        super( context, R.style.Theme_AppCompat_Dialog );
        this.myListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_no_internet);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);

        alert_cancel = findViewById(R.id.alert_cancel);
        btn_reintentar_alert = findViewById(R.id.btn_reintentar_alert);
        anim_no_wifi = findViewById(R.id.anim_no_wifi);

        alert_cancel.setOnClickListener(this);
        btn_reintentar_alert.setOnClickListener(this);

        Glide.with(getContext())
                .load(getContext().getResources().getDrawable(R.drawable.img_no_internet))
                .apply(RequestOptions.circleCropTransform())
                .into(anim_no_wifi);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alert_cancel:
                dismiss();
                break;

            case R.id.btn_reintentar_alert:
                myListener.onButtonClick();
                dismiss();
                break;

        }
    }

    public interface myOnClickListener {
        void onButtonClick();
    }
}
