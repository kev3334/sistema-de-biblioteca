package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@RequestMapping("/libro")
public class LibroControlador {
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;
    @Autowired
    private LibroServicio libroServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar(ModelMap model){
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("autores",autores);
        model.addAttribute("editoriales", editoriales);
        return "libro_form.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
                           @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
                           @RequestParam String idEditorial, ModelMap modelo){

        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "El libro fue cargado correctamente");
        } catch (MiExcepcion e) {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            modelo.addAttribute("autores",autores);
            modelo.addAttribute("editoriales", editoriales);

            modelo.put("error",e.getMessage());
            return "libro_form.html";
        }

        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap model){
        List<Libro> libros = libroServicio.listarLibros();
        model.addAttribute("libros", libros);
        return "libro_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn , ModelMap model){
        model.addAttribute("libro", libroServicio.getOne(isbn) );

        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("autores",autores);
        model.addAttribute("editoriales", editoriales);

        return "libro_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial, ModelMap model){
        try {
            libroServicio.modificarLibro(isbn,titulo,ejemplares,idAutor,idEditorial);
            return "redirect:../lista";
        } catch (MiExcepcion e) {
            model.put("error", e.getMessage());
            return "libro_modificar.html";
        }
    }
}
