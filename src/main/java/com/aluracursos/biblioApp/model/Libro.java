package com.aluracursos.biblioApp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Autor autor;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_idiomas",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "idiomas_id")
    )
    private List<Idiomas> idiomas;
    private Integer descargas;

    public List<Idiomas> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<Idiomas> idiomas) {
        this.idiomas = idiomas;
    }

    public Libro() {
    }

    public Libro(String titulo, Autor autor, List<Idiomas> idiomas, Integer descargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idiomas = idiomas;
        this.descargas = descargas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LIBRO \n" +
                "Titulo: " + titulo + "\n" +
                "Autor: " + autor.getNombre() + "\n" +
                "Idioma: " + idiomas.stream().map(Idiomas::getSiglas).toList() + "\n" +
                "Numero de descargas: " + descargas;
    }
}