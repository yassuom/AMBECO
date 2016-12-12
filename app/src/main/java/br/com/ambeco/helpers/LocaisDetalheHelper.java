package br.com.ambeco.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.ambeco.DetalheLocalActivity;
import br.com.ambeco.R;
import br.com.ambeco.beans.LocalBean;

/**
 * Created by Ambeco on 07/12/16.
 */

public class LocaisDetalheHelper {

    private final TextView campoDescricao;
    private final TextView campoEndereco;
    private final TextView campoTexto;
    private final TextView campoCategoria;
    private final TextView campoNivel;
    private final ImageView campoFoto;


    private LocalBean localBean;


    public LocaisDetalheHelper(DetalheLocalActivity activity) {
        campoDescricao = (TextView) activity.findViewById(R.id.detalhe_local_descricao);
        campoEndereco = (TextView) activity.findViewById(R.id.detalhe_local_endereco);
        campoTexto = (TextView) activity.findViewById(R.id.detalhe_local_texto);
        campoCategoria = (TextView) activity.findViewById(R.id.detalhe_local_categoria);
        campoNivel = (TextView) activity.findViewById(R.id.detalhe_local_nivel);
        campoFoto = (ImageView) activity.findViewById(R.id.detalhe_local_foto);
    }

    public void preencheFormulario(LocalBean localBean) {
        this.localBean = localBean;
        campoDescricao.setText(localBean.getDescricao());
        campoEndereco.setText(montaEndereco());
        campoTexto.setText(localBean.getTexto());
        campoCategoria.setText(montaCategoria());
        campoNivel.setText(String.valueOf(localBean.getNivelDegradacao()));
        carregaImagem(localBean.getCaminhoFoto());
    }

    public String montaEndereco() {
        String retorno = null;

        retorno = localBean.getLogradouro() + ", " + localBean.getCidade().toUpperCase() + " " + localBean.getUf();

        return retorno;
    }

    public String montaCategoria() {
        String retorno = null;

        switch ((int) localBean.getIdCategoria()) {
            case 1: retorno = "Queimada";
                    break;
            case 2: retorno = "Deslizamento";
                    break;
            case 3: retorno = "Lixo";
                    break;
            case 4: retorno = "Desmatamento";
                    break;
        }

        return retorno;
    }

    public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);
        }
    }

}
