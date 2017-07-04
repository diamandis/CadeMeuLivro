package br.com.senac.cademeulivro.model;

import java.io.Serializable;

/**
 * Created by joaos on 03/07/2017.
 */

public class ObraPreview implements Serializable{

    private int idObra;
    private String titulo;
    private String autor;
    private String editora;

    public ObraPreview() {
    }

    public int getIdObra() {
        return idObra;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }
}
