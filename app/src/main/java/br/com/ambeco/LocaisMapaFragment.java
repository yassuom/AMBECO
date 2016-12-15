package br.com.ambeco;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.dao.LocalDAO;

public class LocaisMapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private Map<Marker, LocalBean> allMarkersMap = new HashMap<Marker, LocalBean>();

    private String filter;

    GoogleMap myMap;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                getMapAsync(this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle params = getArguments();
        if(params != null) {
            filter = (String) params.getSerializable("filter");
        }

        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        atualizaMapa();
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
                                ActivityCompat.requestPermissions(LocaisMapaFragment.this.getActivity(),
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

    public void atualizaMapa() {
        LatLng posicao = getCoordenadas("Rua Lourenço Carleto 40, Osasco");

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (posicao != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicao, 17);
            myMap.moveCamera(update);
        }

        LocalDAO localDAO = new LocalDAO(getContext());
        for (final LocalBean localBean : localDAO.listaLocais(filter)) {

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

                Marker marker = myMap.addMarker(marcador);
                allMarkersMap.put(marker, localBean);

                myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
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

        new Localizador(getContext(), myMap);
    }

}
