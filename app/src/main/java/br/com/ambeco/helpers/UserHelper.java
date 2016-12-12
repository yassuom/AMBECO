package br.com.ambeco.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.ambeco.beans.UsuarioBean;

/**
 * Created by Ambeco on 11/12/16.
 */

public class UserHelper {

    public static final String PREFS_NAME = "MyPrefsFile";

    public UsuarioBean getUserCache(Context context) {
        UsuarioBean userBean = new UsuarioBean();

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        if(settings != null) {
            userBean.setIdUsuario(settings.getInt("idUsuario", 0));
            userBean.setEmail(settings.getString("email", null));
            userBean.setSenha(settings.getString("password", null));
            userBean.setNome(settings.getString("nome", null));
            userBean.setSobrenome(settings.getString("sobrenome", null));
        }

        return userBean;
    }

    public void setUserCache(Context context, UsuarioBean userBean) {

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("usuarioLogado", Boolean.TRUE);
        editor.putInt("idUsuario", userBean.getIdUsuario());
        editor.putString("email", userBean.getEmail());
        editor.putString("password", userBean.getSenha());
        editor.putString("nome", userBean.getNome());
        editor.putString("sobrenome", userBean.getSobrenome());
        editor.commit();

    }

    public String getUserNameFromCache(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String nomeRet = (settings.getString("nome", null) + " " + settings.getString("sobrenome", null));
        return nomeRet;
    }

    public String getUserEmailFromCache(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String emailRet = (settings.getString("email", null));
        return emailRet;
    }

}
