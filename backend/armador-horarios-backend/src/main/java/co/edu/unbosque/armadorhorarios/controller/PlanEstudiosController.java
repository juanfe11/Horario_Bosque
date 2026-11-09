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
import co.edu.unbosque.armadorhorarios.dto.PlanEstudiosDTO;
import co.edu.unbosque.armadorhorarios.exception.RegistroNoEncontradoException;
import co.edu.unbosque.armadorhorarios.services.AsignaturaService;
import co.edu.unbosque.armadorhorarios.services.PlanEstudiosService;

@RestController
@RequestMapping("/api/planes")
@CrossOrigin(origins = "*")
public class PlanEstudiosController {

    @Autowired
    private PlanEstudiosService planService;

    @Autowired
    private AsignaturaService asignaturaService;

    public PlanEstudiosController() {
    }

    @GetMapping
    public ResponseEntity<List<PlanEstudiosDTO>> listarTodosLosPlanes() {
        List<PlanEstudiosDTO> lista = planService.getAll();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{codigoPlan}")
    public ResponseEntity<?> obtenerPorCodigo(@PathVariable String codigoPlan) {
        try {
            PlanEstudiosDTO plan = planService.buscarPorCodigoPlan(codigoPlan);
            return new ResponseEntity<>(plan, HttpStatus.OK);
        } catch (RegistroNoEncontradoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{codigoPlan}/asignaturas")
    public ResponseEntity<List<AsignaturaDTO>> listarAsignaturasDePlan(@PathVariable String codigoPlan) {
        List<AsignaturaDTO> asignaturas = asignaturaService.listarPorCodigoPlan(codigoPlan);
        return new ResponseEntity<>(asignaturas, HttpStatus.OK);
    }

    @GetMapping("/{codigoPlan}/semestre/{semestre}")
    public ResponseEntity<?> listarPorSemestre(@PathVariable String codigoPlan, @PathVariable Integer semestre) {
        try {
            PlanEstudiosDTO plan = planService.buscarPorCodigoPlan(codigoPlan);
            List<AsignaturaDTO> filtradas = asignaturaService.listarPorPlanYSemestre(plan.getId(), semestre);
            return new ResponseEntity<>(filtradas, HttpStatus.OK);
        } catch (RegistroNoEncontradoException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
