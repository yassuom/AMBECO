package br.com.ambeco;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.ambeco.adapter.LocalAdapter;
import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.dao.LocalDAO;
import br.com.ambeco.helpers.UserHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeusFavoritosFragment extends Fragment {


    private ListView listaLocal;

    private UserHelper userHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_favoritos, container, false);
        listaLocal = (ListView) view.findViewById(R.id.meus_favoritos_lista);

        listaLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocalBean local = (LocalBean) listaLocal.getItemAtPosition(position);

                Intent intentDetalheLocal = new Intent(getContext(), DetalheLocalActivity.class);
                intentDetalheLocal.putExtra("local", local);
                startActivity(intentDetalheLocal);
            }
        });

        registerForContextMenu(listaLocal);

        userHelper = new UserHelper(view.getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregaLista();
    }

    private void carregaLista() {
        LocalDAO dao = new LocalDAO(getContext());
        List<LocalBean> locais = dao.listaMeusLocaisFavoritos(userHelper.getUserId());
        dao.close();

        LocalAdapter adapter = new LocalAdapter(getContext(), locais);
        listaLocal.setAdapter(adapter);
    }
}
