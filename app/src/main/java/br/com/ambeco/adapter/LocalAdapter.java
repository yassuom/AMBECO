package br.com.ambeco.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.ambeco.R;
import br.com.ambeco.beans.LocalBean;

/**
 * Created by Ambeco on 18/09/2016.
 */
public class LocalAdapter extends BaseAdapter {

    private final List<LocalBean> listaLocais;
    private final Context context;

    public LocalAdapter(Context context, List<LocalBean> listaLocais) {
        this.context = context;
        this.listaLocais = listaLocais;
    }


    @Override
    public int getCount() {
        return listaLocais.size();
    }

    @Override
    public Object getItem(int position) {
        return listaLocais.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaLocais.get(position).getIdLocal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocalBean localBean = listaLocais.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(view == null) {
            view = inflater.inflate(R.layout.lista_layout, parent, false);
        }

        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        campoNome.setText(localBean.getDescricao());

        TextView campoLogradouro = (TextView) view.findViewById(R.id.item_logradouro);
        campoLogradouro.setText(localBean.getLogradouro());

        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
        String caminhoFoto = localBean.getCaminhoFoto();
        if(caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        ImageView campoCor = (ImageView) view.findViewById(R.id.item_cor);
        switch ((int) localBean.getIdCategoria()) {
            case 1: campoCor.setBackgroundColor(ContextCompat.getColor(context,R.color.corID_queimada)); break;
            case 2: campoCor.setBackgroundColor(ContextCompat.getColor(context,R.color.corID_deslizamento)); break;
            case 3: campoCor.setBackgroundColor(ContextCompat.getColor(context,R.color.corID_lixo)); break;
            case 4: campoCor.setBackgroundColor(ContextCompat.getColor(context,R.color.corID_desmatamento)); break;
        }

        return view;
    }
}
