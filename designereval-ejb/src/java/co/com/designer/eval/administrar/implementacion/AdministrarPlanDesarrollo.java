package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarPlanDesarrollo;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Cursos;
import co.com.designer.eval.entidades.EvalActividades;
import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import co.com.designer.eval.entidades.EvalSeguimientosPD;
import co.com.designer.eval.entidades.Profesiones;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConvocatorias;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaCursos;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalActividades;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalPlanesDesarrollos;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalSeguimientosPD;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaProfesiones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Thalia Manrique
 */
@Stateful
public class AdministrarPlanDesarrollo implements IAdministrarPlanDesarrollo, Serializable {

    private transient EntityManagerFactory emf;
    private String idSesion;
    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEvalActividades persistenciaEvalActividades;
    @EJB
    private IPersistenciaCursos persistenciaCursos;
    @EJB
    private IPersistenciaProfesiones persistenciaProfesiones;
    @EJB
    private IPersistenciaEvalPlanesDesarrollos persistenciaEvalPlanesDesarrollos;
    @EJB
    private IPersistenciaEvalSeguimientosPD persistenciabitacoras;
    @EJB
    private IPersistenciaConvocatorias persistenciaConvocatorias;

    @Override
    public void obtenerConexion(String idSesion) {
        try {
            this.idSesion = idSesion;
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerConexion: " + e);
        }
    }

    private EntityManager obtenerConexion() {
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                return emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerConexion - 2: " + e);
        }
        return null;
    }

    @Override
    public List<EvalActividades> obtenerActividades() {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEvalActividades.obtenerActividades(em);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerActividades: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<EvalActividades> obtenerActividadesEmpl(BigInteger secEmpleado, BigInteger secConvocatoria) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEvalActividades.obtenerActividadesEmpl(em, secEmpleado, secConvocatoria);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerActividadesEmpl: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Cursos> obtenerCursos() {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaCursos.obtenerCursos(em);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerCursos: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Profesiones> obtenerProfesiones() {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaProfesiones.obtenerProfesiones(em);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerProfesiones: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<EvalPlanesDesarrollos> obtenerPlanesDesarrollos(BigInteger secEvalResultadoConv) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEvalPlanesDesarrollos.obtenerPlanesDesarrollos(em, secEvalResultadoConv);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerPlanesDesarrollos: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean eliminarPlanDesarrollo(BigInteger secPlanDesarrollo) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEvalPlanesDesarrollos.eliminarPlanDesarrollo(em, secPlanDesarrollo);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.eliminarPlanDesarrollo: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean registrarPlanDesarrollo(BigDecimal secCodigo, //BigInteger secIndagacion,
            BigInteger secEvalResultado, String secEvalActividad, String observacion, String secCurso, String secProfesion) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            //return persistenciaEvalPlanesDesarrollos.registrarPlanDesarrollo(em, secCodigo, secIndagacion, secEvalResultado, secEvalActividad, secCurso);
//            return persistenciaEvalPlanesDesarrollos.registrarPlanDesarrollo(em, secCodigo, secEvalResultado, secEvalActividad, observacion, secCurso);
            return persistenciaEvalPlanesDesarrollos.registrarPlanDesarrollo(em, secCodigo, secEvalResultado, secEvalActividad, observacion, secCurso, secProfesion);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.registrarPlanDesarrollo: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public BigDecimal obtenerUltimoCodigo(BigInteger secEvalResultadoConv) { // Consulta el maximo código  a las actividades del plan de desarrollo de una convocatoria y usuario especifico
        EntityManager em = null;
        BigDecimal res = new BigDecimal(0);
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEvalPlanesDesarrollos.obtenerUltimoCodigo(em, secEvalResultadoConv);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerUltimoCodigo: " + e);
            return res;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public EvalActividades consultarEvalActividad(BigDecimal secuencia) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEvalActividades.consultarEvalActividad(em, secuencia);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.consultarEvalActividad: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Convocatorias> obtenerConvocatorias(String usuario) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaConvocatorias.obtenerConvocatorias(em, usuario);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerConvocatorias: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
       @Override
    public boolean registrarBitacora(BigInteger secPlanDesarrollo, 
             Date fecha, String comentario, String porcentaje) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            //return persistenciaEvalPlanesDesarrollos.registrarPlanDesarrollo(em, secCodigo, secIndagacion, secEvalResultado, secEvalActividad, secCurso);
//            return persistenciaEvalPlanesDesarrollos.registrarPlanDesarrollo(em, secCodigo, secEvalResultado, secEvalActividad, observacion, secCurso);
            return persistenciabitacoras.registrarEvalSeguimientoPD(em, secPlanDesarrollo, fecha, comentario, porcentaje);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlan.registrarBitacora: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public List<EvalSeguimientosPD> obtenerBitacoras(BigInteger secPlanDesarrollo) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciabitacoras.obtenerEvalSeguimientosPD(em, secPlanDesarrollo);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.obtenerBitacoras: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public boolean eliminarBitacora(BigInteger secBitacora) {
            System.out.println("Parametro secBitacora: "+secBitacora);
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciabitacoras.eliminarBitacora(em, secBitacora);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarPlanDesarrollo.eliminarBitacora: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
