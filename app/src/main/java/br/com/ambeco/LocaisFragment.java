package br.com.ambeco;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.ambeco.helpers.FilterHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocaisFragment extends Fragment {

    FrameLayout frameLayout;

    private View rootView;

    private FilterHelper filterHelper = new FilterHelper();

    private FragmentActivity myContext;

    int selectedLayout;

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_locais, container, false);

        frameLayout = (FrameLayout) rootView.findViewById(R.id.locais_frame);

        changeFragment(new LocaisMapaFragment());

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Controle de clique nos botões de filtro
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ImageView filterQueimada = (ImageView) rootView.findViewById(R.id.lista_locais_filtro_queimada);
        filterQueimada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterHelper.isFilterQueimada()) {
                    alteraImagem(v,R.drawable.ic_queimada_disable);
                    filterHelper.setFilterQueimada(Boolean.FALSE);
                } else {
                    alteraImagem(v,R.drawable.ic_queimada_enable);
                    filterHelper.setFilterQueimada(Boolean.TRUE);
                }

                changeLayout(selectedLayout);
            }
        });

        ImageView filterDeslizamento = (ImageView) rootView.findViewById(R.id.lista_locais_filtro_deslizamento);
        filterDeslizamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterHelper.isFilterDeslizamento()) {
                    alteraImagem(v,R.drawable.ic_deslizamento_disable);
                    filterHelper.setFilterDeslizamento(Boolean.FALSE);
                } else {
                    alteraImagem(v,R.drawable.ic_deslizamento_enable);
                    filterHelper.setFilterDeslizamento(Boolean.TRUE);
                }

                changeLayout(selectedLayout);
            }
        });

        ImageView filterLixo = (ImageView) rootView.findViewById(R.id.lista_locais_filtro_lixo);
        filterLixo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterHelper.isFilterLixo()) {
                    alteraImagem(v,R.drawable.ic_lixo_disable);
                    filterHelper.setFilterLixo(Boolean.FALSE);
                } else {
                    alteraImagem(v,R.drawable.ic_lixo_enable);
                    filterHelper.setFilterLixo(Boolean.TRUE);
                }

                changeLayout(selectedLayout);
            }
        });

        ImageView filterDesmatamento = (ImageView) rootView.findViewById(R.id.lista_locais_filtro_desmatamento);
        filterDesmatamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterHelper.isFilterDesmatamento()) {
                    alteraImagem(v,R.drawable.ic_desmatamento_disable);
                    filterHelper.setFilterDesmatamento(Boolean.FALSE);
                } else {
                    alteraImagem(v,R.drawable.ic_desmatamento_enable);
                    filterHelper.setFilterDesmatamento(Boolean.TRUE);
                }

                changeLayout(selectedLayout);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
// Metodos de visualização dos componentes
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void changeLayout(int layout) {
        selectedLayout = layout;

        if(layout == R.string.layout_lista) {
            changeFragment(new LocaisListaFragment());
        } else {
            changeFragment(new LocaisMapaFragment());
        }

    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        FrameLayout viewById = (FrameLayout) rootView.findViewById(R.id.locais_frame);
        Bundle params = new Bundle();
        params.putSerializable("filter",getFilter());
        fragment.setArguments(params);

        Animation fadeIn = AnimationUtils.loadAnimation(myContext, R.anim.fade_in);

        tx.replace(R.id.locais_frame, fragment);
        tx.commit();

        viewById.startAnimation(fadeIn);
    }

    private String getFilter() {
        String filter = "";

        if(filterHelper.isFilterQueimada()) {
            filter = "1";
        }
        if(filterHelper.isFilterDeslizamento()) {
            if(filter == "") {
                filter = "2";
            } else {
                filter = filter + ",2";
            }
        }
        if(filterHelper.isFilterLixo()) {
            if(filter == "") {
                filter = "3";
            } else {
                filter = filter + ",3";
            }
        }
        if(filterHelper.isFilterDesmatamento()) {
            if(filter == "") {
                filter = "4";
            } else {
                filter = filter + ",4";
            }
        }

        return filter;
    }

}