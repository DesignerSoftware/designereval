package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPreguntas;
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
public class PersistenciaPreguntas implements IPersistenciaPreguntas {

    @Override
    public List<Preguntas> obtenerPreguntas(EntityManager em, BigInteger secPrueba) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT SECUENCIA, CONSECUTIVO, DESCRIPCION, OBSERVACIONES "
                    + "FROM EVALPREGUNTAS "
                    + "WHERE EVALPLANILLA=(SELECT PL.SECUENCIA "
                    + "       FROM EVALPRUEBAS PR, EVALPLANILLAS PL "
                    + "	   WHERE PR.PLANILLA = PL.SECUENCIA "
                    + "	   AND PR.SECUENCIA = ? "
                    + "       ) "
                    + "ORDER BY CONSECUTIVO ASC", Preguntas.class);
            q.setParameter(1, secPrueba);
            List<Preguntas> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println("Error PersistenciaPreguntas.obtenerPreguntas: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public BigInteger obtenerNroPreguntas(EntityManager em, BigInteger secPrueba) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT PL.NUMEROPREGUNTA "
                    + "FROM EVALPRUEBAS PR, EVALPLANILLAS PL "
                    + "WHERE PR.PLANILLA = PL.SECUENCIA "
                    + "AND PR.SECUENCIA = ? ");
            q.setParameter(1, secPrueba);
            BigDecimal resultado = (BigDecimal) q.getSingleResult();
//            em.getTransaction().commit();
            return resultado.toBigInteger();
        } catch (Exception ex) {
            System.out.println("Error PersistenciaPreguntas.obtenerPreguntas: " + ex);
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
