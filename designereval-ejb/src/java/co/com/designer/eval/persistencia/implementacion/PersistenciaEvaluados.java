package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvaluados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author luistrivino
 */
@Stateless
public class PersistenciaEvaluados implements IPersistenciaEvaluados {

    @Override
    public List<Evaluados> obtenerEvaluados(EntityManager em, String usuario, BigInteger secConvocatoria) {
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT R.SECUENCIA, R.EMPLEADO, CONCAT(CONCAT(CONCAT(CONCAT(PE.NOMBRE,' '),PE.PRIMERAPELLIDO),' '),PE.SEGUNDOAPELLIDO) NOMBREPERSONA, R.PUNTAJEOBTENIDO, "
                    + "R.FECHAPERIODODESDE, R.FECHAPERIODOHASTA, R.EVALCONVOCATORIA, "
                    + "R.NOMBREPRUEBA, R.ESTADOEVAL, "
                    + "EVALCONVOCATORIAS_PKG.ESTACONSOLIDADO(R.EVALCONVOCATORIA,R.EMPLEADO) CONSOLIDADO "
                    + "FROM EVALRESULTADOSCONV R, EMPLEADOS E, PERSONAS PE "
                    + "WHERE (EXISTS(SELECT 'X' "
                    + "              FROM EVALINDAGACIONES I "
                    + "			  WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    + "                     FROM USUARIOS U, PERSONAS P "
                    + "                     WHERE U.persona=P.secuencia "
                    + "                     AND U.ALIAS=?) "
                    + "              AND R.SECUENCIA=I.evalresultadoconv) "
                    + "      ) "
                    + "AND R.EVALCONVOCATORIA = ? "
                    + "AND R.EMPLEADO = E.SECUENCIA "
                    + "AND E.PERSONA = PE.SECUENCIA ", Evaluados.class);
            q.setParameter(1, usuario);
            q.setParameter(2, secConvocatoria);
            List<Evaluados> lst = q.getResultList();
            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerEvaluados: " + ex);
            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public boolean actualizarPorcentaje(EntityManager em, BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado) {
        try {
            em.getTransaction().begin();
            Query q;
            Integer total;
            if (agrupado == 1) {
                q = em.createNativeQuery("SELECT COUNT(*) FROM EVALPRUEBAS WHERE CONVOCATORIA = ? GROUP BY PLANILLA ");
                q.setParameter(1, secConvocatoria);
                total = (Integer) q.getResultList().size();
            } else {
                q = em.createNativeQuery("SELECT COUNT(*) FROM EVALPRUEBAS WHERE CONVOCATORIA = ? ");
                q.setParameter(1, secConvocatoria);
                total = ((BigDecimal) q.getSingleResult()).intValue();
            }

            if (total != null && total != 0) {
                q = em.createNativeQuery("SELECT sum(nvl(a.puntoobtenido,0)*b.puntos)/100/? "
                        + "FROM evalindagaciones a, evalpruebas b "
                        + "WHERE a.evalprueba = b.secuencia "
                        + "AND a.evalresultadoconv = ? ");
                q.setParameter(1, total);
                q.setParameter(2, secEvaluado);
                BigDecimal porcentaje = (BigDecimal) q.getSingleResult();
                if (porcentaje != null) {
                    q = em.createNativeQuery("UPDATE EVALRESULTADOSCONV A SET A.PUNTAJEOBTENIDO = ? WHERE A.SECUENCIA = ? ");
                    q.setParameter(1, porcentaje);
                    q.setParameter(2, secEvaluado);
                    q.executeUpdate();
                }
                em.getTransaction().commit();
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaEvaluados.actualizarPorcentaje: " + ex);
            terminarTransaccionException(em);
            return false;
        }
    }

    public void terminarTransaccionException(EntityManager em) {
        System.out.println(this.getClass().getName() + ".terminarTransaccionException");
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            System.out.println("Antes de hacer rollback");
            em.getTransaction().rollback();
            System.out.println("Despues de hacer rollback");
        }
    }
}
