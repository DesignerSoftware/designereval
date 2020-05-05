package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Cursos;
import co.com.designer.eval.entidades.Profesiones;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaProfesiones;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Thalia Manrique
 */
@Stateless
public class PersistenciaProfesiones implements IPersistenciaProfesiones {
    
    
    @Override
    public List<Profesiones> obtenerProfesiones(EntityManager em) {
        try {
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT SECUENCIA, DESCRIPCION "
                    + "FROM PROFESIONES "
                    + "ORDER BY CODIGO ASC", Profesiones.class);
            List<Profesiones> lst = q.getResultList();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaProfesiones.obtenerProfesiones: " + ex);
            return null;
        }
    }
}
