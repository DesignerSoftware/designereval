package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.entidades.Respuestas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaRespuestas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author luistrivino
 */
@Stateless
public class PersistenciaRespuestas implements IPersistenciaRespuestas {

    @Override
    public List<Respuestas> obtenerRespuestas(EntityManager em, BigInteger secPregunta) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT SECUENCIA, CUALITATIVO, CUANTITATIVO, DESCRIPCION "
                    + "FROM EVALRESPUESTAS RES "
                    + "WHERE RES.EVALPREGUNTAS = ? "
                    + "ORDER BY CUANTITATIVO ASC ", Respuestas.class);
            q.setParameter(1, secPregunta);
            List<Respuestas> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.obtenerRespuestas: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public List<Respuestas> obtenerRespuestas(EntityManager em, BigInteger secPregunta, String Historica) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT RES.SECUENCIA \n"
                    + ", NVL(ERI.CUALITATIVOASIGNADO, RES.CUALITATIVO) CUALITATIVO, NVL(ERI.PUNTAJEMANUAL, RES.CUANTITATIVO) CUANTITATIVO \n"
                    + ", RES.DESCRIPCION \n"
                    + "FROM EVALRESPUESTAS RES, EvalRespuestasIndagaciones ERI \n"
                    + "WHERE RES.SECUENCIA = ERI.EVALRESPUESTA(+) \n"
                    + "AND RES.EVALPREGUNTAS = ERI.EVALPREGUNTA(+) \n"
                    + "AND RES.EVALPREGUNTAS = ? \n"
                    + "ORDER BY CUANTITATIVO ASC", Respuestas.class);
            q.setParameter(1, secPregunta);
            List<Respuestas> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.obtenerRespuestas-2: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public boolean registrarRespuesta(EntityManager em, BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("INSERT INTO EVALRESPUESTASINDAGACIONES (EVALINDAGACION, EVALPREGUNTA, EVALRESPUESTA, CUALITATIVOASIGNADO, CUANTITATIVOASIGNADO ) "
                    + "VALUES ( ?, ?, ?, "
                    + "(SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?), "
                    + "(SELECT CUANTITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) "
                    + ") ");
            q.setParameter(1, secIndagacion);
            q.setParameter(2, secPregunta);
            q.setParameter(3, secRespuesta);
            q.setParameter(4, secRespuesta);
            q.setParameter(5, secRespuesta);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.registrarRespuesta: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    @Override
    public boolean actualizarRespuesta(EntityManager em, BigInteger secIndagacion,
            BigInteger secPregunta, BigInteger secRespuesta) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("UPDATE EVALRESPUESTASINDAGACIONES "
                    + "SET CUALITATIVOASIGNADO = (SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) , "
                    + "CUANTITATIVOASIGNADO = (SELECT CUANTITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) , "
                    + "EVALRESPUESTA = ? "
                    + "WHERE EVALINDAGACION = ? "
                    + "AND EVALPREGUNTA = ? ");
            q.setParameter(1, secRespuesta);
            q.setParameter(2, secRespuesta);
            q.setParameter(3, secRespuesta);
            q.setParameter(4, secIndagacion);
            q.setParameter(5, secPregunta);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.actualizarRespuesta: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    @Override
    public boolean registrarActualizarRespuesta(EntityManager em, List<Preguntas> preguntas, BigInteger secIndagacion) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            for (Preguntas pregunta : preguntas) {
                Query q = null;
                if (pregunta.isNuevo()) {
                    q = em.createNativeQuery("INSERT INTO EVALRESPUESTASINDAGACIONES (EVALINDAGACION, EVALPREGUNTA, EVALRESPUESTA, CUALITATIVOASIGNADO, CUANTITATIVOASIGNADO ) "
                            + "VALUES ( ?, ?, ?, "
                            + "(SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?), "
                            + "(SELECT CUANTITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) "
                            + ") ");
                } else {
                    q = em.createNativeQuery("UPDATE EVALRESPUESTASINDAGACIONES "
                            + "SET CUALITATIVOASIGNADO = (SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) , "
                            + "CUANTITATIVOASIGNADO = (SELECT CUANTITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) , "
                            + "EVALRESPUESTA = ? "
                            + "WHERE EVALINDAGACION = ? "
                            + "AND EVALPREGUNTA = ? ");
                }
                q.setParameter(1, secIndagacion);
                q.setParameter(2, pregunta.getSecuencia());
                q.setParameter(3, pregunta.getRespuesta());
                q.setParameter(4, pregunta.getRespuesta());
                q.setParameter(5, pregunta.getRespuesta());
                q.executeUpdate();
            }
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.registrarActualizarRespuesta: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    @Override
    public BigInteger consultarRespuesta(EntityManager em, BigInteger secIndagacion,
            BigInteger secPregunta) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT EVALRESPUESTA "
                    + "FROM EVALRESPUESTASINDAGACIONES "
                    + "WHERE EVALINDAGACION = ? "
                    + "AND EVALPREGUNTA = ? ");
            q.setParameter(1, secIndagacion);
            q.setParameter(2, secPregunta);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado.toBigInteger();
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.consultarRespuesta: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public boolean eliminarRespuestas(EntityManager em, BigInteger secIndagacion) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("DELETE EVALRESPUESTASINDAGACIONES "
                    + "WHERE EVALINDAGACION = ? ");
            q.setParameter(1, secIndagacion);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.eliminarRespuestas: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    public void terminarTransaccionException(EntityManager em) {
        System.out.println(this.getClass().getName() + ".terminarTransaccionException");
//        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
        if (em != null && em.isOpen()) {
            System.out.println(this.getClass().getName() + ": " + "Antes de hacer rollback");
//            em.getTransaction().rollback();
            em.close();
            System.out.println(this.getClass().getName() + ": " + "Despues de hacer rollback");
        }
    }

    @Override
    public BigDecimal consultarPuntajeEvalAnterior(EntityManager em, BigInteger secEmpleado, Date dtFechaCorte) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String vcFechaCorte = sdf.format(dtFechaCorte);
        try {
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT \n"
                    + "ERC.PUNTAJEOBTENIDO \n"
                    + "FROM EMPRESAS EM, EVALVIGCONVOCATORIAS EVC \n"
                    + ", EVALCONVOCATORIAS EC, EVALRESULTADOSCONV ERC \n"
                    + "WHERE EVC.EMPRESA = EM.SECUENCIA \n"
                    + "AND EC.EVALVIGCONVOCATORIA = EVC.SECUENCIA \n"
                    + "AND ERC.EVALCONVOCATORIA = EC.SECUENCIA \n"
                    + "AND EVC.FECHAVIGENCIA = ( \n"
                    + " SELECT MAX(EVCI.FECHAVIGENCIA) \n"
                    + " FROM EVALVIGCONVOCATORIAS EVCI, EVALCONVOCATORIAS ECI \n"
                    + " , EVALRESULTADOSCONV ERCI \n"
                    + " WHERE EVC.EMPRESA = EVCI.EMPRESA \n"
                    + " AND ECI.EVALVIGCONVOCATORIA = EVCI.SECUENCIA \n"
                    + " AND ERCI.EVALCONVOCATORIA = ECI.SECUENCIA \n"
                    + " AND ERCI.EMPLEADO = ERC.EMPLEADO \n"
                    + " AND ECI.ESTADO =EC.ESTADO \n"
                    + " AND EVCI.FECHAVIGENCIA BETWEEN TRUNC(TO_DATE('?', 'DDMMYYYY'), 'MM') AND LAST_DAY(TO_DATE('?', 'DDMMYYYY'))) \n"
//                    + "AND EC.ESTADO IN ('ALCANCE') \n"
                    + "AND EC.ESTADO IN ('PROCESAR') \n"
                    + "AND ERC.EMPLEADO = ? ");
            q.setParameter(1, vcFechaCorte);
            q.setParameter(2, vcFechaCorte);
            q.setParameter(3, secEmpleado);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaRespuestas.consultarPuntajeEvalAnterior: " + ex);
            return null;
        }
    }

    @Override
    public boolean registrarActualizarRespuestaHistorica(EntityManager em, Preguntas pregunta, BigInteger secIndagacion, BigDecimal resAnterior) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
//            for (Preguntas pregunta : preguntas) {
            Query q = null;
            if (pregunta.isNuevo()) {
                q = em.createNativeQuery("INSERT INTO EVALRESPUESTASINDAGACIONES (EVALINDAGACION, EVALPREGUNTA, EVALRESPUESTA, CUALITATIVOASIGNADO, CUANTITATIVOASIGNADO, PUNTAJEMANUAL ) "
                        + "VALUES ( ?, ?, ?, "
                        + "(SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?), "
                        + "? ,"
                        + "? "
                        + ") ");
                q.setParameter(1, secIndagacion);
                q.setParameter(2, pregunta.getSecuencia());
                q.setParameter(3, pregunta.getRespuesta());
                q.setParameter(4, pregunta.getRespuesta());
                q.setParameter(5, resAnterior);
                q.setParameter(6, resAnterior);
            } else {
                q = em.createNativeQuery("UPDATE EVALRESPUESTASINDAGACIONES "
                        + "SET CUALITATIVOASIGNADO = (SELECT CUALITATIVO FROM EVALRESPUESTAS WHERE SECUENCIA = ?) , "
                        + "CUANTITATIVOASIGNADO = ? , "
                        + "PUNTAJEMANUAL = ? "
                        + "EVALRESPUESTA = ? "
                        + "WHERE EVALINDAGACION = ? "
                        + "AND EVALPREGUNTA = ? ");
                q.setParameter(1, pregunta.getRespuesta());
                q.setParameter(2, resAnterior);
                q.setParameter(3, resAnterior);
                q.setParameter(4, pregunta.getRespuesta());
                q.setParameter(5, secIndagacion);
                q.setParameter(6, pregunta.getSecuencia());
            }

            q.executeUpdate();
//            }
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaRespuestas.registrarActualizarRespuesta: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }
}
