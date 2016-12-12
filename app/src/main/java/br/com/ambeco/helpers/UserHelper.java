package br.com.ambeco.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.ambeco.beans.UsuarioBean;

/**
 * Created by Ambeco on 11/12/16.
 */

public class UserHelper {

    public static final String PREFS_NAME = "MyPrefsFile";

    private SharedPreferences settings = null;

    public UserHelper(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public UsuarioBean getUserCache() {
        UsuarioBean userBean = new UsuarioBean();

        userBean.setIdUsuario(settings.getInt("idUsuario", 0));
        userBean.setEmail(settings.getString("email", null));
        userBean.setSenha(settings.getString("password", null));
        userBean.setNome(settings.getString("nome", null));
        userBean.setSobrenome(settings.getString("sobrenome", null));

        return userBean;
    }

    public void setUserCache(UsuarioBean userBean) {

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("usuarioLogado", Boolean.TRUE);
        editor.putInt("idUsuario", userBean.getIdUsuario());
        editor.putString("email", userBean.getEmail());
        editor.putString("password", userBean.getSenha());
        editor.putString("nome", userBean.getNome());
        editor.putString("sobrenome", userBean.getSobrenome());
        editor.commit();

    }

    public String getUserNameFromCache() {
        String nomeRet = (settings.getString("nome", null) + " " + settings.getString("sobrenome", null));
        return nomeRet;
    }

    public String getUserEmailFromCache() {
        String emailRet = (settings.getString("email", null));
        return emailRet;
    }

    public int getUserId() {
        int idRet = (settings.getInt("idUsuario", 0));
        return idRet;
    }

}
