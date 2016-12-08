package br.com.ambeco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.helpers.LocaisDetalheHelper;

public class DetalheLocalActivity extends AppCompatActivity {

    private LocaisDetalheHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_local);

        helper = new LocaisDetalheHelper(this);

        Intent intent = getIntent();
        LocalBean localBean = (LocalBean) intent.getSerializableExtra("local");

        if(localBean != null) {
            helper.preencheFormulario(localBean);
        }
    }
}
