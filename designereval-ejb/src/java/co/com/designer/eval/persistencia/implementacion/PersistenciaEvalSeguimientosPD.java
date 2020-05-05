package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import co.com.designer.eval.entidades.EvalSeguimientosPD;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalSeguimientosPD;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Thalia Manrique
 */
public class PersistenciaEvalSeguimientosPD implements IPersistenciaEvalSeguimientosPD{
    
    @Override
    public List<EvalPlanesDesarrollos> obtenerEvalSeguimientosPD(EntityManager em, BigInteger secEvalPlanDesarrollo) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createQuery("select epd from EvalSeguimientosPD esp where epd.evalplandesarrollo=:secEvalplan order by epd.codigo asc", EvalSeguimientosPD.class);
            // q.setParameter(1, secEvalPlanDesarrollos);
            q.setParameter("secEvalplan", secEvalPlanDesarrollo);
            /*q.setParameter(2, secConvocatoria);*/
            List<EvalPlanesDesarrollos> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaEvalPlanesDesarrollos.obtenerPlanesDesarrollos: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }
    
    
        @Override
    public boolean registrarEvalSeguimientoPD(EntityManager em, BigDecimal secEvalplandesarrollo,
            Date fecha, String comentario, String porcentaje) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("INSERT INTO EVALSEGUIMIENTOSPD (EVALPLANDESARROLLO, FECHA, COMENTARIO, PORCENTAJE) " +
                " VALUES (?, ?, ?, ?) ");
            q.setParameter(1, secEvalplandesarrollo);
            //q.setParameter(2, secIndagacion);
            q.setParameter(2, fecha);
            q.setParameter(4, comentario);
            q.setParameter(5, porcentaje);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalSeguimientosPD.registrarEvalSeguimientoPD: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }
}
