package com.example.rickandmorty.ui_fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.rickandmorty.activity.MainActivity;
import com.example.rickandmorty.R;
import com.example.rickandmorty.clases.SharedPref;

public class MenuFragment extends Fragment {

    private Switch myswitch;
    private CardView ir_linkedin;
    private LinearLayout creditos_cont;
    AnimationDrawable animationDrawable;

    SharedPref sharedpref;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        sharedpref = new SharedPref(getContext());
        super.onViewCreated(view, savedInstanceState);

        myswitch = view.findViewById(R.id.myswitch);
        ir_linkedin = view.findViewById(R.id.ir_linkedin);
        creditos_cont = view.findViewById(R.id.creditos_cont);

        setRippleDrawable(ir_linkedin, getResources().getColor(R.color.colorNav_inactivo), getResources().getColor(R.color.colorNav_inactivo));

        // animacion creditos
        creditos_cont.setBackgroundResource(R.drawable.animation_design_body_new);
        animationDrawable = (AnimationDrawable) creditos_cont.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        if (sharedpref.loadNightModeState()){
            myswitch.setChecked(true);
        }

        myswitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                sharedpref.setNightModeState(true);
                restartApp();
            } else {
                sharedpref.setNightModeState(false);
                restartApp();
            }
        });

        ir_linkedin.setOnClickListener(view1 -> {
            String url = getString(R.string.linkedin_url);
            Intent intentLinkedIn = new Intent(Intent.ACTION_VIEW);
            intentLinkedIn.setData(Uri.parse(url));
            startActivity(intentLinkedIn);
        });

        creditos_cont.setOnClickListener(view1 -> {
            String url = getString(R.string.flink_url);
            Intent intentLinkedIn = new Intent(Intent.ACTION_VIEW);
            intentLinkedIn.setData(Uri.parse(url));
            startActivity(intentLinkedIn);
        });
    }

    private void restartApp() {
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
        getActivity().getFragmentManager().popBackStack();
    }

    public static void setRippleDrawable(View view, int normalColor, int touchColor) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(touchColor), view.getBackground(), null);
                view.setBackground(rippleDrawable);
            } else {
                StateListDrawable stateListDrawable = new StateListDrawable();
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(touchColor));
                stateListDrawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(touchColor));
                stateListDrawable.addState(new int[]{}, new ColorDrawable(normalColor));
                view.setBackground(stateListDrawable);
            }
        } catch (Exception e) {
            Log.e("error", "" + e);
        }
    }
}
