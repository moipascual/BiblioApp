package com.aluracursos.biblioApp.repository;

import com.aluracursos.biblioApp.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.deathYear > :year AND a.birthYear <= :year")
    List<Autor> findAutoresVivos(String year);

    Optional<Autor> findByNombre(String nombre);
}
