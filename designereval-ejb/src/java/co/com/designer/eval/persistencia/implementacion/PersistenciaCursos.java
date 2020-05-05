package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Cursos;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaCursos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Thalia Manrique
 */
@Stateless
public class PersistenciaCursos implements IPersistenciaCursos {
    
    @Override
    public List<Cursos> obtenerCursos(EntityManager em) {
        try {
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT SECUENCIA, CODIGO, NOMBRE, TIPOCURSO, OBJETIVO "
                    + "FROM CURSOS "
                    + "ORDER BY CODIGO ASC", Cursos.class);
            List<Cursos> lst = q.getResultList();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaCursos.obtenerCursos: " + ex);
            return null;
        }
    }
}
