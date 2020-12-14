package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConvocatorias;
import java.math.BigDecimal;
import java.util.Date;
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
    public List<Date> obtenerVigenciasConvocatorias(EntityManager em, String usuario) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT EVC.FECHAVIGENCIA \n"
                    + "FROM EVALVIGCONVOCATORIAS EVC \n"
                    + "WHERE EXISTS ( \n"
                    + "SELECT EC.SECUENCIA \n"
                    + "FROM CONEXIONESEVAL U, EVALINDAGACIONES EI, EVALRESULTADOSCONV ERC, EVALCONVOCATORIAS EC \n"
                    + "WHERE U.SEUDONIMO = ? \n"
                    + "AND EI.EMPLEADOEVALUADOR = U.PERSONA \n"
                    + "AND ERC.SECUENCIA = EI.EVALRESULTADOCONV \n"
                    + "AND EC.SECUENCIA = ERC.EVALCONVOCATORIA \n"
                    + "AND EC.EVALVIGCONVOCATORIA = EVC.SECUENCIA \n"
                    + ")");
            q.setParameter(1, usuario);
            List<Date> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaConvocatorias.obtenerConvocatorias: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public List<Convocatorias> obtenerConvocatorias(EntityManager em, String usuario) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT C.SECUENCIA, "
                    + "E.FECHAVIGENCIA EVALVIGCONVOCATORIA, "
                    + "C.ESTADO, C.CODIGO, "
                    + "(SELECT DESCRIPCION FROM EVALENFOQUES WHERE SECUENCIA = C.ENFOQUE) ENFOQUE, "
                    + "C.AGRUPADO, C.FECHAINICIO, C.OBJETIVOS "
                    + "FROM EVALCONVOCATORIAS C, EVALVIGCONVOCATORIAS E "
                    + "WHERE E.SECUENCIA = C.EVALVIGCONVOCATORIA "
                    + "AND C.ENFOQUE=(SELECT SECUENCIA FROM EVALENFOQUES WHERE CODIGO=1) "
                    + "AND C.ESTADO = 'PROCESAR' "
                    + "AND ( EXISTS ( SELECT 1 "
                    + " FROM EVALINDAGACIONES I, EVALRESULTADOSCONV R "
                    + " WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    //                    + "  FROM USUARIOS U, PERSONAS P "
                    + "  FROM CONEXIONESEVAL U, PERSONAS P "
                    + "  WHERE U.persona = P.secuencia "
                    //                    + "  AND U.ALIAS = ? ) "
                    + "  AND U.SEUDONIMO = ? ) "
                    + " AND I.evalresultadoconv = R.SECUENCIA "
                    + " AND R.evalconvocatoria = C.secuencia "
                    + " )) "
                    + "ORDER BY EVALVIGCONVOCATORIA DESC", Convocatorias.class);
            q.setParameter(1, usuario);
            List<Convocatorias> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaConvocatorias.obtenerConvocatorias: " + ex);
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
                    + "E.FECHAVIGENCIA EVALVIGCONVOCATORIA, "
                    + "C.ESTADO, C.CODIGO, "
                    + "(SELECT DESCRIPCION FROM EVALENFOQUES WHERE SECUENCIA = C.ENFOQUE) ENFOQUE, "
                    + "C.AGRUPADO, C.FECHAINICIO, C.OBJETIVOS "
                    + "FROM EVALCONVOCATORIAS C, EVALVIGCONVOCATORIAS E "
                    + "WHERE E.SECUENCIA = C.EVALVIGCONVOCATORIA "
                    + "AND C.ENFOQUE=(SELECT SECUENCIA FROM EVALENFOQUES WHERE CODIGO=1) "
                    + "AND C.ESTADO = 'ALCANCE' "
                    + "AND ( EXISTS ( SELECT 1 "
                    + "               FROM EVALINDAGACIONES I, EVALRESULTADOSCONV R "
                    + "               WHERE I.EMPLEADOEVALUADOR=(SELECT P.SECUENCIA "
                    //                    + "                             FROM USUARIOS U, PERSONAS P "
                    + "                             FROM CONEXIONESEVAL U, PERSONAS P "
                    + "                             WHERE U.persona = P.secuencia "
                    //                    + "                             AND U.ALIAS = ?) "
                    + "                             AND U.SEUDONIMO = ?) "
                    + "               AND I.evalresultadoconv = R.SECUENCIA "
                    + "               AND R.evalconvocatoria = C.secuencia "
                    + "             )) "
                    + "order by EVALVIGCONVOCATORIA DESC ", Convocatorias.class);
            q.setParameter(1, usuario);
            List<Convocatorias> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaConvocatorias.obtenerConvocatoriasAlcance: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(EntityManager em, String usuario) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
//            Query q = em.createNativeQuery("SELECT PERSONA FROM USUARIOS WHERE ALIAS = ? ");
            Query q = em.createNativeQuery("SELECT PERSONA FROM CONEXIONESEVAL WHERE SEUDONIMO = ? ");
            q.setParameter(1, usuario);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaConvocatorias.obtenerSecuenciaEvaluador: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public boolean cerrarEvaluaciones(EntityManager em, BigDecimal secConvocatoria, String usuario) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() ENTRO");
//            Query q = em.createNativeQuery("CALL EVALCONVOCATORIAS_PKG.CERRAREVALUACIONES(?) ");
            Query q = em.createNativeQuery("CALL EVALCONVOCATORIAS_PKG.CERRAREVALUACIONES( ? , ? ) ");
            q.setParameter(1, secConvocatoria);
            q.setParameter(2, usuario);
            q.executeUpdate();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA EJECUTO EVALCONVOCATORIAS_PKG.CERRAREVALUACION(" + secConvocatoria + ")");
//            em.getTransaction().commit();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA COMMIT");
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaConvocatorias.cerrarEvaluaciones: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    @Override
    public String cerrarConvocatoria(EntityManager em, BigDecimal secConvocatoria, String usuario) {
        String resultado = null;
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() ENTRO");
            StoredProcedureQuery q = em.createStoredProcedureQuery("EVALCONVOCATORIAS_PKG.CERRARCONVOCATORIA");
            q.registerStoredProcedureParameter(1, BigDecimal.class, ParameterMode.IN);
            q.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
            q.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
            q.setParameter(1, secConvocatoria);
            q.setParameter(2, resultado);
            q.setParameter(3, usuario);
//            q.executeUpdate();
            q.execute();
            q.hasMoreResults();
            resultado = (String) q.getOutputParameterValue(2);
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA EJECUTO EVALCONVOCATORIAS_PKG.CERRAREVALUACION(" + secConvocatoria + ")");
//            em.getTransaction().commit();
            //System.out.println("co.com.designer.eval.persistencia.implementacion.PersistenciaConvocatorias.cerrarConvocatoria() YA COMMIT");
            return resultado;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaConvocatorias.cerrarConvocatoria: " + ex);
//            terminarTransaccionException(em);
            return ex.getMessage();
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
