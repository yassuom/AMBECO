package br.com.ambeco.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ambeco.LocaisFragment;
import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.beans.UsuarioBean;
import br.com.ambeco.helpers.FilterHelper;

/**
 * Created by Ambeco on 27/10/16.
 */

public class LocalDAO extends SQLiteOpenHelper {

    Context contexto;

    public LocalDAO(Context context) {
        super(context, "Ambeco", null, 1);
        contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sqlTableUsuario = "CREATE TABLE tbUsuario (idUsuario INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "sobrenome TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "senha TEXT NOT NULL);";
        sqLiteDatabase.execSQL(sqlTableUsuario);

        String sqlTableLocais = "CREATE TABLE tbLocal (idLocal INTEGER PRIMARY KEY, " +
                "descricao TEXT NOT NULL, " +
                "logradouro TEXT NOT NULL, " +
                "bairro TEXT NOT NULL, " +
                "altura INTEGER NOT NULL, " +
                "cidade TEXT NOT NULL, " +
                "uf TEXT NOT NULL, " +
                "nivelDegradacao INTEGER NOT NULL, " +
                "texto TEXT NOT NULL," +
                "caminhoFoto TEXT NOT NULL, " +
                "idCategoria INTEGER NOT NULL, " +
                "idUsuario INTEGER NOT NULL, " +
                "FOREIGN KEY(idUsuario) REFERENCES tbUsuario(idUsuario)); ";
        sqLiteDatabase.execSQL(sqlTableLocais);

        String sqlTableLocaisFavoritos = "CREATE TABLE tbFavoritos (idLocal INTEGER NOT NULL, " +
                "idUsuario INTEGER NOT NULL, " +
                "FOREIGN KEY(idLocal) REFERENCES tbLocal(idLocal) " +
                "FOREIGN KEY(idUsuario) REFERENCES tbUsuario(idUsuario)); ";
        sqLiteDatabase.execSQL(sqlTableLocaisFavoritos);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertLocal(LocalBean localBean) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getDadosLocal(localBean);

        db.insert("tbLocal", null, dados);
    }

    public void deletaLocal(LocalBean localBean) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {String.valueOf(localBean.getIdLocal())};
        db.delete("tbFavoritos", "idLocal = ?", params);
        db.delete("tbLocal", "idLocal = ?", params);
    }

    private ContentValues getDadosLocal(LocalBean localBean) {
        ContentValues dados = new ContentValues();
        dados.put("descricao", localBean.getDescricao());
        dados.put("logradouro", localBean.getLogradouro());
        dados.put("bairro", localBean.getBairro());
        dados.put("altura", localBean.getAltura());
        dados.put("cidade", localBean.getCidade());
        dados.put("uf", localBean.getUf());
        dados.put("nivelDegradacao", localBean.getNivelDegradacao());
        dados.put("texto", localBean.getTexto());
        dados.put("caminhoFoto", localBean.getCaminhoFoto());
        dados.put("idCategoria", localBean.getIdCategoria());
        dados.put("idUsuario", localBean.getIdUsuario());
        return dados;
    }

    public List<LocalBean> listaLocais(String filter) {
        List<LocalBean> locais = new ArrayList<LocalBean>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;

        if(filter == "") {
            String sql = "SELECT * FROM tbLocal";
            cursor = db.rawQuery(sql, null);
        } else {
            String sql = "SELECT * FROM tbLocal WHERE idCategoria in(" + filter + ") ";
            cursor = db.rawQuery(sql, null);
        }

        while(cursor.moveToNext()) {
            LocalBean local = new LocalBean();
            local.setIdLocal(cursor.getLong(cursor.getColumnIndex("idLocal")));
            local.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            local.setLogradouro(cursor.getString(cursor.getColumnIndex("logradouro")));
            local.setBairro(cursor.getString(cursor.getColumnIndex("bairro")));
            local.setAltura(cursor.getLong(cursor.getColumnIndex("altura")));
            local.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));
            local.setUf(cursor.getString(cursor.getColumnIndex("uf")));
            local.setNivelDegradacao(cursor.getLong(cursor.getColumnIndex("nivelDegradacao")));
            local.setTexto(cursor.getString(cursor.getColumnIndex("texto")));
            local.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));
            local.setIdCategoria(cursor.getLong(cursor.getColumnIndex("idCategoria")));
            local.setIdUsuario(cursor.getInt(cursor.getColumnIndex("idUsuario")));
            locais.add(local);
        }

        cursor.close();

        return locais;
    }

    public List<LocalBean> listaMeusLocais(int pIdUsuario) {
        List<LocalBean> locais = new ArrayList<LocalBean>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;

        cursor = db.rawQuery( "SELECT * FROM tbLocal WHERE idUsuario = ?", new String[]{String.valueOf(pIdUsuario)});

        while(cursor.moveToNext()) {
            LocalBean local = new LocalBean();
            local.setIdLocal(cursor.getLong(cursor.getColumnIndex("idLocal")));
            local.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            local.setLogradouro(cursor.getString(cursor.getColumnIndex("logradouro")));
            local.setBairro(cursor.getString(cursor.getColumnIndex("bairro")));
            local.setAltura(cursor.getLong(cursor.getColumnIndex("altura")));
            local.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));
            local.setUf(cursor.getString(cursor.getColumnIndex("uf")));
            local.setNivelDegradacao(cursor.getLong(cursor.getColumnIndex("nivelDegradacao")));
            local.setTexto(cursor.getString(cursor.getColumnIndex("texto")));
            local.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));
            local.setIdCategoria(cursor.getLong(cursor.getColumnIndex("idCategoria")));
            local.setIdUsuario(cursor.getInt(cursor.getColumnIndex("idUsuario")));
            locais.add(local);
        }

        cursor.close();

        return locais;
    }

    public void insertUsuario(UsuarioBean usuarioBean) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getDadosUsuario(usuarioBean);

        db.insert("tbUsuario", null, dados);
    }

    private ContentValues getDadosUsuario(UsuarioBean usuarioBean) {
        ContentValues dados = new ContentValues();
        dados.put("nome", usuarioBean.getNome());
        dados.put("sobrenome", usuarioBean.getSobrenome());
        dados.put("email", usuarioBean.getEmail().toLowerCase());
        dados.put("senha", usuarioBean.getSenha().toLowerCase());
        return dados;
    }

    public UsuarioBean getUsuario(String strEmail, String strSenha) {
        UsuarioBean retBean = new UsuarioBean();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;
        String sql = "SELECT * FROM tbUsuario WHERE email = ? and senha = ?";

        cursor = db.rawQuery(sql, new String[] {strEmail.toLowerCase(), strSenha.toLowerCase()} );

        if(cursor.getCount() > 0) {

            while(cursor.moveToNext()) {
                retBean.setIdUsuario(cursor.getInt(cursor.getColumnIndex("idUsuario")));
                retBean.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                retBean.setSobrenome(cursor.getString(cursor.getColumnIndex("sobrenome")));
                retBean.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                retBean.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
            }

        } else {
            return null;
        }

        cursor.close();
        return retBean;
    }

    public boolean usuarioExistente(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tbUsuario WHERE email = ?;", new String[]{email.toLowerCase()});
        int resultado = cursor.getCount();
        cursor.close();
        return (resultado > 0);
    }

    public List<LocalBean> listaMeusLocaisFavoritos(int pIdUsuario) {
        List<LocalBean> locais = new ArrayList<LocalBean>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;

        cursor = db.rawQuery( "SELECT * FROM tbLocal INNER JOIN tbFavoritos ON tbLocal.idLocal = tbFavoritos.idLocal WHERE tbFavoritos.idUsuario = ?", new String[]{String.valueOf(pIdUsuario)});

        while(cursor.moveToNext()) {
            LocalBean local = new LocalBean();
            local.setIdLocal(cursor.getLong(cursor.getColumnIndex("idLocal")));
            local.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            local.setLogradouro(cursor.getString(cursor.getColumnIndex("logradouro")));
            local.setBairro(cursor.getString(cursor.getColumnIndex("bairro")));
            local.setAltura(cursor.getLong(cursor.getColumnIndex("altura")));
            local.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));
            local.setUf(cursor.getString(cursor.getColumnIndex("uf")));
            local.setNivelDegradacao(cursor.getLong(cursor.getColumnIndex("nivelDegradacao")));
            local.setTexto(cursor.getString(cursor.getColumnIndex("texto")));
            local.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));
            local.setIdCategoria(cursor.getLong(cursor.getColumnIndex("idCategoria")));
            local.setIdUsuario(cursor.getInt(cursor.getColumnIndex("idUsuario")));
            locais.add(local);
        }

        cursor.close();

        return locais;
    }

    public void insertFavorito(int pIdLocal, int pIdUsuario) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getDadosFavoritos(pIdLocal, pIdUsuario);

        db.insert("tbFavoritos", null, dados);
    }

    public void excluiFavorito(int pIdLocal, int pIdUsuario) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("tbFavoritos", "idLocal = ? and idUsuario = ?", new String[]{String.valueOf(pIdLocal),String.valueOf(pIdUsuario)});
    }

    private ContentValues getDadosFavoritos(int pIdLocal, int pIdUsuario) {
        ContentValues dados = new ContentValues();
        dados.put("idLocal", pIdLocal);
        dados.put("idUsuario", pIdUsuario);
        return dados;
    }



}
