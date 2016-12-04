package br.com.ambeco;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.dao.LocalDAO;

public class LocaisMapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng posicao = getMyLocation();

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(Boolean.TRUE);

        if (posicao != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicao, 17);
            googleMap.moveCamera(update);
        }

        LocalDAO localDAO = new LocalDAO(getContext());
        for (LocalBean localBean : localDAO.buscaLocal(null)) {

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
                googleMap.addMarker(marcador);
            }
        }
        localDAO.close();

        new Localizador(getContext(), googleMap);

    }

    private LatLng getMyLocation() {
        LocationManager locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        LatLng posicao = new LatLng(location.getLatitude(), location.getLongitude());
        return posicao;
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

}
