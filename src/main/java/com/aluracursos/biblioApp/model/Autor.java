package com.aluracursos.biblioApp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, unique = true)
private String nombre;

@Column(name = "birth_year")
private String birthYear;

@Column(name = "death_year")
private String deathYear;

@OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
private List<Libro> libro;

public Autor() {
}

public List<Libro> getLibro() {
    return libro;
}

public void setLibro(List<Libro> libro) {
    libro.forEach(l -> l.setAutor(this));
    this.libro = libro;
}

public String getDeathYear() {
    return deathYear;
}

public void setDeathYear(String deathYear) {
    this.deathYear = deathYear;
}

public String getBirthYear() {
    return birthYear;
}

public void setBirthYear(String birthYear) {
    this.birthYear = birthYear;
}

public String getNombre() {
    return nombre;
}

public void setNombre(String nombre) {
    this.nombre = nombre;
}

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

@Override
public String toString() {
    return  "Autor: " + nombre + "\n" +
            "Año de nacimiento: " + birthYear + "\n" +
            "Año de fallecimiento: " + deathYear + "\n" +
            "Titulo: " + libro.stream().map(Libro::getTitulo).toList() + "\n";
}
}
