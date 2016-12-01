package br.com.ambeco.listeners;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import br.com.ambeco.R;

/**
 * Created by Ambeco on 26/10/16.
 */

public class onFilterImageClickListener implements View.OnClickListener {

    private boolean box1Enabled = false;

    private boolean box2Enabled = false;

    private boolean box3Enabled = false;

    private boolean box4Enabled = false;

    @Override
    public void onClick(View v) {

        Context contexto = v.getContext();
        Animation fadeIn = AnimationUtils.loadAnimation(contexto, R.anim.fade_in);
        Drawable imgDrawable = null;

        switch (v.getId()) {
            case R.id.lista_locais_box1:
                if(isBox1Enabled()) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_queimada_disable);
                    setBox1Enabled(Boolean.FALSE);
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_queimada_enable);
                    setBox1Enabled(Boolean.TRUE);
                }

                break;
            case R.id.lista_locais_box2:
                if(isBox2Enabled()) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_deslizamento_disable);
                    setBox2Enabled(Boolean.FALSE);
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_deslizamento_enable);
                    setBox2Enabled(Boolean.TRUE);
                }

                break;

            case R.id.lista_locais_box3:
                if(isBox3Enabled()) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_lixo_disable);
                    setBox3Enabled(Boolean.FALSE);
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_lixo_enable);
                    setBox3Enabled(Boolean.TRUE);
                }

                break;

            case R.id.lista_locais_box4:
                if(isBox4Enabled()) {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_desmatamento_disable);
                    setBox4Enabled(Boolean.FALSE);
                } else {
                    imgDrawable = contexto.getResources().getDrawable(R.drawable.ic_desmatamento_enable);
                    setBox4Enabled(Boolean.TRUE);
                }

                break;
        }
        v.setBackground(imgDrawable);
        v.startAnimation(fadeIn);
    }


    public boolean isBox1Enabled() {
        return box1Enabled;
    }

    public void setBox1Enabled(boolean box1Enabled) {
        this.box1Enabled = box1Enabled;
    }

    public boolean isBox2Enabled() {
        return box2Enabled;
    }

    public void setBox2Enabled(boolean box2Enabled) {
        this.box2Enabled = box2Enabled;
    }

    public boolean isBox3Enabled() {
        return box3Enabled;
    }

    public void setBox3Enabled(boolean box3Enabled) {
        this.box3Enabled = box3Enabled;
    }

    public boolean isBox4Enabled() {
        return box4Enabled;
    }

    public void setBox4Enabled(boolean box4Enabled) {
        this.box4Enabled = box4Enabled;
    }
}
