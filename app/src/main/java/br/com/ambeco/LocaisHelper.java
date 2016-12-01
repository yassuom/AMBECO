package br.com.ambeco;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.ambeco.CadastraLocalActivity;
import br.com.ambeco.beans.LocalBean;

/**
 * Created by Ambeco on 06/11/16.
 */

public class LocaisHelper {

    private final CadastraLocalActivity cadastraLocalActivity;
    private final EditText campoDescricao;
    private final EditText campoLogradouro;
    private final EditText campoBairro;
    private final EditText campoAltura;
    private final EditText campoCidade;
    private final Spinner  campoUf;
    private final SeekBar campoNivelDegradacao;
    private final EditText campoTexto;
    private final ImageView campoFoto;

    private LocalBean localBean;


    public LocaisHelper(CadastraLocalActivity activity) {
        cadastraLocalActivity = activity;
        campoDescricao = (EditText) activity.findViewById(R.id.cadastra_local_descricao);
        campoLogradouro = (EditText) activity.findViewById(R.id.cadastra_local_logradouro);
        campoBairro = (EditText) activity.findViewById(R.id.cadastra_local_bairro);
        campoAltura = (EditText) activity.findViewById(R.id.cadastra_local_altura);
        campoCidade = (EditText) activity.findViewById(R.id.cadastra_local_cidade);
        campoUf = (Spinner) activity.findViewById(R.id.cadastra_local_uf);
        campoNivelDegradacao = (SeekBar) activity.findViewById(R.id.cadastra_local_nivel);
        campoTexto = (EditText) activity.findViewById(R.id.cadastra_local_texto);
        campoFoto = (ImageView) activity.findViewById(R.id.cadastra_local_foto);
        localBean = new LocalBean();
    }

    public LocalBean getLocal() {
        localBean.setDescricao(campoDescricao.getText().toString());
        localBean.setLogradouro(campoLogradouro.getText().toString());
        localBean.setBairro(campoBairro.getText().toString());
        localBean.setAltura(Long.valueOf(campoAltura.getText().toString()));
        localBean.setCidade(campoCidade.getText().toString());
        localBean.setUf(campoUf.getSelectedItem().toString());
        localBean.setNivelDegradacao(campoNivelDegradacao.getProgress());
        localBean.setTexto(campoTexto.getText().toString());
        localBean.setCaminhoFoto((String) campoFoto.getTag());
        localBean.setIdCategoria(cadastraLocalActivity.getIdCategoriaSelecionada());
        return localBean;
    }



    public void carregaImagem(Bitmap bitMapFoto, String caminhoFoto) {
        if (bitMapFoto != null) {
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitMapFoto, 300, 300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);
        }
    }

}
