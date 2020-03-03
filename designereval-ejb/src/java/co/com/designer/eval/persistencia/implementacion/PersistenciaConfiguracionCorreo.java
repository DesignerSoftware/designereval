package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.ConfiguracionCorreo;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaConfiguracionCorreo implements IPersistenciaConfiguracionCorreo {

    @Override
    public ConfiguracionCorreo consultarConfiguracionServidorCorreo(EntityManager eManager, String nitEmpresa) {
        System.out.println(this.getClass().getName() + "." + "consultarConfiguracionServidorCorreo" + "()");
        ConfiguracionCorreo cc = null;
        try {
            if (eManager != null && eManager.isOpen()) {
                eManager.joinTransaction();
//                eManager.getTransaction().begin();
                String sqlQuery = "SELECT cc FROM ConfiguracionCorreo cc WHERE cc.empresa.nit = :nitEmpresa";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("nitEmpresa", Long.valueOf(nitEmpresa));
                cc = (ConfiguracionCorreo) query.getSingleResult();
//                eManager.getTransaction().commit();
            } else {
                cc = null;
                System.out.println(this.getClass().getName()+": "+"entityManager nulo.");
            }
        } catch (IllegalStateException ise) {
            System.out.println(this.getClass().getName()+": "+"ERROR: " + ise.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo: " + e);
        }
        /*finally {
            terminarTransaccionException(eManager);
        }*/
        return cc;
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
