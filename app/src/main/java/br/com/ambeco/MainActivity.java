package br.com.ambeco;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.TextView;

import br.com.ambeco.helpers.UserHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean box1Enabled = false;

    private boolean box2Enabled = false;

    private boolean box3Enabled = false;

    private boolean box4Enabled = false;

    private boolean isMapOnScreen = true;

    private UserHelper userHelper = new UserHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
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

        View header = navigationView.getHeaderView(0);

        TextView nav_header_name = (TextView) header.findViewById(R.id.nav_header_nome);
        nav_header_name.setText(userHelper.getUserNameFromCache(this));

        TextView nav_header_email = (TextView) header.findViewById(R.id.nav_header_email);
        nav_header_email.setText(userHelper.getUserEmailFromCache(this));
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

    // Metodo responsavel por alterar o fragment em apresentacao
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        FrameLayout viewById = (FrameLayout) findViewById(R.id.frame_principal);

        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);

        tx.replace(R.id.frame_principal, fragment);
        tx.commit();

        viewById.startAnimation(fadeIn);
    }

    // Clique do botao Novo Local
    public void btnNovoLocalClick(View view) {
        Intent intentInsertLocal = new Intent(MainActivity.this, CadastraLocalActivity.class);
        startActivity(intentInsertLocal);
    }

    // Clique filtro Queimada
    public void imgFiltroQueimadaClick(View view) {
        if (box1Enabled) {
            alteraImagem(view,R.drawable.ic_queimada_disable);
            box1Enabled = Boolean.FALSE;
        } else {
            alteraImagem(view,R.drawable.ic_queimada_enable);
            box1Enabled = Boolean.TRUE;
        }
    }

    // Clique filtro Deslizamento
    public void imgFiltroDeslizamentoClick(View view) {
        if (box2Enabled) {
            alteraImagem(view,R.drawable.ic_deslizamento_disable);
            box2Enabled = Boolean.FALSE;
        } else {
            alteraImagem(view,R.drawable.ic_deslizamento_enable);
            box2Enabled = Boolean.TRUE;
        }
    }

    // Clique filtro Lixo
    public void imgFiltroLixoClick(View view) {
        if (box3Enabled) {
            alteraImagem(view,R.drawable.ic_lixo_disable);
            box3Enabled = Boolean.FALSE;
        } else {
            alteraImagem(view,R.drawable.ic_lixo_enable);
            box3Enabled = Boolean.TRUE;
        }
    }

    // Clique filtro Desmatamento
    public void imgFiltroDesmatamentoClick(View view) {
        if (box4Enabled) {
            alteraImagem(view,R.drawable.ic_desmatamento_disable);
            box4Enabled = Boolean.FALSE;
        } else {
            alteraImagem(view,R.drawable.ic_desmatamento_enable);
            box4Enabled = Boolean.TRUE;
        }
    }

    // Altera as imagens de filtro
    private void alteraImagem(View view, int idImagem) {
        Context contexto = view.getContext();
        Animation fadeIn = AnimationUtils.loadAnimation(contexto, R.anim.fade_in);
        Drawable imgDrawable = contexto.getResources().getDrawable(idImagem);
        view.setBackground(imgDrawable);
        view.startAnimation(fadeIn);
    }
}
