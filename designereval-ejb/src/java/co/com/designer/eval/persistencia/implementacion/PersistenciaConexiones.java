package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.clasesAyuda.ExtraeCausaExcepcion;
import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConexiones;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author luistrivino
 */
@Stateless
public class PersistenciaConexiones implements IPersistenciaConexiones {

    @Override
    public BigInteger consultarSIDActual(EntityManager em) throws Exception {
        BigInteger sid = BigInteger.ZERO;
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query query = em.createNativeQuery("SELECT sys_context('USERENV', 'SID') FROM DUAL");
            sid = new BigInteger((String) query.getSingleResult());
//            em.getTransaction().commit();
        } catch (Exception e) {
//            terminarTransaccionException(em);
            throw new Exception(e);
        }
        return sid;
    }

    private BigDecimal contarConexionesSID(EntityManager em, BigInteger sid) throws Exception {
        BigDecimal conteo = null;
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query query = em.createNativeQuery("SELECT COUNT(*) FROM CONEXIONES WHERE SID =? ");
            query.setParameter(1, sid);
            conteo = (BigDecimal) query.getSingleResult();
            //System.out.println("tipo del conteo: " + conteo.getClass().getName());
//            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(this.getClass().getName()+": "+"contarConexionesSID-excepcion: " + e.getMessage());
//            terminarTransaccionException(em);
            throw new Exception(e);
        }
        return conteo;
    }

    @Override
    public boolean insertarUltimaConexion(EntityManager em, Conexiones conexion) {
        boolean resul = false;
        long conteo = 0;
        try {
            conteo = contarConexionesSID(em, conexion.getSid()).longValue();
        } catch (Exception ee) {
            conteo = 0;
        }
        if (conteo == 0) {
            try {
//                em.getTransaction().begin();
                em.joinTransaction();
                em.persist(conexion);
//                em.getTransaction().commit();
                resul = true;
            } catch (Exception ex) {
                System.out.println(this.getClass().getName()+": "+"Error PersistenciaConexiones.insertarUltimaConexion: la conexion existe");
                if (ExtraeCausaExcepcion.obtenerMensajeSQLException(ex).contains("ORA-00001")) {
                    resul = modificarConexion(em, conexion);
                } else {
//                    terminarTransaccionException(em);
                    resul = false;
                }
            }
        } else {
            resul = modificarConexion(em, conexion);
        }
        return resul;
    }

    private boolean modificarConexion(EntityManager em, Conexiones conexion) {
        try {
            System.out.println(this.getClass().getName()+": "+"La conexion se modificara");
//            em.getTransaction().begin();
            em.joinTransaction();
            Query query = em.createQuery("select c from Conexiones c where c.sid = :sid ");
            query.setParameter("sid", conexion.getSid());
            Conexiones con2 = (Conexiones) query.getSingleResult();
            con2.setDireccionIP(conexion.getDireccionIP());
            con2.setEstacion(conexion.getEstacion());
            con2.setUltimaEntrada(conexion.getUltimaEntrada());
            con2.setUsuarioBD(conexion.getUsuarioBD());
            con2.setUsuarioso(conexion.getUsuarioso());
            em.merge(con2);
            conexion = con2;
            System.out.println(this.getClass().getName()+": "+"conexion modificada");
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex2) {
//            terminarTransaccionException(em);
            return false;
        }
    }

    public void terminarTransaccionException(EntityManager em) {
        System.out.println(this.getClass().getName() + ".terminarTransaccionException");
//        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
        if (em != null && em.isOpen()) {
            System.out.println(this.getClass().getName()+": "+"Antes de hacer rollback");
//            em.getTransaction().rollback();
            em.close();
            System.out.println(this.getClass().getName()+": "+"Despues de hacer rollback");
        }
    }
}
