package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Cursos;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

@Local
public interface IPersistenciaCursos {
    public List<Cursos> obtenerCursos(EntityManager em);
}
