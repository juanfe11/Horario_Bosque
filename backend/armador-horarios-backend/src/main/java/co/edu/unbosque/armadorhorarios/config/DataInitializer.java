package co.edu.unbosque.armadorhorarios.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import co.edu.unbosque.armadorhorarios.entity.Asignatura;
import co.edu.unbosque.armadorhorarios.entity.BloqueHorario;
import co.edu.unbosque.armadorhorarios.entity.PlanEstudios;
import co.edu.unbosque.armadorhorarios.entity.Seccion;
import co.edu.unbosque.armadorhorarios.entity.Sesion;
import co.edu.unbosque.armadorhorarios.repository.AsignaturaRepository;
import co.edu.unbosque.armadorhorarios.repository.BloqueHorarioRepository;
import co.edu.unbosque.armadorhorarios.repository.PlanEstudiosRepository;
import co.edu.unbosque.armadorhorarios.repository.SeccionRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PlanEstudiosRepository planRepo;

    @Autowired
    private BloqueHorarioRepository bloqueRepo;

    @Autowired
    private AsignaturaRepository asignaturaRepo;

    @Autowired
    private SeccionRepository seccionRepo;

    // Consecutivo para docentes y salones
    private final AtomicInteger consecutivo = new AtomicInteger();

    // Generador de inscritos simulados
    private final Random random = new Random();

    private static final String ANT = "-ANT";

    @Override
    public void run(String... args) throws Exception {
        if (planRepo.count() > 0) {
            return;
        }

        // Bloques horarios
        List<BloqueHorario> bloques = new ArrayList<>();
        bloques.add(new BloqueHorario("07:00 - 09:00", 7, 9, false, true));
        bloques.add(new BloqueHorario("09:00 - 11:00", 9, 11, false, true));
        bloques.add(new BloqueHorario("11:00 - 13:00", 11, 13, false, true));
        bloques.add(new BloqueHorario("13:00 - 14:00 (Receso Almuerzo)", 13, 14, true, false));
        bloques.add(new BloqueHorario("14:00 - 16:00", 14, 16, false, true));
        bloques.add(new BloqueHorario("16:00 - 18:00", 16, 18, false, true));
        bloques.add(new BloqueHorario("18:00 - 20:00", 18, 20, false, true));
        bloques.add(new BloqueHorario("20:00 - 22:00", 20, 22, false, true));
        bloqueRepo.saveAll(bloques);

        // Planes de estudio
        PlanEstudios plan2025 = new PlanEstudios("PLAN-2025", "Ingenieria de Sistemas - Plan 2025", 48);
        PlanEstudios planAnterior = new PlanEstudios("PLAN-ANTERIOR", "Ingenieria de Sistemas - Plan Anterior", 54);
        planRepo.save(plan2025);
        planRepo.save(planAnterior);

        // Semestres 1, 2 y 3: ambos planes
        cargarSemestre1(plan2025, planAnterior);
        cargarSemestre2(plan2025, planAnterior);
        cargarSemestre3(plan2025, planAnterior);

        // Semestres 4 y 5: solo plan 2025
        cargarSemestre4(plan2025);
        cargarSemestre5(plan2025);

        // Semestres 4 a 9: solo plan anterior
        cargarAntSemestre4(planAnterior);
        cargarAntSemestre5(planAnterior);
        cargarAntSemestre6(planAnterior);
        cargarAntSemestre7(planAnterior);
        cargarAntSemestre8(planAnterior);
        cargarAntSemestre9(planAnterior);
    }

    // Crea y guarda una asignatura (creditos = horas, independientes = horas * 2)
    private Asignatura asignatura(String codigo, String nombre, int semestre, String componente, int horas,
            PlanEstudios plan, String sufijo) {
        Asignatura asig = new Asignatura(codigo + sufijo, nombre, semestre, componente, horas, horas, horas * 2, false,
                true, plan);
        return asignaturaRepo.save(asig);
    }

    // Crea y guarda una electiva (esElectiva = true) para las filas "Segun Eleccion"
    private Asignatura electiva(String codigo, String nombre, int semestre, int horas, PlanEstudios plan,
            String sufijo) {
        Asignatura asig = new Asignatura(codigo + sufijo, nombre, semestre, "Libre Eleccion", horas, horas, horas * 2,
                true, true, plan);
        return asignaturaRepo.save(asig);
    }

    // Crea una seccion por cada grupo de la lista (ej. "G1,G2,G3"), todas con el mismo docente y sesiones
    // Cada sesion se escribe como "DIA HORA_INICIO" y dura 2 horas. Docente null = Docente Asignado N
    private void secciones(Asignatura asig, String grupos, String docente, String... sesiones) {
        for (String grupo : grupos.split(",")) {
            int n = consecutivo.incrementAndGet();
            String doc = docente != null ? docente : "Docente Asignado " + n;
            Seccion sec = new Seccion("SEC-" + asig.getCodigo() + "-" + grupo, doc, "SALON-" + (101 + n % 60),
                    26 + random.nextInt(18), 44, asig);
            for (String s : sesiones) {
                String[] p = s.split(" ");
                int ini = Integer.parseInt(p[1]);
                sec.getSesiones().add(new Sesion(p[0], ini, ini + 2, sec));
            }
            seccionRepo.save(sec);
        }
    }

    // Semestre 1
    private void cargarSemestre1(PlanEstudios plan2025, PlanEstudios planAnterior) {
        // Matematicas 1 (solo plan 2025)
        Asignatura mat1 = asignatura("20994", "Matematicas 1", 1, "Ciencias Basicas", 4, plan2025, "");
        secciones(mat1, "G1,G2,G3", null, "MIERCOLES 8", "VIERNES 8");
        secciones(mat1, "G4,G5", null, "MIERCOLES 10", "VIERNES 10");
        secciones(mat1, "G6,G7", null, "MIERCOLES 13", "VIERNES 13");

        // Matematicas 1R (solo plan anterior)
        Asignatura mat1r = asignatura("20994", "Matematicas 1R", 1, "Ciencias Basicas", 4, planAnterior, ANT);
        secciones(mat1r, "G9", null, "MIERCOLES 7", "VIERNES 7");

        // Materias de ambos planes
        cargarSemestre1Comun(plan2025, "");
        cargarSemestre1Comun(planAnterior, ANT);
    }

    private void cargarSemestre1Comun(PlanEstudios plan, String suf) {
        // Desarrollo de sistemas de informacion 1
        Asignatura dsi1 = asignatura("21092", "Desarrollo de sistemas de informacion 1", 1, "Disciplinar", 6, plan, suf);
        secciones(dsi1, "G1", "Helio Ramirez", "LUNES 7", "MARTES 7", "JUEVES 7");
        secciones(dsi1, "G2", "David Rodriguez", "LUNES 7", "MARTES 7", "JUEVES 7");
        secciones(dsi1, "G3", "Helio Ramirez", "LUNES 7", "MARTES 9", "JUEVES 9");
        secciones(dsi1, "G4", "David Rodriguez", "LUNES 7", "MARTES 9", "JUEVES 9");

        // Principios de sistemas de informacion
        Asignatura psi = asignatura("21117", "Principios de sistemas de informacion", 1, "Disciplinar", 4, plan, suf);
        secciones(psi, "G1", "Alejandro Lopez", "MARTES 7", "JUEVES 7");
        secciones(psi, "G2", null, "MARTES 7", "JUEVES 7");
        secciones(psi, "G3", "Alejandro Lopez", "MARTES 9", "JUEVES 9");
        secciones(psi, "G4", "Carlos Delgado", "MARTES 9", "JUEVES 9");

        // Fundamentos de ingenieria
        Asignatura fund = asignatura("20959", "Fundamentos de ingenieria", 1, "Fundamentacion", 4, plan, suf);
        secciones(fund, "G1,G2", null, "MIERCOLES 8", "VIERNES 8");
        secciones(fund, "G3,G4", null, "MIERCOLES 10", "VIERNES 10");
        secciones(fund, "G5,G6,G7", null, "MIERCOLES 13", "VIERNES 13");

        // Ciencias para la vida
        Asignatura cien = asignatura("20952", "Ciencias para la vida", 1, "Institucional", 2, plan, suf);
        secciones(cien, "G1,G2", null, "MIERCOLES 8");
        secciones(cien, "G3,G4", null, "MIERCOLES 10");
        secciones(cien, "G6,G7", null, "MIERCOLES 13");

        // Estructuracion del pensamiento 1
        Asignatura pens1 = asignatura("20996", "Estructuracion del pensamiento 1", 1, "Sociohumanistica", 2, plan, suf);
        secciones(pens1, "G1,G2", null, "VIERNES 8");
        secciones(pens1, "G3,G4", null, "VIERNES 10");
        secciones(pens1, "G6,G7", null, "VIERNES 13");
    }

    // Semestre 2
    private void cargarSemestre2(PlanEstudios plan2025, PlanEstudios planAnterior) {
        // Matematicas 2 (solo plan 2025)
        Asignatura mat2 = asignatura("20995", "Matematicas 2", 2, "Ciencias Basicas", 4, plan2025, "");
        secciones(mat2, "G3,G4,G5,G6,G7", null, "MIERCOLES 9", "VIERNES 9");

        // Fisica 1 (solo plan 2025)
        Asignatura fis1 = asignatura("20998", "Fisica 1", 2, "Ciencias Basicas", 2, plan2025, "");
        secciones(fis1, "G1,G2,G3,G4", null, "MARTES 9");
        secciones(fis1, "G5,G6,G7", null, "JUEVES 9");

        // Estructuracion del pensamiento 2 (solo plan 2025)
        Asignatura pens2 = asignatura("20997", "Estructuracion del pensamiento 2", 2, "Sociohumanistica", 2, plan2025, "");
        secciones(pens2, "G1-2,G2-2,G3-2", null, "MARTES 14");
        secciones(pens2, "G6-2,G7-2,G8-2", null, "JUEVES 14");

        // Matematicas 2R (solo plan anterior)
        Asignatura mat2r = asignatura("20995", "Matematicas 2R", 2, "Ciencias Basicas", 4, planAnterior, ANT);
        secciones(mat2r, "G1,G2", null, "LUNES 7", "MIERCOLES 7");

        // Fisica 1R (solo plan anterior)
        Asignatura fis1r = asignatura("20998", "Fisica 1R", 2, "Ciencias Basicas", 2, planAnterior, ANT);
        secciones(fis1r, "G8", null, "VIERNES 7");

        // Estructuracion del pensamiento 2 Repitentes (solo plan anterior)
        Asignatura pens2r = asignatura("20997", "Estructuracion del pensamiento 2 (Repitentes)", 2, "Sociohumanistica", 2, planAnterior, ANT);
        secciones(pens2r, "G4-2,G5-2", null, "MIERCOLES 7");

        // Materias de ambos planes
        cargarSemestre2Comun(plan2025, "");
        cargarSemestre2Comun(planAnterior, ANT);
    }

    private void cargarSemestre2Comun(PlanEstudios plan, String suf) {
        // Desarrollo de sistemas de informacion 2
        Asignatura dsi2 = asignatura("21093", "Desarrollo de sistemas de informacion 2", 2, "Disciplinar", 6, plan, suf);
        secciones(dsi2, "G1", "Diego Rodriguez", "LUNES 16", "MIERCOLES 16", "VIERNES 16");
        secciones(dsi2, "G2", "Henry Luis Gomez", "LUNES 16", "MIERCOLES 16", "VIERNES 16");
        secciones(dsi2, "G3", "Wilson Rojas", "LUNES 16", "MIERCOLES 16", "VIERNES 16");
        secciones(dsi2, "G4", "Henry Luis Gomez", "LUNES 7", "MIERCOLES 7", "VIERNES 14");

        // Algebra lineal
        Asignatura alg = asignatura("21037", "Algebra lineal", 2, "Ciencias Basicas", 2, plan, suf);
        secciones(alg, "G1,G2,G3", null, "MIERCOLES 11");
        secciones(alg, "G5,G6", null, "VIERNES 11");

        // Desafios desde las ciencias para la ingenieria 1
        Asignatura des1 = asignatura("20954", "Desafios desde las ciencias para la ingenieria 1", 2, "Fundamentacion", 4, plan, suf);
        secciones(des1, "G1,G2,G3,G4", null, "MARTES 14", "MARTES 16");
        secciones(des1, "G5,G6,G7", null, "JUEVES 14", "JUEVES 16");
    }

    // Semestre 3
    private void cargarSemestre3(PlanEstudios plan2025, PlanEstudios planAnterior) {
        cargarSemestre3Comun(plan2025, "");
        cargarSemestre3Comun(planAnterior, ANT);
    }

    private void cargarSemestre3Comun(PlanEstudios plan, String suf) {
        // Bioestadistica y probabilidad
        Asignatura bio = asignatura("21036", "Bioestadistica y probabilidad", 3, "Ciencias Basicas", 2, plan, suf);
        secciones(bio, "G1,G2,G3,G4,G5,G6", null, "MARTES 7");

        // Estructuras de datos
        Asignatura ed = asignatura("21103", "Estructuras de datos", 3, "Disciplinar", 4, plan, suf);
        secciones(ed, "G1", "Alejandro Lopez", "MIERCOLES 9", "VIERNES 9");
        secciones(ed, "G2", "Gabriel Gutierrez", "LUNES 9", "VIERNES 16");
        secciones(ed, "G3", "David Rodriguez", "MIERCOLES 7", "VIERNES 7");

        // Idiomas 1 (el horario no trae codigo ni grupos)
        Asignatura idi1 = asignatura("IDI-301", "Idiomas 1", 3, "Lenguaje", 4, plan, suf);
        secciones(idi1, "G1", null, "MARTES 9", "JUEVES 9");
        secciones(idi1, "G2", null, "LUNES 16", "MIERCOLES 16");
        secciones(idi1, "G3", null, "VIERNES 16");

        // Desarrollo de sistemas de informacion 3
        Asignatura dsi3 = asignatura("21094", "Desarrollo de sistemas de informacion 3", 3, "Disciplinar", 6, plan, suf);
        secciones(dsi3, "G1", "Diego Rodriguez", "LUNES 14", "MIERCOLES 14", "VIERNES 14");
        secciones(dsi3, "G2", "Diego Rodriguez", "LUNES 11", "MIERCOLES 11", "VIERNES 11");
        secciones(dsi3, "G3", null, "LUNES 14", "MIERCOLES 14", "VIERNES 14");

        // Fisica 2
        Asignatura fis2 = asignatura("20999", "Fisica 2", 3, "Ciencias Basicas", 2, plan, suf);
        secciones(fis2, "G1,G2,G3", null, "MARTES 11");
        secciones(fis2, "G4,G5,G6", null, "JUEVES 11");

        // Desafios desde las ciencias para la ingenieria 2
        Asignatura des2 = asignatura("20955", "Desafios desde las ciencias para la ingenieria 2", 3, "Fundamentacion", 4, plan, suf);
        secciones(des2, "G1,G2,G3", null, "MARTES 14", "MARTES 16");
        secciones(des2, "G4,G5,G6", null, "JUEVES 14", "JUEVES 16");
    }

    // Semestre 4 (solo plan 2025)
    private void cargarSemestre4(PlanEstudios plan) {
        // Modelizacion informacional
        Asignatura mod = asignatura("21114", "Modelizacion informacional", 4, "Disciplinar", 4, plan, "");
        secciones(mod, "G1", null, "LUNES 7", "MIERCOLES 7");
        secciones(mod, "G2", null, "LUNES 9", "MIERCOLES 9");

        // Idiomas 2
        Asignatura idi2 = asignatura("IDI-402", "Idiomas 2", 4, "Lenguaje", 4, plan, "");
        secciones(idi2, "G1", null, "MARTES 14", "JUEVES 14");
        secciones(idi2, "G2", null, "LUNES 14", "VIERNES 14");
        secciones(idi2, "G3", null, "LUNES 9", "MIERCOLES 9");

        // Estructuras de sistemas
        Asignatura est = asignatura("21104", "Estructuras de sistemas", 4, "Disciplinar", 4, plan, "");
        secciones(est, "G1", "Gabriel Gutierrez", "MARTES 14", "MIERCOLES 14");
        secciones(est, "G2", "Gabriel Gutierrez", "MARTES 11", "JUEVES 9");

        // Infraestructura TI 1
        Asignatura inf1 = asignatura("21106", "Infraestructura TI 1", 4, "Disciplinar", 4, plan, "");
        secciones(inf1, "G1", "Antonio Avila", "LUNES 11", "JUEVES 11");
        secciones(inf1, "G2", "German Campos", "LUNES 11", "JUEVES 11");

        // Territorio y Sostenibilidad
        Asignatura ter = asignatura("20963", "Territorio y Sostenibilidad", 4, "Sociohumanistica", 2, plan, "");
        secciones(ter, "G1", null, "MIERCOLES 11");
        secciones(ter, "G2,G3", null, "JUEVES 14");
        secciones(ter, "G4,G5", null, "JUEVES 16");

        // Bases de datos 1
        Asignatura bd1 = asignatura("21089", "Bases de datos 1", 4, "Disciplinar", 6, plan, "");
        secciones(bd1, "G1", "Christian Duarte", "LUNES 16", "MIERCOLES 16", "VIERNES 16");
        secciones(bd1, "G2", null, "LUNES 16", "MIERCOLES 16", "VIERNES 16");

        // Proyecto Nucleo 1
        Asignatura pro1 = asignatura("21118", "Proyecto Nucleo 1", 4, "Disciplinar", 2, plan, "");
        secciones(pro1, "G1", "Orlando Lopez", "MARTES 16");
        secciones(pro1, "G2", "Wilson Rojas", "MARTES 16");
    }

    // Semestre 5 - Equivalencias (solo plan 2025)
    private void cargarSemestre5(PlanEstudios plan) {
        // Infraestructura TI 2
        Asignatura inf2 = asignatura("21107", "Infraestructura TI 2", 5, "Disciplinar", 4, plan, "");
        secciones(inf2, "G1", "John Chacon", "LUNES 9", "MIERCOLES 9");

        // Fundamentos de Ecologia
        Asignatura eco = asignatura("20958", "Fundamentos de Ecologia", 5, "Ciencias Basicas", 2, plan, "");
        secciones(eco, "G1", null, "MARTES 9");
        secciones(eco, "G2", null, "JUEVES 9");

        // Idiomas 3
        Asignatura idi3 = asignatura("20585", "Idiomas 3", 5, "Lenguaje", 4, plan, "");
        secciones(idi3, "G1", null, "MARTES 9", "JUEVES 9");
        secciones(idi3, "G2", null, "MARTES 11", "JUEVES 11");

        // Ingenieria de Software 1
        Asignatura isw1 = asignatura("21109", "Ingenieria de Software 1", 5, "Disciplinar", 4, plan, "");
        secciones(isw1, "G1", "Wilson Rojas", "MARTES 14", "JUEVES 14");
        secciones(isw1, "G2", "David Rodriguez", "MARTES 14", "JUEVES 14");

        // Bases de datos 2
        Asignatura bd2 = asignatura("21090", "Bases de datos 2", 5, "Disciplinar", 4, plan, "");
        secciones(bd2, "G1", "Christian Duarte", "MARTES 16", "JUEVES 16");
        secciones(bd2, "G2", "Francisco Barguil", "MARTES 16", "JUEVES 16");
    }

    // Semestre 4 - Repitentes (solo plan anterior)
    private void cargarAntSemestre4(PlanEstudios plan) {
        // Electiva 2
        Asignatura ele2 = electiva("ELE-402", "Electiva 2", 4, 2, plan, ANT);
        secciones(ele2, "G1", null, "SABADO 7");

        // Direccion y Planeacion
        Asignatura dir = asignatura("15328", "Direccion y Planeacion", 4, "Disciplinar", 4, plan, ANT);
        secciones(dir, "G1", "Jorge Puerto", "MIERCOLES 9", "VIERNES 9");

        // Estructuracion del Pensamiento 4 (Repitentes)
        Asignatura pens4 = asignatura("15301", "Estructuracion del Pensamiento 4 (Repitentes)", 4, "Sociohumanistica", 2, plan, ANT);
        secciones(pens4, "G1-4", null, "SABADO 9");

        // Complejidad Algoritmica
        Asignatura comp = asignatura("12999", "Complejidad Algoritmica", 4, "Disciplinar", 4, plan, ANT);
        secciones(comp, "G1", "Wilson Rojas", "MARTES 11", "JUEVES 11");
        secciones(comp, "G2", "Helio Ramirez", "MARTES 11", "JUEVES 11");

        // Sistemas Digitales y Arquitectura del Computador
        Asignatura sdig = asignatura("15327", "Sistemas Digitales y Arquitectura del Computador", 4, "Disciplinar", 2, plan, ANT);
        secciones(sdig, "G1", "Antonio Avila", "MIERCOLES 11");

        // Matematicas Aplicadas
        Asignatura mata = asignatura("15321", "Matematicas Aplicadas", 4, "Ciencias Basicas", 4, plan, ANT);
        secciones(mata, "G1", "Alejandro Lopez", "LUNES 14", "VIERNES 14");

        // Bases de datos 1
        Asignatura bd1 = asignatura("12998", "Bases de datos 1", 4, "Disciplinar", 6, plan, ANT);
        secciones(bd1, "G1", "Christian Duarte", "LUNES 16", "MIERCOLES 16", "VIERNES 16");
        secciones(bd1, "G2", null, "LUNES 16", "MIERCOLES 16", "VIERNES 16");
    }

    // Semestre 5 (solo plan anterior)
    private void cargarAntSemestre5(PlanEstudios plan) {
        // Sistemas Operacionales
        Asignatura so = asignatura("12997", "Sistemas Operacionales", 5, "Disciplinar", 4, plan, ANT);
        secciones(so, "G1", "Samuel Galindo", "LUNES 9", "JUEVES 11");
        secciones(so, "G2", "Samuel Galindo", "MARTES 9", "JUEVES 9");

        // Historia y Filosofia de la Ingenieria
        Asignatura hist = asignatura("15313", "Historia y Filosofia de la Ingenieria", 5, "Sociohumanistica", 2, plan, ANT);
        secciones(hist, "G2", null, "MIERCOLES 9");
        secciones(hist, "G3", null, "MIERCOLES 11");

        // Microprocesadores y Programacion Assembler
        Asignatura micro = asignatura("12996", "Microprocesadores y Programacion Assembler", 5, "Disciplinar", 4, plan, ANT);
        secciones(micro, "G1", "German Campos", "LUNES 14", "VIERNES 9");
        secciones(micro, "G2", "Antonio Avila", "LUNES 14", "VIERNES 9");

        // Dinamica de Sistemas
        Asignatura din = asignatura("13017", "Dinamica de Sistemas", 5, "Disciplinar", 4, plan, ANT);
        secciones(din, "G1", "Edier Espinosa", "VIERNES 11", "LUNES 16");
        secciones(din, "G2", "Carlos Rodriguez", "VIERNES 11", "LUNES 16");

        // Ingenieria de Software 1
        Asignatura isw1 = asignatura("13002", "Ingenieria de Software 1", 5, "Disciplinar", 4, plan, ANT);
        secciones(isw1, "G1", "Wilson Rojas", "MARTES 14", "JUEVES 14");
        secciones(isw1, "G2", "David Rodriguez", "MARTES 14", "JUEVES 14");

        // Bases de datos 2
        Asignatura bd2 = asignatura("13001", "Bases de datos 2", 5, "Disciplinar", 4, plan, ANT);
        secciones(bd2, "G1", "Christian Duarte", "MARTES 16", "JUEVES 16");
        secciones(bd2, "G2", "Francisco Barguil", "MARTES 16", "JUEVES 16");

        // Electiva 3
        Asignatura ele3 = electiva("ELE-503", "Electiva 3", 5, 2, plan, ANT);
        secciones(ele3, "G1", null, "SABADO 16");
    }

    // Semestre 6 (solo plan anterior)
    private void cargarAntSemestre6(PlanEstudios plan) {
        // Ingenieria de Software 2 (el horario trae 3005 y 13005, se unifican en 13005)
        Asignatura isw2 = asignatura("13005", "Ingenieria de Software 2", 6, "Disciplinar", 4, plan, ANT);
        secciones(isw2, "G1", "Andres Rey", "MARTES 16", "JUEVES 16");
        secciones(isw2, "G2", "Wilson Rojas", "LUNES 7", "VIERNES 14");
        secciones(isw2, "G3", "Andres Rey", "LUNES 7", "VIERNES 14");

        // Gestion de Proyectos
        Asignatura gest = asignatura("13007", "Gestion de Proyectos", 6, "Disciplinar", 4, plan, ANT);
        secciones(gest, "G1", "Samuel Galindo", "MARTES 7", "VIERNES 7");
        secciones(gest, "G2", "Jorge Gomez", "MARTES 7", "VIERNES 7");
        secciones(gest, "G3", "Ismael Ardila", "MARTES 7", "VIERNES 7");

        // Redes de Datos 1
        Asignatura redes1 = asignatura("13000", "Redes de Datos 1", 6, "Disciplinar", 4, plan, ANT);
        secciones(redes1, "G1", "John Chacon", "LUNES 9", "MIERCOLES 9");
        secciones(redes1, "G2", "John Chacon", "VIERNES 9", "MIERCOLES 11");

        // Modelos y Simulacion de Sistemas
        Asignatura mod = asignatura("13011", "Modelos y Simulacion de Sistemas", 6, "Disciplinar", 4, plan, ANT);
        secciones(mod, "G1", "Edier Espinosa", "LUNES 11", "MIERCOLES 14");
        secciones(mod, "G2", "Carlos Rodriguez", "LUNES 11", "MIERCOLES 14");
        secciones(mod, "G3", "Edier Espinosa", "MARTES 11", "JUEVES 9");

        // Labor Social (el horario no trae codigo)
        Asignatura labor = asignatura("LAB-SOC", "Labor Social", 6, "Institucional", 2, plan, ANT);
        secciones(labor, "G01D,G02D,G03D", "Edier Espinosa", "JUEVES 11");

        // Proyecto Nucleo 2
        Asignatura pro2 = asignatura("15319", "Proyecto Nucleo 2", 6, "Disciplinar", 2, plan, ANT);
        secciones(pro2, "G1", "Samuel Galindo", "VIERNES 11");
        secciones(pro2, "G2", "Cristian Vanegas", "VIERNES 16");

        // Investigacion Tecnologica y de Ingenieria
        Asignatura inv = asignatura("13006", "Investigacion Tecnologica y de Ingenieria", 6, "Disciplinar", 2, plan, ANT);
        secciones(inv, "G1", "Leonardo Ramirez", "LUNES 16");
        secciones(inv, "G2", "Leonardo Ramirez", "MIERCOLES 16");
    }

    // Semestre 7 (solo plan anterior)
    private void cargarAntSemestre7(PlanEstudios plan) {
        // Seminario de Investigacion
        Asignatura semi = asignatura("13010", "Seminario de Investigacion", 7, "Disciplinar", 2, plan, ANT);
        secciones(semi, "G3", "Judy Moreno", "MIERCOLES 9");

        // Taller de Practica Profesional
        Asignatura taller = asignatura("12991", "Taller de Practica Profesional", 7, "Disciplinar", 2, plan, ANT);
        secciones(taller, "G1", "Sandra Ayala", "JUEVES 9");
        secciones(taller, "G2", "Ana Maria Arcila", "JUEVES 9");

        // Ingenieria de Software 3
        Asignatura isw3 = asignatura("15341", "Ingenieria de Software 3", 7, "Disciplinar", 4, plan, ANT);
        secciones(isw3, "G1", "Samuel Galindo", "LUNES 11", "VIERNES 9");
        secciones(isw3, "G2", "Samuel Galindo", "LUNES 14", "JUEVES 14");

        // Gestion de Tecnologia e Innovacion
        Asignatura gti = asignatura("13024", "Gestion de Tecnologia e Innovacion", 7, "Disciplinar", 2, plan, ANT);
        secciones(gti, "G1", "Carlos Delgado", "MIERCOLES 11");
        secciones(gti, "G2", "Alejandro Lopez", "MIERCOLES 11");

        // Legislacion Normatividad y Ciudadania
        Asignatura leg = asignatura("12993", "Legislacion Normatividad y Ciudadania", 7, "Sociohumanistica", 2, plan, ANT);
        secciones(leg, "G1", "German Neuta", "VIERNES 11");
        secciones(leg, "G2", "Jorge Puerto", "VIERNES 11");

        // Redes de Datos 2
        Asignatura redes2 = asignatura("15340", "Redes de Datos 2", 7, "Disciplinar", 4, plan, ANT);
        secciones(redes2, "G1", "German Campos", "MIERCOLES 16", "VIERNES 16");
        secciones(redes2, "G2", "Edward Guillen", "MIERCOLES 16", "VIERNES 16");

        // Electiva 4
        Asignatura ele4 = electiva("ELE-704", "Electiva 4", 7, 2, plan, ANT);
        secciones(ele4, "G1", null, "SABADO 20");

        // Materias de enfasis (semestre 7)
        cargarAntEnfasis(plan, 7, ANT);
        cargarAntEnfasisJueves(plan, 7, ANT);
    }

    // Semestre 8 (solo plan anterior)
    private void cargarAntSemestre8(PlanEstudios plan) {
        // Seguridad de la Informacion
        Asignatura seg = asignatura("13009", "Seguridad de la Informacion", 8, "Disciplinar", 4, plan, ANT);
        secciones(seg, "G1", "Cesar Beltran", "MARTES 18", "JUEVES 18");
        secciones(seg, "G2", "John Chacon", "MARTES 11", "VIERNES 11");
        secciones(seg, "G3", "Diego Rodriguez", "MARTES 18", "JUEVES 18");

        // Sistemas Inteligentes
        Asignatura sint = asignatura("13053", "Sistemas Inteligentes", 8, "Disciplinar", 4, plan, ANT);
        secciones(sint, "G1", "Carlos Rodriguez", "MARTES 16", "JUEVES 16");
        secciones(sint, "G2", "Carlos Rodriguez", "MARTES 14", "JUEVES 14");
        secciones(sint, "G3", "Carlos Rodriguez", "LUNES 14", "MIERCOLES 16");

        // Compiladores
        Asignatura compi = asignatura("1119", "Compiladores", 8, "Disciplinar", 2, plan, ANT);
        secciones(compi, "G1", null, "VIERNES 16");
        secciones(compi, "G2", null, "MIERCOLES 14");
        secciones(compi, "G3", null, "VIERNES 14");

        // Proyecto de Grado 1
        Asignatura pg1 = asignatura("13013", "Proyecto de Grado 1", 8, "Integracion", 2, plan, ANT);
        secciones(pg1, "G1", "Judy Moreno", "LUNES 16");
        secciones(pg1, "G2", "Ismael Ardila", "LUNES 18");
        secciones(pg1, "G3", null, "LUNES 18");

        // Electiva 5
        Asignatura ele5 = electiva("ELE-805", "Electiva 5", 8, 2, plan, ANT);
        secciones(ele5, "G1", null, "SABADO 18");

        // Electiva 6
        Asignatura ele6 = electiva("ELE-806", "Electiva 6", 8, 2, plan, ANT);
        secciones(ele6, "G1", null, "SABADO 20");

        // Materias de enfasis (semestre 8)
        cargarAntEnfasis(plan, 8, ANT + "-S8");
    }

    // Semestre 9 (solo plan anterior)
    private void cargarAntSemestre9(PlanEstudios plan) {
        // Practica Profesional
        Asignatura prac = asignatura("13014", "Practica Profesional", 9, "Integracion", 2, plan, ANT);
        secciones(prac, "G1", "Sandra Ayala", "SABADO 9");

        // Proyecto de Grado 2
        Asignatura pg2 = asignatura("13015", "Proyecto de Grado 2", 9, "Integracion", 2, plan, ANT);
        secciones(pg2, "G1", "Judy Moreno", "LUNES 18");
        secciones(pg2, "G2", "Ismael Ardila", "MARTES 18");

        // Electiva 7
        Asignatura ele7 = electiva("ELE-907", "Electiva 7", 9, 2, plan, ANT);
        secciones(ele7, "G1", null, "SABADO 18");

        // Materias de enfasis (semestre 9)
        cargarAntEnfasis(plan, 9, ANT + "-S9");
        cargarAntEnfasisJueves(plan, 9, ANT + "-S9");
    }

    // Materias de enfasis que se repiten en varios semestres (una copia por semestre)
    private void cargarAntEnfasis(PlanEstudios plan, int semestre, String suf) {
        // Materias de enfasis - miercoles 18
        Asignatura bigData = asignatura("17453", "Big Data and Analytics", semestre, "Disciplinar", 2, plan, suf);
        secciones(bigData, "G1", "Alvaro Cangrejo", "MIERCOLES 18");
        Asignatura comdig = asignatura("15615", "Comunicaciones Digitales", semestre, "Disciplinar", 2, plan, suf);
        secciones(comdig, "G1", "German Campos", "MIERCOLES 18");
        Asignatura moviles = asignatura("20145", "Desarrollo de Aplicaciones Moviles", semestre, "Disciplinar", 2, plan, suf);
        secciones(moviles, "G1", "Juan Camilo Hernandez", "MIERCOLES 18");
        Asignatura audit = asignatura("24209", "Auditoria de Sistemas de Informacion", semestre, "Disciplinar", 2, plan, suf);
        secciones(audit, "G1", "Alejandro Lopez", "MIERCOLES 18");
        Asignatura tend = asignatura("24207", "Tendencias avanzadas en Ingenieria de Software asistida por IA", semestre, "Disciplinar", 2, plan, suf);
        secciones(tend, "G1", "Andres Rey", "MIERCOLES 18");

        // Analisis Exploratorio y Visualizacion de datos
        Asignatura anal = asignatura("18475", "Analisis Exploratorio y Visualizacion de datos", semestre, "Disciplinar", 2, plan, suf);
        secciones(anal, "G1", "Alvaro Cangrejo", "MIERCOLES 20");

        // Fundamentos de Computacion en la nube y Arquitectura Serverless
        Asignatura nube = asignatura("23634", "Fundamentos de Computacion en la nube y Arquitectura Serverless", semestre, "Disciplinar", 2, plan, suf);
        secciones(nube, "G1", "Jonathan Briceno", "SABADO 7");
    }

    // Materias de enfasis que solo aparecen en los semestres 7 y 9
    private void cargarAntEnfasisJueves(PlanEstudios plan, int semestre, String suf) {
        Asignatura dist = asignatura("15616", "Sistemas Distribuidos", semestre, "Disciplinar", 2, plan, suf);
        secciones(dist, "G1", "Hernando Camargo", "JUEVES 18");
        Asignatura block = asignatura("18555", "Fundamentos de BlockChain y Criptomonedas", semestre, "Disciplinar", 2, plan, suf);
        secciones(block, "G1", "Wilson Rojas", "JUEVES 18");
        Asignatura llm = asignatura("24208", "Aplicaciones basadas en grandes modelos de lenguaje", semestre, "Disciplinar", 2, plan, suf);
        secciones(llm, "G1", "Andres Rey", "JUEVES 18");
    }
}