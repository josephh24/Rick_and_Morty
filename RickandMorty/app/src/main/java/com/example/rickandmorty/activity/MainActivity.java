package com.example.rickandmorty.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.rickandmorty.adaptadores.MyPagerAdapter;
import com.example.rickandmorty.R;
import com.example.rickandmorty.clases.SharedPref;
import com.example.rickandmorty.clases.ZoomOutTransformation;
import com.example.rickandmorty.ui_fragment.MenuFragment;
import com.example.rickandmorty.ui_fragment.PersonajesFragment;
import com.example.rickandmorty.ui_fragment.FavoritosFragment;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;

public class MainActivity extends AppCompatActivity {

    private ViewPager view_pager;
    private MyPagerAdapter pagerAdapter;
    private BubbleNavigationLinearView bottom_navigation_view_linear;

    SharedPref sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref = new SharedPref(this);
        if (sharedpref.loadNightModeState()){
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_pager = findViewById(R.id.view_pager);
        bottom_navigation_view_linear = findViewById(R.id.bottom_navigation_view_linear);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        addingFragmentsTOpagerAdapter();
        view_pager.setAdapter(pagerAdapter);
        view_pager.setCurrentItem(1);

        ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) { // valida en que fragmento se encuentra
                bottom_navigation_view_linear.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        };

        bottom_navigation_view_linear.setNavigationChangeListener((view, position) -> view_pager.setCurrentItem(position, true));

        view_pager.addOnPageChangeListener(viewListener);


        ZoomOutTransformation zoomOutTransformation = new ZoomOutTransformation();
        view_pager.setPageTransformer(true, zoomOutTransformation);

        /*
        HingeTransformation hingeTransformation = new HingeTransformation();
        view_pager.setPageTransformer(true, hingeTransformation);
         */
    }

    private void addingFragmentsTOpagerAdapter() {
        pagerAdapter.addFragments(new FavoritosFragment());
        pagerAdapter.addFragments(new PersonajesFragment());
        pagerAdapter.addFragments(new MenuFragment());
    }
}
