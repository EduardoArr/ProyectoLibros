package com.example.listviewpersonas.Controlador;

import java.io.Serializable;

public class Libros implements Serializable {
    private String foto;
    private String id;
    private String titulo;
    private String autor;
    private String resumen;
    private String comentario;


    public Libros(String id,  String titulo, String autor, String resumen, String comentario) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.resumen = resumen;
        this.comentario = comentario;
    }

    public Libros(String titulo, String autor, String resumen, String comentario) {
        this.titulo = titulo;
        this.autor = autor;
        this.resumen = resumen;
        this.comentario = comentario;
    }

    public Libros(){

    }




    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getResumen() {
        return resumen;
    }

    public String getComentario() {
        return comentario;
    }

    public String getFoto() {
        return foto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
