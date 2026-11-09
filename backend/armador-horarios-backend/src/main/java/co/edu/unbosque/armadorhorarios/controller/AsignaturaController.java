package co.edu.unbosque.armadorhorarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.armadorhorarios.dto.AsignaturaDTO;
import co.edu.unbosque.armadorhorarios.dto.SeccionDTO;
import co.edu.unbosque.armadorhorarios.exception.RegistroNoEncontradoException;
import co.edu.unbosque.armadorhorarios.services.AsignaturaService;
import co.edu.unbosque.armadorhorarios.services.SeccionService;

@RestController
@RequestMapping("/api/asignaturas")
@CrossOrigin(origins = "*")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private SeccionService seccionService;

    public AsignaturaController() {
    }

    @GetMapping
    public ResponseEntity<List<AsignaturaDTO>> listarTodas() {
        return new ResponseEntity<>(asignaturaService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            AsignaturaDTO asig = asignaturaService.buscarPorId(id);
            return new ResponseEntity<>(asig, HttpStatus.OK);
        } catch (RegistroNoEncontradoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/secciones")
    public ResponseEntity<List<SeccionDTO>> obtenerSeccionesDeAsignatura(@PathVariable Long id) {
        List<SeccionDTO> secciones = seccionService.listarPorAsignatura(id);
        return new ResponseEntity<>(secciones, HttpStatus.OK);
    }
}
