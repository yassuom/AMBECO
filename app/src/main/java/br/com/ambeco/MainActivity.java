package br.com.ambeco;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.dao.LocalDAO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean box1Enabled = false;

    private boolean box2Enabled = false;

    private boolean box3Enabled = false;

    private boolean box4Enabled = false;

    private boolean isMapOnScreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(Boolean.FALSE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new LocaisMapaFragment());

        Button novo_local = (Button) findViewById(R.id.novo_local);
        novo_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInsertLocal = new Intent(MainActivity.this, CadastraLocalActivity.class);
                startActivity(intentInsertLocal);
            }
        });

        ImageView box1 = (ImageView) findViewById(R.id.lista_locais_box1);
        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFilterImage(v);
            }
        });

        ImageView box2 = (ImageView) findViewById(R.id.lista_locais_box2);
        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFilterImage(v);
            }
        });

        ImageView box3 = (ImageView) findViewById(R.id.lista_locais_box3);
        box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFilterImage(v);
            }
        });

        ImageView box4 = (ImageView) findViewById(R.id.lista_locais_box4);
        box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFilterImage(v);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_locais, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mapa:

                Drawable idMapMenuIcon = null;

                if(isMapOnScreen) {
                    isMapOnScreen = false;
                    replaceFragment(new LocaisListaFragment());
                    idMapMenuIcon = getResources().getDrawable(R.drawable.ic_mapa);
                } else {
                    isMapOnScreen = true;
                    replaceFragment(new LocaisMapaFragment());
                    idMapMenuIcon = getResources().getDrawable(R.drawable.ic_lista);
                }

                item.setIcon(idMapMenuIcon);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        FrameLayout viewById = (FrameLayout) findViewById(R.id.frame_principal);

        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);

        tx.replace(R.id.frame_principal, fragment);
        tx.commit();

        viewById.startAnimation(fadeIn);
    }

    private void onClickFilterImage(View v) {
        Context contexto = v.getContext();
        Animation fadeIn = AnimationUtils.loadAnimation(contexto, R.anim.fade_in);
        Drawable imgDrawable = null;

        switch (v.getId()) {
            case R.id.lista_locais_box1:
                if (box1Enabled) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_queimada_disable);
                    box1Enabled = Boolean.FALSE;
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_queimada_enable);
                    box1Enabled = Boolean.TRUE;
                }

                break;
            case R.id.lista_locais_box2:
                if (box2Enabled) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_deslizamento_disable);
                    box2Enabled = Boolean.FALSE;
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_deslizamento_enable);
                    box2Enabled = Boolean.TRUE;
                }

                break;

            case R.id.lista_locais_box3:
                if (box3Enabled) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_lixo_disable);
                    box3Enabled = Boolean.FALSE;
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_lixo_enable);
                    box3Enabled = Boolean.TRUE;
                }

                break;

            case R.id.lista_locais_box4:
                if (box4Enabled) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_desmatamento_disable);
                    box4Enabled = Boolean.FALSE;
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_desmatamento_enable);
                    box4Enabled = Boolean.TRUE;
                }

                break;
        }
        v.setBackground(imgDrawable);
        v.startAnimation(fadeIn);
    }
}
