package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap model, MultipartFile archivo){
        try {
            usuarioServicio.registrar(archivo, nombre, email, password, password2);
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
    public String inicio(HttpSession session){

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if(logueado.getRol().toString().equals("ADMIN")){
            return "redirect:/admin/dashboard";
        }
        return "inicio.html";
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/perfil/{id}")
    public String actualizar(@PathVariable String id, ModelMap model){
        model.put("usuario", usuarioServicio.getOne(id));
        return "usuario_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre,
                             @RequestParam String email, @RequestParam String password, @RequestParam String password2,
                             ModelMap model, HttpSession session){
        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);
            model.put("exito", "Usuario actualizado correctamente");

            session.setAttribute("usuariosession", usuarioServicio.getOne(id));

            return "redirect:../inicio";
        } catch (MiExcepcion e) {

            model.put("error", e.getMessage());
            model.put("nombre", nombre);
            model.put("email",email);
            return "usuario_modificar.html";
        }

    }


}
