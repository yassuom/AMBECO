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
                LocalBean local = (LocalBean) listaLocal.getItemAtPosition(position);

                Intent intentDetalheLocal = new Intent(getContext(), DetalheLocalActivity.class);
                intentDetalheLocal.putExtra("local", local);
                startActivity(intentDetalheLocal);
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
        MenuItem deletar = menu.add("Excluir");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                LocalDAO dao = new LocalDAO(getContext());
                                dao.deletaLocal(localBean);
                                dao.close();

                                carregaLista();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Deseja excluir o local selecionado?").setPositiveButton("Sim", dialogClickListener)
                        .setNegativeButton("Não", dialogClickListener).show();

                return true;
            }
        });
    }

}
