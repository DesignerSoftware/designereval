package co.com.designer.eval.conexionFuente.interfaz;

import javax.persistence.EntityManagerFactory;


public interface ISesionEntityManagerFactory {

    /**
     * M�todo para probar la conexi�n la unidad de persistencia y verificar
     * si hay conexi�n.
     * @param unidadPersistencia
     * @return 
     */
    public EntityManagerFactory crearConexionUsuario(String unidadPersistencia);

    /**
     * M�todo para crear una conexi�n para un usuario de base de datos.
     * El par�metro baseDatos se tomar� como la unidad de persistencia 
     * a conectar.
     * @param usuario
     * @param clave
     * @param baseDatos
     * @return 
     */
    public EntityManagerFactory crearFactoryUsuario(String usuario, String clave, String baseDatos);

}
