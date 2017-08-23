package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Personas;
import javax.ejb.Local;

/**
 *
 * @author Felipe Trivi�o
 */
@Local
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean validarUsuario(String usuario);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);

    public Personas conexionUsuario(String baseDatos, String usuario, String contrase�a);

    public Conexiones ultimaConexionUsuario(String usuario);

    public boolean insertarUltimaConexion(Conexiones conexion);

}