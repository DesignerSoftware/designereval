package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.clasesAyuda.SessionEntityManager;
import javax.ejb.Local;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */

@Local
public interface IAdministrarSesiones {

    public void adicionarSesion(SessionEntityManager session);

    public EntityManagerFactory obtenerConexionSesion(String idSesion);

    public void borrarSesion(String idSesion);
}
