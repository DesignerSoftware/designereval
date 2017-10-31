package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Generales;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaGenerales;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Felipe Trivi�o
 */
@Stateless
public class PersistenciaGenerales implements IPersistenciaGenerales {

    @Override
    public Generales consultarRutasGenerales(EntityManager eManager) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT g FROM Generales g";
            Query query = eManager.createQuery(sqlQuery);
            Generales g = (Generales) query.getResultList().get(0);
            eManager.getTransaction().commit();
            return g;
        } catch (Exception e) {
            System.out.println("Error PersistenciaGenerales.consultarRutasGenerales: " + e);
            terminarTransaccionException(eManager);
            return null;
        }
    }

    @Override
    public String logoEmpresa(EntityManager eManager, String nit) {
        try {
            eManager.getTransaction().begin();
            String sqlQuery = "SELECT LOGO FROM EMPRESAS WHERE NIT = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, nit);
            String logo = (String) query.getSingleResult();
            eManager.getTransaction().commit();
            return logo;
        } catch (Exception e) {
            System.out.println("Error PersistenciaGenerales.consultarRutasGenerales: " + e);
            terminarTransaccionException(eManager);
            return null;
        }
    }

    public void terminarTransaccionException(EntityManager em) {
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
