package br.com.ambeco;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
public class MeusLocaisFragment extends Fragment {

    private ListView listaLocal;

    private UserHelper userHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_locais, container, false);
        listaLocal = (ListView) view.findViewById(R.id.meus_locais_lista);

        listaLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                LocalBean localBean = (LocalBean) listaLocal.getItemAtPosition(position);

                Intent intentVaiParaFormulario = new Intent(getContext(), CadastraLocalActivity.class);
                intentVaiParaFormulario.putExtra("local", localBean);
                startActivity(intentVaiParaFormulario);
            }
        });

        userHelper = new UserHelper(view.getContext());

        registerForContextMenu(listaLocal);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregaLista();
    }

    private void carregaLista() {
        LocalDAO dao = new LocalDAO(getContext());
        List<LocalBean> locais = dao.listaMeusLocais(userHelper.getUserId());
        dao.close();

        LocalAdapter adapter = new LocalAdapter(getContext(), locais);
        listaLocal.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final LocalBean localBean = (LocalBean) listaLocal.getItemAtPosition(info.position);

        //item de menu deletar
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                LocalDAO dao = new LocalDAO(getContext());
                dao.deletaLocal(localBean);
                dao.close();

                carregaLista();
                return false;
            }
        });

    }

}
