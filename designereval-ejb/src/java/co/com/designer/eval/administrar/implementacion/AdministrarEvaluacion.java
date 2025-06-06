package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarEvaluacion;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.entidades.Respuestas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvaluados;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPreguntas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaRespuestas;
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
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarEvaluacion implements IAdministrarEvaluacion, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaPreguntas persistenciaPreguntas;
    @EJB
    private IPersistenciaRespuestas persistenciaRespuestas;
    @EJB
    private IPersistenciaPruebas persistenciaPruebas;
    @EJB
    private IPersistenciaEvaluados persistenciaEvaluados;
    private transient EntityManagerFactory emf;
//    private EntityManager em;
    private String idSesion;

    @Override
    public void obtenerConexion(String idSesion) {
        try {
            this.idSesion = idSesion;
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
//            if (emf != null && emf.isOpen()) {
//                em = emf.createEntityManager();
//            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.obtenerConexion: " + e);
        }
    }

    private EntityManager obtenerConexion() {
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
//                em = emf.createEntityManager();
                return emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.obtenerConexion - 2: " + e);
        }
        return null;
    }

    @Override
    public List<Preguntas> obtenerCuestinonario(BigInteger secPrueba, BigInteger secIndagacion) {
        System.out.println("AdministrarEvaluacion.obtenerCuestionario: "
                +"secPrueba: "+secPrueba
                +"secIndagacion: "+secIndagacion);
        EntityManager em = null;
        List<Preguntas> lst = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            lst = obtenerPreguntas(secPrueba);
            if (lst != null && !lst.isEmpty()) {
                for (int i = 0; i < lst.size(); i++) {
                    System.out.println("Pregunta: "+lst.get(i).getSecuencia()+" tipo: "+lst.get(i).getTipo());
                    if (lst.get(i).getTipo().equalsIgnoreCase("HISTORICA")) {
                        lst.get(i).setRespuestas(obtenerRespuestasHistorica(lst.get(i).getSecuencia()));
                    } else {
                        lst.get(i).setRespuestas(obtenerRespuestas(lst.get(i).getSecuencia()));
                    }
                    lst.get(i).setRespuesta(persistenciaRespuestas.consultarRespuesta(em, secIndagacion, lst.get(i).getSecuencia()));
                    /*if (lst.get(i).getRespuesta() == null) {
                        lst.get(i).setNuevo(true);
                    } else {
                        lst.get(i).setNuevo(false);
                    }*/
                    lst.get(i).setNuevo(true);
                }
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.obtenerCuestinonario: " + e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return lst;
    }

    public List<Preguntas> obtenerPreguntas(BigInteger secPrueba) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaPreguntas.obtenerPreguntas(em, secPrueba);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.obtenerPreguntas: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Respuestas> obtenerRespuestas(BigInteger secPregunta) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaRespuestas.obtenerRespuestas(em, secPregunta);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.obtenerRespuestas: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Respuestas> obtenerRespuestasHistorica(BigInteger secPregunta) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaRespuestas.obtenerRespuestas(em, secPregunta, "S");
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.obtenerRespuestas-2: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public BigInteger obtenerNroPreguntas(BigInteger secPrueba) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaPreguntas.obtenerNroPreguntas(em, secPrueba);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.obtenerNroPreguntas: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean registrarRespuesta(BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaRespuestas.registrarRespuesta(em, secIndagacion, secPregunta, secRespuesta);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.registrarRespuesta: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean actualizarRespuesta(BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaRespuestas.actualizarRespuesta(em, secIndagacion, secPregunta, secRespuesta);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.actualizarRespuesta: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean registrarActualizarRespuesta(List<Preguntas> preguntas, BigInteger secIndagacion) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaRespuestas.registrarActualizarRespuesta(em, preguntas, secIndagacion);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.registrarActualizarRespuesta: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean eliminarRespuestas(BigInteger secIndagacion) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaRespuestas.eliminarRespuestas(em, secIndagacion);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.eliminarRespuestas: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean actualizarPorcentaje(BigInteger secPrueba, String observacion, double porcentaje) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaPruebas.actualizarPorcentaje(em, secPrueba, observacion, porcentaje, "C");
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.actualizarPorcentaje: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean actualizarPorcentaje(BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaEvaluados.actualizarPorcentaje(em, secConvocatoria, secEvaluado, agrupado);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.actualizarPorcentaje2: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean reiniciarEvaluacion(BigInteger secIndagacion,
            String observacion, double porcentaje,
            BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado) {
        EntityManager em = null;
        boolean res;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            res = persistenciaRespuestas.eliminarRespuestas(em, secIndagacion);
            if (res) {
                res = persistenciaPruebas.actualizarPorcentaje(em, secIndagacion, observacion, porcentaje, "A");
                if (res) {
                    res = persistenciaEvaluados.actualizarPorcentaje(em, secConvocatoria, secEvaluado, agrupado);
                }
            }
            return res;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.eliminarRespuestas: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean registrarRespuestasPuntos(List<Preguntas> preguntas, BigInteger secIndagacion,
            String observacionEvaluador, double porcentaje,
            BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado) {
        EntityManager em = null;
        boolean res = false;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            res = persistenciaRespuestas.eliminarRespuestas(em, secIndagacion);
            if (res) {
                res = persistenciaRespuestas.registrarActualizarRespuesta(em, preguntas, secIndagacion);
                if (res) {
                    res = persistenciaPruebas.actualizarPorcentaje(em, secIndagacion, observacionEvaluador, porcentaje, "C");
                    if (res) {
                        res = persistenciaEvaluados.actualizarPorcentaje(em, secConvocatoria, secEvaluado, agrupado);
                    }
                }
            }
            em.joinTransaction();
            return res;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.registrarActualizarRespuesta: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public boolean consultarEvaluacionHistorica(BigInteger secEmpleado, Date fechaCorte, BigInteger secIndagacion, Preguntas pregunta) {
        EntityManager em = null;
        boolean res;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            BigDecimal resAnterior = BigDecimal.ZERO;
            
            resAnterior = persistenciaRespuestas.consultarPuntajeEvalAnterior(em, secEmpleado, fechaCorte);
            if (resAnterior.compareTo(BigDecimal.ZERO) == 1 ) {
                res = persistenciaRespuestas.registrarActualizarRespuestaHistorica(em, pregunta, secIndagacion, resAnterior);
            } else {
                res = false;
            }
            return res;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarEvaluacion.eliminarRespuestas: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
