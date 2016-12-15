package br.com.ambeco;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.ambeco.adapter.LocalAdapter;
import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.dao.LocalDAO;
import br.com.ambeco.helpers.UserHelper;

public class LocaisListaFragment extends Fragment {

    private ListView listaLocal;

    private String filter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locais_lista, container, false);
        listaLocal = (ListView) view.findViewById(R.id.lista_locais);

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle params = getArguments();
        if(params != null) {
            filter = (String) params.getSerializable("filter");
        }

        carregaLista();
    }

    private void carregaLista() {
        LocalDAO dao = new LocalDAO(getContext());
        List<LocalBean> locais = dao.listaLocais(filter);
        dao.close();

        LocalAdapter adapter = new LocalAdapter(getContext(), locais);
        listaLocal.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final LocalBean localBean = (LocalBean) listaLocal.getItemAtPosition(info.position);

        MenuItem favoritos = menu.add("Adicionar aos favoritos");
        favoritos.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                LocalDAO localDAO = new LocalDAO(getContext());
                UserHelper userHelper = new UserHelper(getContext());

                localDAO.insertFavorito((int)localBean.getIdLocal(), userHelper.getUserId());

                Toast.makeText(getContext(), "Local Adicionado aos Favoritos!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }
}
