package com.aluracursos.biblioApp.repository;

import com.aluracursos.biblioApp.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l JOIN l.idiomas i WHERE i.siglas ILIKE %:idiomas")
    List<Libro> findByIdiomas(String idiomas);

    @Query("SELECT l FROM Libro l ORDER BY l.descargas DESC LIMIT 5")
    List<Libro> getTop();
}