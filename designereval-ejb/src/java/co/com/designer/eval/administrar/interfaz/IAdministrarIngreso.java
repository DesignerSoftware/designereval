package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Personas;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);

    public Personas conexionUsuario(String baseDatos, String usuario, String clave);
    
    public Personas conexionUsuario(String baseDatos, String esquema, String usuario, String clave);

    public Conexiones ultimaConexionUsuario(String usuario);

    public boolean insertarUltimaConexion(Conexiones conexion);

    public String cambiarPassword(String usuario, String password);

}
