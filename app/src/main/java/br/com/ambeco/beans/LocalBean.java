package br.com.ambeco.beans;

import java.io.Serializable;

/**
 * Created by Ambeco on 18/09/2016.
 */
public class LocalBean implements Serializable {

    private long idLocal;

    private String descricao;

    private String logradouro;

    private String bairro;

    private long altura;

    private String cidade;

    private String uf;

    private long nivelDegradacao;

    private String texto;

    private String caminhoFoto;

    private long idCategoria;

    private int idUsuario;

    public long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(long idLocal) {
        this.idLocal = idLocal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public long getAltura() {
        return altura;
    }

    public void setAltura(long altura) {
        this.altura = altura;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public long getNivelDegradacao() {
        return nivelDegradacao;
    }

    public void setNivelDegradacao(long nivelDegradacao) {
        this.nivelDegradacao = nivelDegradacao;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdUsuario() { return idUsuario; }

    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

}
