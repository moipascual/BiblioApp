package com.aluracursos.biblioApp.principal;

import com.aluracursos.biblioApp.model.*;
import com.aluracursos.biblioApp.repository.AutorRepository;
import com.aluracursos.biblioApp.repository.LibroRepository;
import com.aluracursos.biblioApp.service.ConsumoAPI;
import com.aluracursos.biblioApp.service.ConvierteDatos;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {
        var opcion = -1;

        while (opcion != 0) {
            var menu = """

                      ***** M.E.N.U ******
             1- Buscar libros por titulo
             2- Mostrar libros guardados
             3- Mostrar autores guardados
             4- Mostrar autores vivos según fecha
             5- Mostrar libros según idioma
             6- Mostrar Top 5 libros
             0- Quit
            ***************************
            -- SELECCIONE UNA OPCIÓN --:
            """;
            System.out.println(menu);

            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    mostrarAutoresVivos();
                    break;
                case 5:
                    mostrarLibrosSegunIdioma();
                    break;
                case 6:
                    mostrarTop();
                    break;
                default:
                    System.out.println("#### OPCIÓN NO VÁLIDA ####");
            }
        }
    }

    private void buscarLibroPorTitulo() throws DataIntegrityViolationException {
        System.out.println("Título de libro a buscar: ");
        var tituloBuscado = teclado.nextLine();

        var jsonResponse = consumoAPI.obtener(tituloBuscado);
        try {
            var json = conversor.aOjeto(jsonResponse);

            DatosLibro datos = conversor.obtenerDatos(String.valueOf(json.get("results").get(0)), DatosLibro.class);
            System.out.println("---- RESULTADOS -----");
            System.out.println("Titulo: " + datos.titulo());
            datos.autor().forEach(a -> System.out.println("Autor: " + a.nombre()));
            System.out.println("Idioma: " + datos.idiomas().get(0));
            System.out.println("Numero de Downloads: " + datos.descargas());
            System.out.println("***************************");

            guardar(datos);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void guardar(DatosLibro datos) {
        Optional<Autor> memoriaAutor = autorRepository.findByNombre(datos.autor().get(0).nombre());


        List<Libro> librosConjunto = new ArrayList<>();

        if (memoriaAutor.isPresent()) {
            Libro libro = new Libro();
            var autor = memoriaAutor.get();

            libro.setTitulo(datos.titulo());
            libro.setIdiomas(datos.idiomas().stream().map(Idiomas::new).collect(Collectors.toList()));
            libro.setDescargas(datos.descargas());

            librosConjunto.add(libro);
            autor.setLibro(librosConjunto);

            autorRepository.save(autor);
        } else {

            Autor autor = new Autor();
            for (DatosAutor datosAutor : datos.autor()) {
                autor.setNombre(datosAutor.nombre());
                autor.setBirthYear(datosAutor.birthYear());
                autor.setDeathYear(datosAutor.deathYear());
            }

            Libro libro = new Libro(datos.titulo(), autor, datos.idiomas().stream().map(Idiomas::new).collect(Collectors.toList()), datos.descargas());
            libroRepository.save(libro);
        }
    }

    private void mostrarLibros() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void mostrarAutores() {
        List<Autor> autores = autorRepository.findAll();

        System.out.println("---- AUTORES ----");
        autores.forEach(System.out::println);
    }

    private void mostrarAutoresVivos() {
        System.out.println("¿En qué año desea indagar? ");
        var year = teclado.nextLine();

        List<Autor> autoresVivos = autorRepository.findAutoresVivos(year);
        System.out.println("---- Autores vivos en " + year + " ----");
        autoresVivos.forEach(System.out::println);
    }

    private void mostrarLibrosSegunIdioma() {
        System.out.println("""
        ----- IDIOMAS ----
        ES - Español
        EN - Inglês
        FR - Francés
        PT - Portugués
        
        ¿Qúe idioma desa buscar?: 
        """);
        String idioma = teclado.nextLine().toLowerCase();

        List<Libro> libros = libroRepository.findByIdiomas(idioma);
        System.out.println("---- Libros ee " + idioma.toUpperCase() + " ----");

        libros.forEach(System.out::println);
    }

    private void mostrarTop() {
        List<Libro> libros = libroRepository.getTop();
        System.out.println("---- TOP 5 DESCARGAS ----");
        libros.forEach(System.out::println);
    }
}