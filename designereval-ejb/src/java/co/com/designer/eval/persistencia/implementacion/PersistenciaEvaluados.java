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
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT R.SECUENCIA, R.EMPLEADO, CONCAT(CONCAT(CONCAT(CONCAT(PE.NOMBRE,' '),PE.PRIMERAPELLIDO),' '),PE.SEGUNDOAPELLIDO) NOMBREPERSONA, R.PUNTAJEOBTENIDO, "
                    + "PE.EMAIL, "
                    + "R.FECHAPERIODODESDE, R.FECHAPERIODOHASTA, R.EVALCONVOCATORIA, "
                    + "R.NOMBREPRUEBA, R.ESTADOEVAL, "
                    //                    + "EVALCONVOCATORIAS_PKG.ESTACONSOLIDADO(R.EVALCONVOCATORIA,R.EMPLEADO) CONSOLIDADO "
                    + "EVALCONVOCATORIAS_PKG.ESTACONSOLIDADO(R.EVALCONVOCATORIA, R.EMPLEADO , ? ) CONSOLIDADO "
                    + "FROM EVALRESULTADOSCONV R, EMPLEADOS E, PERSONAS PE "
                    + "WHERE (EXISTS(SELECT 'X' "
                    + "              FROM EVALINDAGACIONES I "
                    + "			  WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    //                    + "                     FROM USUARIOS U, PERSONAS P "
                    + "                     FROM CONEXIONESEVAL U, PERSONAS P "
                    + "                     WHERE U.persona=P.secuencia "
                    //                    + "                     AND U.ALIAS=?) "
                    + "                     AND U.SEUDONIMO = ? ) "
                    + "              AND R.SECUENCIA=I.evalresultadoconv) "
                    + "      ) "
                    + "AND R.EVALCONVOCATORIA = ? "
                    + "AND R.EMPLEADO = E.SECUENCIA "
                    + "AND E.PERSONA = PE.SECUENCIA ", Evaluados.class);
            q.setParameter(1, usuario);
            q.setParameter(2, usuario);
            q.setParameter(3, secConvocatoria);
            List<Evaluados> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaConvocatorias.obtenerEvaluados: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public boolean actualizarPorcentaje(EntityManager em, BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado) {
        try {
//            em.getTransaction().begin();
//            em.joinTransaction();
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
                /*q = em.createNativeQuery("SELECT sum(nvl(a.puntoobtenido,0)*b.puntos)/100/? "
                        + "FROM evalindagaciones a, evalpruebas b "
                        + "WHERE a.evalprueba = b.secuencia "
                        + "AND a.evalresultadoconv = ? ");*/
                q = em.createNativeQuery("select sum(t.puntos) "
                        + " from ( "
                        + " select c.descripcion, sum(nvl(a.puntoobtenido,0)*b.puntos)/100*c.pesocompetencia/100 puntos "
                        + " from evalindagaciones a, evalpruebas b, evalplanillas c "
                        + " where a.evalprueba = b.secuencia "
                        + " and c.secuencia = b.planilla "
                        + " and a.evalresultadoconv = ? "
                        + " group by c.descripcion, c.pesocompetencia "
                        + " ) T ");
                /*q = em.createNativeQuery("select sum(te.x) puntaje from ( \n"
                        + "select \n"
                        + "t.descripcion, t.puntosevlr, t.puntosevlr*t.puntosm/100 x,  t.pesocompetencia \n"
                        + "from ( \n"
                        + "select epl.secuencia, \n"
                        + "epl.descripcion, \n"
                        + "sum(ei.puntoobtenido*(select ti.pesoevaluador from ( \n"
                        + "select \n"
                        + "epri.convocatoria, epri.planilla, \n"
                        + "epri.puntos/count( distinct epri.evalevaluador) pesoevaluador \n"
                        + "from evalpruebas epri \n"
                        + "group by epri.convocatoria, epri.planilla, epri.puntos \n"
                        + ") Ti where ti.planilla = epl.secuencia) \n"
                        + ")/100 puntosevlr, \n"
                        + "(select epli.pesocompetencia/count(distinct epli.descripcion) \n"
                        + "from evalplanillas epli, evalpruebas epri, evalindagaciones eii \n"
                        + "where epri.planilla = epli.secuencia \n"
                        + "and eii.evalprueba = epri.secuencia \n"
                        + "and epli.pesocompetencia = epl.pesocompetencia \n"
                        + "and eii.evalresultadoconv = ei.evalresultadoconv \n"
                        + "group by epli.pesocompetencia, epli.empresa, epli.evalenfoque, epli.fechacreacion, eii.evalresultadoconv) \n"
                        + "puntosm, \n"
                        + "epl.pesocompetencia \n"
                        + " from evalindagaciones ei, evalpruebas epr, evalplanillas epl \n"
                        + " where ei.evalprueba = epr.secuencia \n"
                        + " and epl.secuencia = epr.planilla \n"
                        + " and ei.evalresultadoconv = ? \n"
                        + " group by epl.secuencia, epl.descripcion, epl.pesocompetencia, ei.evalresultadoconv \n"
                        + " ) T \n"
                        + ") te ");*/
//                q.setParameter(1, total);
//                q.setParameter(2, total);
                q.setParameter(1, secEvaluado);
                BigDecimal porcentaje = (BigDecimal) q.getSingleResult();
                if (porcentaje != null) {
                    q = em.createNativeQuery("UPDATE EVALRESULTADOSCONV A SET A.PUNTAJEOBTENIDO = ? WHERE A.SECUENCIA = ? ");
                    q.setParameter(1, porcentaje);
                    q.setParameter(2, secEvaluado);
                    q.executeUpdate();
                }
//                em.getTransaction().commit();
            }
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaEvaluados.actualizarPorcentaje: " + ex);
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
}
