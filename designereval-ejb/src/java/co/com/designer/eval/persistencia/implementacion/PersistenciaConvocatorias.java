package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConvocatorias;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author luistrivino
 */
@Stateless
public class PersistenciaConvocatorias implements IPersistenciaConvocatorias {

    @Override
    public List<Convocatorias> obtenerConvocatorias(EntityManager em, String usuario) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT C.SECUENCIA, "
                    + "(SELECT FECHAVIGENCIA FROM EVALVIGCONVOCATORIAS WHERE SECUENCIA = C.EVALVIGCONVOCATORIA) EVALVIGCONVOCATORIA, "
                    + "C.ESTADO, C.CODIGO, "
                    + "(SELECT DESCRIPCION FROM EVALENFOQUES WHERE SECUENCIA = C.ENFOQUE) ENFOQUE, "
                    + "C.AGRUPADO, C.FECHAINICIO, C.OBJETIVOS "
                    + "FROM EVALCONVOCATORIAS C "
                    + "WHERE C.ENFOQUE=(SELECT SECUENCIA FROM EVALENFOQUES WHERE CODIGO=1) "
                    + "AND C.ESTADO = 'PROCESAR' "
                    + "AND ( EXISTS ( SELECT 1 "
                    + " FROM EVALINDAGACIONES I, EVALRESULTADOSCONV R "
                    + " WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    + "  FROM USUARIOS U, PERSONAS P "
                    + "  WHERE U.persona = P.secuencia "
                    + "  AND U.ALIAS = ? ) "
                    + " AND I.evalresultadoconv = R.SECUENCIA "
                    + " AND R.evalconvocatoria = C.secuencia "
                    + " )) "
                    + "ORDER BY EVALVIGCONVOCATORIA DESC", Convocatorias.class);
            q.setParameter(1, usuario);
            List<Convocatorias> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerConvocatorias: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public List<Convocatorias> obtenerConvocatoriasAlcance(EntityManager em, String usuario) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT C.SECUENCIA, "
                    + "(SELECT FECHAVIGENCIA FROM EVALVIGCONVOCATORIAS WHERE SECUENCIA = C.EVALVIGCONVOCATORIA) EVALVIGCONVOCATORIA, "
                    + "C.ESTADO, C.CODIGO, "
                    + "(SELECT DESCRIPCION FROM EVALENFOQUES WHERE SECUENCIA = C.ENFOQUE) ENFOQUE, "
                    + "C.AGRUPADO, C.FECHAINICIO, C.OBJETIVOS "
                    + "FROM EVALCONVOCATORIAS C "
                    + "WHERE C.ENFOQUE=(SELECT SECUENCIA FROM EVALENFOQUES WHERE CODIGO=1) "
                    + "AND C.ESTADO = 'ALCANCE' "
                    + "AND ( EXISTS ( SELECT 1 "
                    + "               FROM EVALINDAGACIONES I, EVALRESULTADOSCONV R "
                    + "               WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    + "                             FROM USUARIOS U, PERSONAS P "
                    + "                             WHERE U.persona = P.secuencia "
                    + "                             AND U.ALIAS = ?) "
                    + "               AND I.evalresultadoconv = R.SECUENCIA "
                    + "               AND R.evalconvocatoria = C.secuencia "
                    + "             )) "
                    + "order by EVALVIGCONVOCATORIA DESC ", Convocatorias.class);
            q.setParameter(1, usuario);
            List<Convocatorias> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerConvocatoriasAlcance: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(EntityManager em, String usuario) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT PERSONA FROM USUARIOS WHERE ALIAS = ? ");
            q.setParameter(1, usuario);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerSecuenciaEvaluador: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public boolean cerrarEvaluaciones(EntityManager em, BigDecimal secConvocatoria) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() ENTRO");
            Query q = em.createNativeQuery("CALL EVALCONVOCATORIAS_PKG.CERRAREVALUACIONES(?) ");
            q.setParameter(1, secConvocatoria);
            q.executeUpdate();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA EJECUTO EVALCONVOCATORIAS_PKG.CERRAREVALUACION(" + secConvocatoria + ")");
//            em.getTransaction().commit();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA COMMIT");
            return true;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.cerrarEvaluaciones: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    @Override
    public String cerrarConvocatoria(EntityManager em, BigDecimal secConvocatoria) {
        String resultado = null;
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() ENTRO");
            StoredProcedureQuery q = em.createStoredProcedureQuery("EVALCONVOCATORIAS_PKG.CERRARCONVOCATORIA");
            q.registerStoredProcedureParameter(1, BigDecimal.class, ParameterMode.IN);
            q.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            q.setParameter(1, secConvocatoria);
            q.setParameter(2, resultado);
//            q.executeUpdate();
            q.execute();
            q.hasMoreResults();
            resultado = (String) q.getOutputParameterValue(2);
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA EJECUTO EVALCONVOCATORIAS_PKG.CERRAREVALUACION(" + secConvocatoria + ")");
//            em.getTransaction().commit();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA COMMIT");
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.cerrarConvocatoria: " + ex);
//            terminarTransaccionException(em);
            return ex.getMessage();
        }
    }

    public void terminarTransaccionException(EntityManager em) {
        System.out.println(this.getClass().getName() + ".terminarTransaccionException");
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            System.out.println("Antes de hacer rollback");
//            em.getTransaction().rollback();
            System.out.println("Despues de hacer rollback");
        }
    }
}
