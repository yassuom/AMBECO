package br.com.ambeco.helpers;

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
import br.com.ambeco.R;
import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.helpers.UserHelper;

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

    private final RadioButton campoQueimada;
    private final RadioButton campoDeslizamento;
    private final RadioButton campoLixo;
    private final RadioButton campoDesmatamento;


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
        campoQueimada = (RadioButton) activity.findViewById(R.id.cadastra_local_queimada);
        campoDeslizamento = (RadioButton) activity.findViewById(R.id.cadastra_local_deslizamento);
        campoLixo = (RadioButton) activity.findViewById(R.id.cadastra_local_lixo);
        campoDesmatamento = (RadioButton) activity.findViewById(R.id.cadastra_local_desmatamento);
        localBean = new LocalBean();
    }

    public LocalBean getLocal(int pIdUsuario) {
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
        localBean.setIdUsuario(pIdUsuario);
        return localBean;
    }

    public void preencheFormulario(LocalBean localBean) {
        campoDescricao.setText(localBean.getDescricao());
        campoLogradouro.setText(localBean.getLogradouro());
        campoBairro.setText(localBean.getBairro());
        campoAltura.setText(String.valueOf(localBean.getAltura()));
        campoCidade.setText(localBean.getCidade());
        campoUf.setTag(localBean.getUf());
        campoNivelDegradacao.setProgress((int)localBean.getNivelDegradacao());
        campoTexto.setText(localBean.getTexto());
        carregaImageFromPath(localBean.getCaminhoFoto());

        switch ((int)localBean.getIdCategoria()) {
            case 1: campoQueimada.setChecked(Boolean.TRUE);
            case 2: campoDeslizamento.setChecked(Boolean.TRUE);
            case 3: campoLixo.setChecked(Boolean.TRUE);
            case 4: campoDesmatamento.setChecked(Boolean.TRUE);
        }
    }

    public void carregaImageFromPath(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);
        }
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
