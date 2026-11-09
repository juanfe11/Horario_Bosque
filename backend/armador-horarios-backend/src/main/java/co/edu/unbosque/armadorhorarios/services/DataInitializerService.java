package co.edu.unbosque.armadorhorarios.services;

import java.util.ArrayList;
import java.util.List;

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
public class DataInitializerService implements CommandLineRunner {

    @Autowired
    private PlanEstudiosRepository planRepo;

    @Autowired
    private BloqueHorarioRepository bloqueRepo;

    @Autowired
    private AsignaturaRepository asignaturaRepo;

    @Autowired
    private SeccionRepository seccionRepo;

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

        // Plan 2025 - Semestre 1
        // Matematicas 1
        Asignatura asigMat1 = new Asignatura("MAT-101", "Matematicas 1", 1, "Ciencias Basicas", 4, 4, 8, false, true, plan2025);
        asignaturaRepo.save(asigMat1);
        Seccion secMat1A = new Seccion("SEC-MAT1-A", "Docente Asignado 1", "SALON-101", 38, 44, asigMat1);
        secMat1A.getSesiones().add(new Sesion("LUNES", 7, 9, secMat1A));
        secMat1A.getSesiones().add(new Sesion("MIERCOLES", 7, 9, secMat1A));
        seccionRepo.save(secMat1A);
        Seccion secMat1B = new Seccion("SEC-MAT1-B", "Docente Asignado 2", "SALON-102", 44, 44, asigMat1);
        secMat1B.getSesiones().add(new Sesion("MARTES", 9, 11, secMat1B));
        secMat1B.getSesiones().add(new Sesion("JUEVES", 9, 11, secMat1B));
        seccionRepo.save(secMat1B);

        // Fundamentos de ingenieria
        Asignatura asigFund1 = new Asignatura("ING-101", "Fundamentos de ingenieria", 1, "Fundamentacion", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigFund1);
        Seccion secFund1A = new Seccion("SEC-FUND-A", "Docente Asignado 3", "SALON-103", 35, 44, asigFund1);
        secFund1A.getSesiones().add(new Sesion("VIERNES", 7, 9, secFund1A));
        seccionRepo.save(secFund1A);
        Seccion secFund1B = new Seccion("SEC-FUND-B", "Docente Asignado 4", "SALON-104", 30, 44, asigFund1);
        secFund1B.getSesiones().add(new Sesion("MARTES", 14, 16, secFund1B));
        seccionRepo.save(secFund1B);

        // Ciencias para la vida
        Asignatura asigCien1 = new Asignatura("BIO-101", "Ciencias para la vida", 1, "Institucional", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigCien1);
        Seccion secCien1A = new Seccion("SEC-CIEN-A", "Docente Asignado 5", "AUD-101", 32, 44, asigCien1);
        secCien1A.getSesiones().add(new Sesion("JUEVES", 14, 16, secCien1A));
        seccionRepo.save(secCien1A);
        Seccion secCien1B = new Seccion("SEC-CIEN-B", "Docente Asignado 6", "AUD-102", 28, 44, asigCien1);
        secCien1B.getSesiones().add(new Sesion("VIERNES", 11, 13, secCien1B));
        seccionRepo.save(secCien1B);

        // Estructuracion del pensamiento 1
        Asignatura asigPens1 = new Asignatura("HUM-101", "Estructuracion del pensamiento 1", 1, "Sociohumanistica", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigPens1);
        Seccion secPens1A = new Seccion("SEC-PENS1-A", "Docente Asignado 7", "SALON-201", 36, 44, asigPens1);
        secPens1A.getSesiones().add(new Sesion("MIERCOLES", 11, 13, secPens1A));
        seccionRepo.save(secPens1A);
        Seccion secPens1B = new Seccion("SEC-PENS1-B", "Docente Asignado 8", "SALON-202", 40, 44, asigPens1);
        secPens1B.getSesiones().add(new Sesion("SABADO", 9, 11, secPens1B));
        seccionRepo.save(secPens1B);

        // Desarrollo de sistemas de informacion 1
        Asignatura asigDsi1 = new Asignatura("SIS-101", "Desarrollo de sistemas de informacion 1", 1, "Disciplinar", 4, 4, 8, false, true, plan2025);
        asignaturaRepo.save(asigDsi1);
        Seccion secDsi1A = new Seccion("SEC-DSI1-A", "Docente Asignado 9", "LAB-INFO-1", 39, 44, asigDsi1);
        secDsi1A.getSesiones().add(new Sesion("MARTES", 16, 18, secDsi1A));
        secDsi1A.getSesiones().add(new Sesion("JUEVES", 16, 18, secDsi1A));
        seccionRepo.save(secDsi1A);
        Seccion secDsi1B = new Seccion("SEC-DSI1-B", "Docente Asignado 10", "LAB-INFO-2", 44, 44, asigDsi1);
        secDsi1B.getSesiones().add(new Sesion("LUNES", 18, 20, secDsi1B));
        secDsi1B.getSesiones().add(new Sesion("MIERCOLES", 18, 20, secDsi1B));
        seccionRepo.save(secDsi1B);

        // Principios de sistemas de informacion
        Asignatura asigPsi1 = new Asignatura("SIS-102", "Principios de sistemas de informacion", 1, "Disciplinar", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigPsi1);
        Seccion secPsi1A = new Seccion("SEC-PSI-A", "Docente Asignado 11", "LAB-SW-1", 34, 44, asigPsi1);
        secPsi1A.getSesiones().add(new Sesion("LUNES", 9, 11, secPsi1A));
        seccionRepo.save(secPsi1A);
        Seccion secPsi1B = new Seccion("SEC-PSI-B", "Docente Asignado 12", "LAB-SW-2", 30, 44, asigPsi1);
        secPsi1B.getSesiones().add(new Sesion("MIERCOLES", 14, 16, secPsi1B));
        seccionRepo.save(secPsi1B);

        // Plan 2025 - Semestre 2
        // Matematicas 2
        Asignatura asigMat2 = new Asignatura("MAT-201", "Matematicas 2", 2, "Ciencias Basicas", 4, 4, 8, false, true, plan2025);
        asignaturaRepo.save(asigMat2);
        Seccion secMat2A = new Seccion("SEC-MAT2-A", "Docente Asignado 1", "SALON-105", 36, 44, asigMat2);
        secMat2A.getSesiones().add(new Sesion("LUNES", 9, 11, secMat2A));
        secMat2A.getSesiones().add(new Sesion("MIERCOLES", 9, 11, secMat2A));
        seccionRepo.save(secMat2A);

        // Algebra lineal
        Asignatura asigAlg2 = new Asignatura("MAT-202", "Algebra lineal", 2, "Ciencias Basicas", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigAlg2);
        Seccion secAlg2A = new Seccion("SEC-ALG-A", "Docente Asignado 2", "SALON-106", 38, 44, asigAlg2);
        secAlg2A.getSesiones().add(new Sesion("MARTES", 7, 9, secAlg2A));
        seccionRepo.save(secAlg2A);

        // Fisica 1
        Asignatura asigFis1 = new Asignatura("FIS-201", "Fisica 1", 2, "Ciencias Basicas", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigFis1);
        Seccion secFis1A = new Seccion("SEC-FIS1-A", "Docente Asignado 3", "LAB-FIS-1", 33, 44, asigFis1);
        secFis1A.getSesiones().add(new Sesion("JUEVES", 7, 9, secFis1A));
        seccionRepo.save(secFis1A);

        // Desafios desde las ciencias para la ingenieria 1
        Asignatura asigDes1 = new Asignatura("ING-201", "Desafios desde las ciencias para la ingenieria 1", 2, "Fundamentacion", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigDes1);
        Seccion secDes1A = new Seccion("SEC-DES1-A", "Docente Asignado 4", "SALON-203", 31, 44, asigDes1);
        secDes1A.getSesiones().add(new Sesion("VIERNES", 9, 11, secDes1A));
        seccionRepo.save(secDes1A);

        // Estructuracion del pensamiento 2
        Asignatura asigPens2 = new Asignatura("HUM-201", "Estructuracion del pensamiento 2", 2, "Sociohumanistica", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigPens2);
        Seccion secPens2A = new Seccion("SEC-PENS2-A", "Docente Asignado 5", "SALON-204", 37, 44, asigPens2);
        secPens2A.getSesiones().add(new Sesion("MARTES", 11, 13, secPens2A));
        seccionRepo.save(secPens2A);

        // Desarrollo de sistemas de informacion 2
        Asignatura asigDsi2 = new Asignatura("SIS-201", "Desarrollo de sistemas de informacion 2", 2, "Disciplinar", 4, 4, 8, false, true, plan2025);
        asignaturaRepo.save(asigDsi2);
        Seccion secDsi2A = new Seccion("SEC-DSI2-A", "Docente Asignado 6", "LAB-INFO-3", 35, 44, asigDsi2);
        secDsi2A.getSesiones().add(new Sesion("LUNES", 14, 16, secDsi2A));
        secDsi2A.getSesiones().add(new Sesion("MIERCOLES", 14, 16, secDsi2A));
        seccionRepo.save(secDsi2A);

        // Electiva libre 1
        Asignatura asigElec1 = new Asignatura("ELE-201", "Electiva libre 1", 2, "Libre Eleccion", 2, 2, 4, true, true, plan2025);
        asignaturaRepo.save(asigElec1);
        Seccion secElec1A = new Seccion("SEC-ELEC1-A", "Docente Asignado 7", "SALON-205", 29, 44, asigElec1);
        secElec1A.getSesiones().add(new Sesion("VIERNES", 14, 16, secElec1A));
        seccionRepo.save(secElec1A);

        // Plan 2025 - Semestre 3
        // Bioestadistica y probabilidad
        Asignatura asigBio3 = new Asignatura("MAT-301", "Bioestadistica y probabilidad", 3, "Ciencias Basicas", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigBio3);
        Seccion secBio3A = new Seccion("SEC-BIO-A", "Docente Asignado 8", "SALON-107", 34, 44, asigBio3);
        secBio3A.getSesiones().add(new Sesion("LUNES", 11, 13, secBio3A));
        seccionRepo.save(secBio3A);

        // Fisica 2
        Asignatura asigFis2 = new Asignatura("FIS-301", "Fisica 2", 3, "Ciencias Basicas", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigFis2);
        Seccion secFis2A = new Seccion("SEC-FIS2-A", "Docente Asignado 9", "LAB-FIS-2", 36, 44, asigFis2);
        secFis2A.getSesiones().add(new Sesion("MIERCOLES", 11, 13, secFis2A));
        seccionRepo.save(secFis2A);

        // Desafios desde las ciencias para la ingenieria 2
        Asignatura asigDes2 = new Asignatura("ING-301", "Desafios desde las ciencias para la ingenieria 2", 3, "Fundamentacion", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigDes2);
        Seccion secDes2A = new Seccion("SEC-DES2-A", "Docente Asignado 10", "SALON-206", 30, 44, asigDes2);
        secDes2A.getSesiones().add(new Sesion("JUEVES", 11, 13, secDes2A));
        seccionRepo.save(secDes2A);

        // Idiomas 1
        Asignatura asigIdi1 = new Asignatura("IDI-301", "Idiomas 1", 3, "Lenguaje", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigIdi1);
        Seccion secIdi1A = new Seccion("SEC-IDI1-A", "Docente Asignado 11", "LAB-ING-1", 40, 44, asigIdi1);
        secIdi1A.getSesiones().add(new Sesion("MARTES", 14, 16, secIdi1A));
        seccionRepo.save(secIdi1A);

        // Desarrollo de sistemas de informacion 3
        Asignatura asigDsi3 = new Asignatura("SIS-301", "Desarrollo de sistemas de informacion 3", 3, "Disciplinar", 4, 4, 8, false, true, plan2025);
        asignaturaRepo.save(asigDsi3);
        Seccion secDsi3A = new Seccion("SEC-DSI3-A", "Docente Asignado 12", "LAB-INFO-4", 38, 44, asigDsi3);
        secDsi3A.getSesiones().add(new Sesion("MARTES", 16, 18, secDsi3A));
        secDsi3A.getSesiones().add(new Sesion("JUEVES", 16, 18, secDsi3A));
        seccionRepo.save(secDsi3A);

        // Estructuras de datos
        Asignatura asigEd3 = new Asignatura("SIS-302", "Estructuras de datos", 3, "Disciplinar", 2, 2, 4, false, true, plan2025);
        asignaturaRepo.save(asigEd3);
        Seccion secEd3A = new Seccion("SEC-ED-A", "Docente Asignado 1", "LAB-INFO-1", 35, 44, asigEd3);
        secEd3A.getSesiones().add(new Sesion("VIERNES", 7, 9, secEd3A));
        seccionRepo.save(secEd3A);

        // Electiva libre 2
        Asignatura asigElec2 = new Asignatura("ELE-301", "Electiva libre 2", 3, "Libre Eleccion", 2, 2, 4, true, true, plan2025);
        asignaturaRepo.save(asigElec2);
        Seccion secElec2A = new Seccion("SEC-ELEC2-A", "Docente Asignado 2", "SALON-207", 25, 44, asigElec2);
        secElec2A.getSesiones().add(new Sesion("SABADO", 7, 9, secElec2A));
        seccionRepo.save(secElec2A);

        // Plan Anterior - Semestre 1
        // Matematicas basicas
        Asignatura asigAntMatb = new Asignatura("ANT-101", "Matematicas basicas", 1, "Ciencias Basicas", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntMatb);
        Seccion secAntMatbA = new Seccion("SEC-ANT-MATB-A", "Docente Asignado 1", "SALON-301", 38, 44, asigAntMatb);
        secAntMatbA.getSesiones().add(new Sesion("LUNES", 7, 9, secAntMatbA));
        secAntMatbA.getSesiones().add(new Sesion("MIERCOLES", 7, 9, secAntMatbA));
        seccionRepo.save(secAntMatbA);

        // Logica matematica
        Asignatura asigAntLog = new Asignatura("ANT-102", "Logica matematica", 1, "Ciencias Basicas", 2, 2, 4, false, true, planAnterior);
        asignaturaRepo.save(asigAntLog);
        Seccion secAntLogA = new Seccion("SEC-ANT-LOG-A", "Docente Asignado 2", "SALON-302", 34, 44, asigAntLog);
        secAntLogA.getSesiones().add(new Sesion("MARTES", 7, 9, secAntLogA));
        seccionRepo.save(secAntLogA);

        // Matematicas discretas
        Asignatura asigAntDisc = new Asignatura("ANT-103", "Matematicas discretas", 1, "Ciencias Basicas", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntDisc);
        Seccion secAntDiscA = new Seccion("SEC-ANT-DISC-A", "Docente Asignado 3", "SALON-303", 35, 44, asigAntDisc);
        secAntDiscA.getSesiones().add(new Sesion("JUEVES", 7, 9, secAntDiscA));
        secAntDiscA.getSesiones().add(new Sesion("SABADO", 7, 9, secAntDiscA));
        seccionRepo.save(secAntDiscA);

        // Fundamentos de programacion
        Asignatura asigAntFp = new Asignatura("ANT-104", "Fundamentos de programacion", 1, "Disciplinar", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntFp);
        Seccion secAntFpA = new Seccion("SEC-ANT-FP-A", "Docente Asignado 4", "LAB-INFO-1", 40, 44, asigAntFp);
        secAntFpA.getSesiones().add(new Sesion("LUNES", 9, 11, secAntFpA));
        secAntFpA.getSesiones().add(new Sesion("MIERCOLES", 9, 11, secAntFpA));
        seccionRepo.save(secAntFpA);

        // Introduccion a la ingenieria de sistemas
        Asignatura asigAntIntro = new Asignatura("ANT-105", "Introduccion a la ingenieria de sistemas", 1, "Fundamentacion", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntIntro);
        Seccion secAntIntroA = new Seccion("SEC-ANT-INTRO-A", "Docente Asignado 5", "SALON-304", 36, 44, asigAntIntro);
        secAntIntroA.getSesiones().add(new Sesion("MARTES", 9, 11, secAntIntroA));
        seccionRepo.save(secAntIntroA);

        // Est. del pensamiento I. Procesos logicos de expresion
        Asignatura asigAntPens1 = new Asignatura("ANT-106", "Est. del pensamiento I. Procesos logicos de expresion", 1, "Sociohumanistica", 1, 1, 2, false, true, planAnterior);
        asignaturaRepo.save(asigAntPens1);
        Seccion secAntPens1A = new Seccion("SEC-ANT-PENS-A", "Docente Asignado 6", "SALON-305", 30, 44, asigAntPens1);
        secAntPens1A.getSesiones().add(new Sesion("VIERNES", 9, 11, secAntPens1A));
        seccionRepo.save(secAntPens1A);

        // Electiva 1
        Asignatura asigAntElec1 = new Asignatura("ANT-107", "Electiva 1", 1, "Libre Eleccion", 2, 2, 4, true, true, planAnterior);
        asignaturaRepo.save(asigAntElec1);
        Seccion secAntElec1A = new Seccion("SEC-ANT-ELEC-A", "Docente Asignado 7", "SALON-306", 28, 44, asigAntElec1);
        secAntElec1A.getSesiones().add(new Sesion("VIERNES", 14, 16, secAntElec1A));
        seccionRepo.save(secAntElec1A);

        // Plan Anterior - Semestre 2
        // Calculo diferencial
        Asignatura asigAntCalc = new Asignatura("ANT-201", "Calculo diferencial", 2, "Ciencias Basicas", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntCalc);
        Seccion secAntCalcA = new Seccion("SEC-ANT-CALC-A", "Docente Asignado 1", "SALON-401", 37, 44, asigAntCalc);
        secAntCalcA.getSesiones().add(new Sesion("MARTES", 7, 9, secAntCalcA));
        secAntCalcA.getSesiones().add(new Sesion("JUEVES", 7, 9, secAntCalcA));
        seccionRepo.save(secAntCalcA);

        // Algebra lineal
        Asignatura asigAntAlg = new Asignatura("ANT-202", "Algebra lineal", 2, "Ciencias Basicas", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntAlg);
        Seccion secAntAlgA = new Seccion("SEC-ANT-ALG-A", "Docente Asignado 2", "SALON-402", 39, 44, asigAntAlg);
        secAntAlgA.getSesiones().add(new Sesion("LUNES", 9, 11, secAntAlgA));
        secAntAlgA.getSesiones().add(new Sesion("MIERCOLES", 9, 11, secAntAlgA));
        seccionRepo.save(secAntAlgA);

        // Fisica I (Mecanica)
        Asignatura asigAntFis1 = new Asignatura("ANT-203", "Fisica I (Mecanica)", 2, "Ciencias Basicas", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntFis1);
        Seccion secAntFis1A = new Seccion("SEC-ANT-FIS1-A", "Docente Asignado 3", "LAB-FIS-1", 33, 44, asigAntFis1);
        secAntFis1A.getSesiones().add(new Sesion("VIERNES", 7, 9, secAntFis1A));
        seccionRepo.save(secAntFis1A);

        // Logica difusa
        Asignatura asigAntDif = new Asignatura("ANT-204", "Logica difusa", 2, "Disciplinar", 2, 2, 4, false, true, planAnterior);
        asignaturaRepo.save(asigAntDif);
        Seccion secAntDifA = new Seccion("SEC-ANT-DIF-A", "Docente Asignado 4", "SALON-403", 29, 44, asigAntDif);
        secAntDifA.getSesiones().add(new Sesion("MIERCOLES", 11, 13, secAntDifA));
        seccionRepo.save(secAntDifA);

        // Programacion I
        Asignatura asigAntProg1 = new Asignatura("ANT-205", "Programacion I", 2, "Disciplinar", 4, 4, 8, false, true, planAnterior);
        asignaturaRepo.save(asigAntProg1);
        Seccion secAntProg1A = new Seccion("SEC-ANT-PROG1-A", "Docente Asignado 5", "LAB-SW-2", 40, 44, asigAntProg1);
        secAntProg1A.getSesiones().add(new Sesion("LUNES", 14, 16, secAntProg1A));
        secAntProg1A.getSesiones().add(new Sesion("MIERCOLES", 14, 16, secAntProg1A));
        seccionRepo.save(secAntProg1A);

        // Seminario de bioetica
        Asignatura asigAntBio = new Asignatura("ANT-206", "Seminario de bioetica", 2, "Institucional", 1, 1, 2, false, true, planAnterior);
        asignaturaRepo.save(asigAntBio);
        Seccion secAntBioA = new Seccion("SEC-ANT-BIO-A", "Docente Asignado 6", "SALON-404", 32, 44, asigAntBio);
        secAntBioA.getSesiones().add(new Sesion("JUEVES", 11, 13, secAntBioA));
        seccionRepo.save(secAntBioA);

        // Est del pensamiento II
        Asignatura asigAntPens2 = new Asignatura("ANT-207", "Est. del pensamiento II. Argumentacion y procesos algoritmicos", 2, "Sociohumanistica", 1, 1, 2, false, true, planAnterior);
        asignaturaRepo.save(asigAntPens2);
        Seccion secAntPens2A = new Seccion("SEC-ANT-PENS2-A", "Docente Asignado 7", "SALON-405", 35, 44, asigAntPens2);
        secAntPens2A.getSesiones().add(new Sesion("SABADO", 9, 11, secAntPens2A));
        seccionRepo.save(secAntPens2A);

        // Plan Anterior - Semestre 3
        // Calculo integral y ecuaciones diferenciales
        Asignatura asigAntCalci = new Asignatura("ANT-301", "Calculo integral y ecuaciones diferenciales", 3, "Ciencias Basicas", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntCalci);
        Seccion secAntCalciA = new Seccion("SEC-ANT-CALCI-A", "Docente Asignado 8", "SALON-406", 36, 44, asigAntCalci);
        secAntCalciA.getSesiones().add(new Sesion("MARTES", 9, 11, secAntCalciA));
        secAntCalciA.getSesiones().add(new Sesion("JUEVES", 9, 11, secAntCalciA));
        seccionRepo.save(secAntCalciA);

        // Estadistica y probabilidad
        Asignatura asigAntEst = new Asignatura("ANT-302", "Estadistica y probabilidad", 3, "Ciencias Basicas", 2, 2, 4, false, true, planAnterior);
        asignaturaRepo.save(asigAntEst);
        Seccion secAntEstA = new Seccion("SEC-ANT-EST-A", "Docente Asignado 9", "SALON-407", 33, 44, asigAntEst);
        secAntEstA.getSesiones().add(new Sesion("LUNES", 11, 13, secAntEstA));
        seccionRepo.save(secAntEstA);

        // Fisica II 
        Asignatura asigAntFis2 = new Asignatura("ANT-303", "Fisica II (Electricidad)", 3, "Ciencias Basicas", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntFis2);
        Seccion secAntFis2A = new Seccion("SEC-ANT-FIS2-A", "Docente Asignado 10", "LAB-FIS-2", 34, 44, asigAntFis2);
        secAntFis2A.getSesiones().add(new Sesion("MIERCOLES", 11, 13, secAntFis2A));
        seccionRepo.save(secAntFis2A);

        // Programacion II
        Asignatura asigAntProg2 = new Asignatura("ANT-304", "Programacion II", 3, "Disciplinar", 4, 4, 8, false, true, planAnterior);
        asignaturaRepo.save(asigAntProg2);
        Seccion secAntProg2A = new Seccion("SEC-ANT-PROG2-A", "Docente Asignado 11", "LAB-SW-3", 41, 44, asigAntProg2);
        secAntProg2A.getSesiones().add(new Sesion("MARTES", 14, 16, secAntProg2A));
        secAntProg2A.getSesiones().add(new Sesion("JUEVES", 14, 16, secAntProg2A));
        seccionRepo.save(secAntProg2A);

        // Estructuras de datos
        Asignatura asigAntEd = new Asignatura("ANT-305", "Estructuras de datos", 3, "Disciplinar", 3, 3, 6, false, true, planAnterior);
        asignaturaRepo.save(asigAntEd);
        Seccion secAntEdA = new Seccion("SEC-ANT-ED-A", "Docente Asignado 12", "LAB-INFO-2", 38, 44, asigAntEd);
        secAntEdA.getSesiones().add(new Sesion("VIERNES", 9, 11, secAntEdA));
        seccionRepo.save(secAntEdA);

        // Proyecto nucleo I
        Asignatura asigAntPn1 = new Asignatura("ANT-306", "Proyecto nucleo I", 3, "Integracion", 1, 1, 2, false, true, planAnterior);
        asignaturaRepo.save(asigAntPn1);
        Seccion secAntPn1A = new Seccion("SEC-ANT-PN1-A", "Docente Asignado 1", "SALON-408", 30, 44, asigAntPn1);
        secAntPn1A.getSesiones().add(new Sesion("LUNES", 16, 18, secAntPn1A));
        seccionRepo.save(secAntPn1A);

        // Est. del pensamiento III
        Asignatura asigAntPens3 = new Asignatura("ANT-307", "Est. del pensamiento III. Descripcion y analisis de procesos multicausales", 3, "Sociohumanistica", 1, 1, 2, false, true, planAnterior);
        asignaturaRepo.save(asigAntPens3);
        Seccion secAntPens3A = new Seccion("SEC-ANT-PENS3-A", "Docente Asignado 2", "SALON-409", 28, 44, asigAntPens3);
        secAntPens3A.getSesiones().add(new Sesion("MIERCOLES", 16, 18, secAntPens3A));
        seccionRepo.save(secAntPens3A);
    }
}
