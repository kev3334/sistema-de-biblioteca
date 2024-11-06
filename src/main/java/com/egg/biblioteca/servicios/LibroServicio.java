package com.egg.biblioteca.servicios;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;
import com.egg.biblioteca.repositorios.LibroRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class LibroServicio {
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String  idEditorial) throws MiExcepcion{
        validar(isbn,titulo,ejemplares,idAutor,idEditorial);

        Libro libro = new Libro();
        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepositorio.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibros(){
        List<Libro> libros = new ArrayList<>();
        libros = libroRepositorio.findAll();
        return libros;
    }

    @Transactional
    public void modificarLibro(Long isbn,String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiExcepcion{
        validar(isbn,titulo,ejemplares,idAutor,idEditorial);

        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);
        Optional<Libro> respuestaLibro = libroRepositorio.findById(isbn);

        Autor autor = new Autor();
        if(respuestaAutor.isPresent()){
            autor = respuestaAutor.get();
        }

        Editorial editorial = new Editorial();
        if(respuestaEditorial.isPresent()){
            editorial = respuestaEditorial.get();
        }

        if(respuestaLibro.isPresent()){
            Libro libro = respuestaLibro.get();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);

            libroRepositorio.save(libro);
        }

    }

    @Transactional(readOnly = true)
    public Libro getOne(Long isbn){
        return libroRepositorio.getReferenceById(isbn);
    }

    public void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String  idEditorial) throws MiExcepcion {
        if(isbn == null ){
            throw new MiExcepcion("El isbn no puede ser nulo");
        }
        if(titulo == null || titulo.isEmpty() ){
            throw new MiExcepcion("El titulo no puede ser nulo o estar vacío.");
        }
        if(ejemplares == null ){
            throw new MiExcepcion("La cantidad de ejemplares no puede ser nulo");
        }
        if(idAutor == null || idAutor.isEmpty()){
            throw new MiExcepcion("El autor no puede ser nulo o estar vacío.");
        }
        if(idEditorial == null || idEditorial.isEmpty()){
            throw new MiExcepcion("La editorial no puede ser nulo o estar vacío.");
        }


    }


}
