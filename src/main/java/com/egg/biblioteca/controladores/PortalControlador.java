package com.egg.biblioteca.controladores;

import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(){

        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar(){
        return "registro.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model){
        if(error != null){
            model.put("error", "Usuario o Contraseña inválidos!");
        }
        return "login.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap model){
        try {
            usuarioServicio.registrar(nombre, email, password, password2);
            model.put("exito", "usuario registrado exitosamente");
            return "index.html";
        } catch (MiExcepcion e) {
            model.put("error", e.getMessage());
            model.put("nombre",nombre);
            model.put("email", email);
            return "registro.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(){
        return "inicio.html";
    }

}
