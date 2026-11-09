package co.edu.unbosque.armadorhorarios.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.armadorhorarios.dto.BloqueHorarioDTO;
import co.edu.unbosque.armadorhorarios.dto.BusquedaCombinacionesRequestDTO;
import co.edu.unbosque.armadorhorarios.dto.ResultadoBusquedaDTO;
import co.edu.unbosque.armadorhorarios.dto.ResultadoValidacionDTO;
import co.edu.unbosque.armadorhorarios.dto.ValidacionSeccionRequestDTO;
import co.edu.unbosque.armadorhorarios.repository.BloqueHorarioRepository;
import co.edu.unbosque.armadorhorarios.services.BuscadorCombinacionesService;
import co.edu.unbosque.armadorhorarios.services.ValidadorHorarioService;

@RestController
@RequestMapping("/api/horario")
@CrossOrigin(origins = "*")
public class HorarioController {

    @Autowired
    private BloqueHorarioRepository bloqueRepo;

    @Autowired
    private ValidadorHorarioService validadorService;

    @Autowired
    private BuscadorCombinacionesService buscadorService;

    @Autowired
    private ModelMapper mapper;

    public HorarioController() {
    }

    @GetMapping("/bloques")
    public ResponseEntity<List<BloqueHorarioDTO>> listarBloques() {
        List<BloqueHorarioDTO> bloques = bloqueRepo.findAll().stream()
                .map(b -> mapper.map(b, BloqueHorarioDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(bloques, HttpStatus.OK);
    }

    @PostMapping("/validar-seccion")
    public ResponseEntity<ResultadoValidacionDTO> validarColocacionSeccion(
            @RequestBody ValidacionSeccionRequestDTO peticion) {
        ResultadoValidacionDTO resultado = validadorService.validarSeccion(peticion);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @PostMapping("/buscar-combinaciones")
    public ResponseEntity<ResultadoBusquedaDTO> buscarCombinaciones(
            @RequestBody BusquedaCombinacionesRequestDTO peticion) {
        ResultadoBusquedaDTO resultado = buscadorService.buscarCombinaciones(peticion);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
