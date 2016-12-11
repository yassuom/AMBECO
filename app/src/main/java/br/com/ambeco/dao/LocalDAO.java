package br.com.ambeco.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.ambeco.beans.LocalBean;

/**
 * Created by Ambeco on 27/10/16.
 */

public class LocalDAO extends SQLiteOpenHelper {

    public LocalDAO(Context context) {
        super(context, "Ambeco", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sqlTableUsuario = "CREATE TABLE tbUsuario (idUsuario INTEGER PRIMARY KEY, " +
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertLocal(LocalBean localBean) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getDadosLocal(localBean);

        db.insert("tbLocal", null, dados);
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

    public List<LocalBean> listaLocais() {
        List<LocalBean> locais = new ArrayList<LocalBean>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;
        String sql = "SELECT * FROM tbLocal";

        cursor = db.rawQuery(sql, null);

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
            local.setIdUsuario(cursor.getLong(cursor.getColumnIndex("idUsuario")));
            locais.add(local);
        }

        cursor.close();

        return locais;
    }

}
