package br.com.ambeco;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Line;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.dao.LocalDAO;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocaisFragment extends Fragment implements OnMapReadyCallback{

    private boolean box1Enabled = false;

    private boolean box2Enabled = false;

    private boolean box3Enabled = false;

    private boolean box4Enabled = false;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private Map<Marker, LocalBean> allMarkersMap = new HashMap<Marker, LocalBean>();

    LinearLayout layoutLista;

    private View rootView;
    GoogleMap myMap;
    MapView mMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_locais, container, false);


        layoutLista = (LinearLayout) rootView.findViewById(R.id.locais_lista);

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                MapsInitializer.initialize(this.getActivity());
                mMapView = (MapView) rootView.findViewById(R.id.map);
                mMapView.onCreate(savedInstanceState);
                mMapView.getMapAsync(this);

            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Funcionalidades do mapa
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng posicao = getCoordenadas("Rua Lourenço Carleto 40, Osasco");

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (posicao != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicao, 17);
            googleMap.moveCamera(update);
        }

        LocalDAO localDAO = new LocalDAO(getContext());
        for (final LocalBean localBean : localDAO.listaLocais()) {

            String enderecoCompleto = localBean.getLogradouro() + " " +
                    localBean.getAltura() + ", " +
                    localBean.getCidade();


            LatLng coordenada = getCoordenadas(enderecoCompleto);
            if (coordenada != null) {
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(coordenada);
                marcador.title(localBean.getDescricao());
                marcador.snippet("Nível Degradação: " + String.valueOf(localBean.getNivelDegradacao()));
                marcador.icon(BitmapDescriptorFactory.fromResource(getMarcador(localBean.getIdCategoria())));

                Marker marker = googleMap.addMarker(marcador);
                allMarkersMap.put(marker, localBean);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        LocalBean bean = allMarkersMap.get(marker);
                        Intent intentDetalheLocal = new Intent(getContext(), DetalheLocalActivity.class);
                        intentDetalheLocal.putExtra("local", bean);
                        startActivity(intentDetalheLocal);
                        return true;
                    }
                });
            }
        }
        localDAO.close();

        new Localizador(getContext(), googleMap);
    }

    private LatLng getCoordenadas(String endereco) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            if(!resultados.isEmpty()) {
                LatLng posicao = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getMarcador(long pIdCategoria) {
        int idCategoria = Integer.parseInt(String.valueOf(pIdCategoria));
        switch (idCategoria) {
            case 1: return R.drawable.ic_marker_queimada;
            case 2: return R.drawable.ic_marker_deslizamento;
            case 3: return R.drawable.ic_marker_lixo;
            case 4: return R.drawable.ic_marker_desmatamento;
            default: return 0;
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this.getContext())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LocaisFragment.this.getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this.getContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                } else {
                    Toast.makeText(this.getContext(), "Permissão Negada!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Controle de clique nos botões de filtro
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Metodos de obtenção dos componentes da tela
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LinearLayout getLayoutLista() {
        return layoutLista;
    }

}
