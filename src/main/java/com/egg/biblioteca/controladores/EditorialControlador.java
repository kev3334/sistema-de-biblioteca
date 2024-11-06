package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.servicios.EditorialServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar(){
        return "editorial_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
        try {

            editorialServicio.crearEditorial(nombre);
            modelo.put("exito", "La Editorial fue cargado correctamente");

        } catch (MiExcepcion e) {
            //Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, e);
            modelo.put("error",e.getMessage());
            return "editorial_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap model){
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("editoriales", editoriales);
        return "editorial_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap model){
        model.put("editorial", editorialServicio.getOne(id));
        return "editorial_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, ModelMap model){
        try {
            editorialServicio.modificarEditorial(nombre, id);
            return "redirect:../lista";
        } catch (MiExcepcion e) {
            model.put("error", e.getMessage());
            return "editorial_modificar.html";
        }
    }
}
