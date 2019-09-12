package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.persistencia.interfaz.IPersistenciaUtilidadesBD;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaUtilidadesBD implements IPersistenciaUtilidadesBD {

    @Override
    public byte[] encriptar(EntityManager eManager, String valor) {
        try {
            eManager.joinTransaction();
            String sqlQuery = "SELECT GENERALES_PKG.ENCRYPT( ? ) FROM DUAL ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, valor);
            return (byte[]) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUtilidadesBD.encriptar: " + e);
            return null;
        }
    }

    @Override
    public String desencriptar(EntityManager eManager, byte[] valor) {
        System.out.println(this.getClass().getName() + ".desencriptar()");
//        System.out.print("valor ");
//        System.out.println(valor);
        String resultado = "";
        try {
            eManager.joinTransaction();
            String sqlQuery = "SELECT GENERALES_PKG.DECRYPT( ? ) FROM DUAL ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, valor);
            resultado = (String) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUtilidadesBD.desencriptar: " + e);
            resultado = null;
        }
        System.out.println("Resultado: " + resultado);
        return resultado;
    }

    @Override
    public BigDecimal totalEmpleadosEvaluador(EntityManager em, BigInteger secuenciaEvaluador) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT EVALPREGUNTAS_PKG.TotalEmpleadosEvaluador(?) FROM DUAL");
            q.setParameter(1, secuenciaEvaluador);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerEvaluados: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public BigDecimal cantidadEvaluadosConvocatoria(EntityManager em, BigInteger secConvocatoria) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT EVALPREGUNTAS_PKG.TotalEmpleadosConvocatoria(?) FROM DUAL");
            q.setParameter(1, secConvocatoria);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerEvaluados: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public BigDecimal totalEmpleadosEvaluadorConvocatoria(EntityManager em, BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT EVALPREGUNTAS_PKG.TotalEmpleadosEvaluador(?, ?) FROM DUAL");
            q.setParameter(1, secuenciaEvaluador);
            q.setParameter(2, secConvocatoria);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerEvaluados: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public BigDecimal cantidadEvaluados(EntityManager em, BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT evalpreguntas_pkg.cantidadevaluados(?, ?) FROM DUAL");
            q.setParameter(1, secConvocatoria);
            q.setParameter(2, secuenciaEvaluador);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaConvocatorias.obtenerEvaluados: " + ex);
//            terminarTransaccionException(em);
            return null;
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
