package edu.pucp.gtics.lab5_gtics_20211.controller;

import edu.pucp.gtics.lab5_gtics_20211.entity.Juegos;
import edu.pucp.gtics.lab5_gtics_20211.entity.Plataformas;
import edu.pucp.gtics.lab5_gtics_20211.entity.User;
import edu.pucp.gtics.lab5_gtics_20211.repository.JuegosRepository;
import edu.pucp.gtics.lab5_gtics_20211.repository.PlataformasRepository;
import edu.pucp.gtics.lab5_gtics_20211.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/juegos")
public class JuegosController {

    @Autowired
    PlataformasRepository plataformasRepository;

    @Autowired
    JuegosRepository juegosRepository;

    @GetMapping("/lista")
    public String listaJuegos (Model model, HttpSession session){

        User sessionUser = (User) session.getAttribute("usuario");

        if(sessionUser.getAutorizacion().equals("ADMIN")) {
            model.addAttribute("listaJuegos", juegosRepository.findAll());
            return "juegos/lista";
        }else{
            model.addAttribute("listaJuegos", juegosRepository.obtenerJuegosPorUser(sessionUser.getIdusuario()));
            return "juegos/comprado";
        }

    }

    @GetMapping(value = {"", "/", "/vista"})
    public String vistaJuegos (Model model ){
        List<Juegos> listaJuegos = juegosRepository.listaJuegosDesc();
        model.addAttribute("listaJuegos", listaJuegos);
        return "juegos/vista";
    }

    @GetMapping("/nuevo")
    public String nuevoJuegos(Model model, @ModelAttribute("juego") Juegos juego){
        model.addAttribute("listaPlataformas", plataformasRepository.findAll());
        return "juegos/editarFrm";
    }


    @PostMapping("/guardar")
    public String guardarJuegos(Model model, RedirectAttributes attr, @ModelAttribute("juego") @Valid Juegos juego, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            model.addAttribute("listaPlataformas", plataformasRepository.findAll());
            return "juegos/editarFrm";
        }else {

            if (juego.getIdjuego() == 0) {
                attr.addFlashAttribute("msg", "Juego creado exitosamente");
            } else {
                attr.addFlashAttribute("msg2", "Juego actualizado exitosamente");
            }
            juegosRepository.save(juego);
            return "redirect:/juegos/lista";
        }

    }

    @GetMapping("/editar")
    public String editarJuegos(Model model, @RequestParam("id") int id, @ModelAttribute("juego") Juegos juego){
        Optional<Juegos> optJuegos = juegosRepository.findById(id);
        if(optJuegos.isPresent()){
            juego = optJuegos.get();
            model.addAttribute("juego", juego);
            model.addAttribute("listaPlataformas", plataformasRepository.findAll());
            return "juegos/editarFrm";
        } else {
            return "redirect:/juegos/lista";
        }

    }

    @GetMapping("/borrar")
    public String borrarDistribuidora(@RequestParam("id") int id, RedirectAttributes attr){
        Optional<Juegos> opt = juegosRepository.findById(id);
        if (opt.isPresent()) {
            juegosRepository.deleteById(id);
            attr.addFlashAttribute("msg3", "Juego borrado exitosamente");
        }
        return "redirect:/juegos/lista";
    }

}
