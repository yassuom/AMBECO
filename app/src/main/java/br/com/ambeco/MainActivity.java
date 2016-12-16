package br.com.ambeco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.ambeco.dao.LocalDAO;
import br.com.ambeco.helpers.UserHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isMapOnScreen = true;

    private UserHelper userHelper;

    private Menu menuMain;

    private LocaisFragment fragmentLocal = new LocaisFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userHelper = new UserHelper(this);

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

        replaceFragment(fragmentLocal);

        View header = navigationView.getHeaderView(0);

        TextView nav_header_name = (TextView) header.findViewById(R.id.nav_header_nome);
        nav_header_name.setText(userHelper.getUserNameFromCache());

        TextView nav_header_email = (TextView) header.findViewById(R.id.nav_header_email);
        nav_header_email.setText(userHelper.getUserEmailFromCache());
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
        menuMain = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_mapa:

                Drawable idMapMenuIcon = null;

                if(isMapOnScreen) {
                    isMapOnScreen = false;
                    fragmentLocal.changeLayout(R.string.layout_lista);
                    idMapMenuIcon = getResources().getDrawable(R.drawable.ic_mapa);
                } else {
                    isMapOnScreen = true;
                    fragmentLocal.changeLayout(R.string.layout_mapa);
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
        int id = item.getItemId();

        if (id == R.id.nav_lista) {
            replaceFragment(new LocaisFragment());
            setMenuItemVisibility(Boolean.TRUE);
        }
        if (id == R.id.nav_meus_locais) {
            replaceFragment(new MeusLocaisFragment());
            setMenuItemVisibility(Boolean.FALSE);
        } else if (id == R.id.nav_favoritos) {
            replaceFragment(new MeusFavoritosFragment());
            setMenuItemVisibility(Boolean.FALSE);
        } else if (id == R.id.nav_logoff) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            userHelper.clearUserCache();
                            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intentLogin);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Deseja fazer o logoff?").setPositiveButton("Sim", dialogClickListener)
                    .setNegativeButton("Não", dialogClickListener).show();
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

    private void setMenuItemVisibility(Boolean status) {
        menuMain.getItem(1).setVisible(status);
    }

}
