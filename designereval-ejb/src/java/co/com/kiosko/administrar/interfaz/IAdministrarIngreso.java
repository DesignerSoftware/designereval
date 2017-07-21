package co.com.kiosko.administrar.interfaz;

import javax.ejb.Local;

/**
 *
 * @author Felipe Trivi�o
 */
@Local
public interface IAdministrarIngreso {

    public boolean conexionIngreso(String unidadPersistencia);

    public boolean validarUsuario(String usuario);

    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave, String nitEmpresa);

    public boolean adicionarConexionUsuario(String idSesion);

    public void cerrarSession(String idSesion);

    public boolean conexionUsuario(String baseDatos, String usuario, String contrase�a);

}
