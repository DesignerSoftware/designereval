package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Cursos;
import co.com.designer.eval.entidades.Profesiones;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

@Local
public interface IPersistenciaProfesiones {

    public List<Profesiones> obtenerProfesiones(EntityManager em);
}
