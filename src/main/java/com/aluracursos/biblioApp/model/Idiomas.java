package com.aluracursos.biblioApp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "idiomas")
public class Idiomas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String siglas;

    @ManyToMany(mappedBy = "idiomas")
    private List<Libro> libros;

    public Idiomas() {
    }

    public Idiomas(String idioma) {
        this.siglas = idioma;
    }

    public String getSiglas() {
        return siglas;
    }
}
