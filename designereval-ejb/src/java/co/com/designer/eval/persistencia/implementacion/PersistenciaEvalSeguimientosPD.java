package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.EvalSeguimientosPD;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalSeguimientosPD;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Thalia Manrique
 */
@Stateless
public class PersistenciaEvalSeguimientosPD implements IPersistenciaEvalSeguimientosPD {

    @Override
    public List<EvalSeguimientosPD> obtenerEvalSeguimientosPD(EntityManager em, BigInteger secEvalPlanDesarrollo) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT SECUENCIA, EVALPLANDESARROLLO, FECHA, COMENTARIO, PORCENTAJE FROM EVALSEGUIMIENTOSPD WHERE EVALPLANDESARROLLO=? ORDER BY FECHA", EvalSeguimientosPD.class);
            // q.setParameter(1, secEvalPlanDesarrollos);
            q.setParameter(1, secEvalPlanDesarrollo);
            /*q.setParameter(2, secConvocatoria);*/
            List<EvalSeguimientosPD> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaEvalSeguimientosPD.obtenerEvalSeguimientosPD: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }
    
    @Override
    public boolean registrarEvalSeguimientoPD(EntityManager em, BigInteger secEvalplandesarrollo,
            Date fecha, String comentario, String porcentaje) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("INSERT INTO EVALSEGUIMIENTOSPD (EVALPLANDESARROLLO, FECHA, COMENTARIO, PORCENTAJE) "
                    + " VALUES (?, ?, ?, ?) ");
            q.setParameter(1, secEvalplandesarrollo);
            q.setParameter(2, fecha, TemporalType.DATE);
            q.setParameter(3, comentario);
            q.setParameter(4, porcentaje);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaEvalSeguimientosPD.registrarEvalSeguimientoPD: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }
    
    @Override
    public boolean editarEvalSeguimientoPD(EntityManager em, BigInteger secEvalSEguimiento,
            Date fecha, String comentario, int porcentaje) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("UPDATE EVALSEGUIMIENTOSPD SET FECHA= ?, COMENTARIO=?, PORCENTAJE=? "
                    + " WHERE SECUENCIA=? ");
            q.setParameter(1, fecha, TemporalType.DATE);
            q.setParameter(2, comentario);
            q.setParameter(3, porcentaje);
            q.setParameter(4, secEvalSEguimiento);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaEvalSeguimientosPD.editarEvalSeguimientoPD: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }
    
    @Override
    public boolean eliminarBitacora(EntityManager em, BigInteger secBitacora) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("DELETE EVALSEGUIMIENTOSPD WHERE SECUENCIA = ? ");
            q.setParameter(1, secBitacora);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalSeguimientosPD.eliminarBitacora: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }
    
    @Override
    public int countBitacoras(EntityManager em, BigInteger secPlanDesarrollo) {
        int count=0;
        try {
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT count(*) FROM EVALSEGUIMIENTOSPD WHERE EVALPLANDESARROLLO=?");
            q.setParameter(1, secPlanDesarrollo);
            q.executeUpdate();

            BigDecimal retorno = (BigDecimal) q.getSingleResult();
            count = retorno.intValueExact();
//            eManager.getTransaction().commit();
     
        } catch (Exception e) {
            System.out.println(this.getClass().getName()+": "+"Error validarUsuario: " + e);
//            terminarTransaccionException(eManager);
            return 0;
        }
        return count;
    }
    
    
}
