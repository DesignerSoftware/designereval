package co.com.designer.eval.conexionFuente.interfaz;

import javax.persistence.EntityManagerFactory;


public interface ISesionEntityManagerFactory {

    /**
     * Método para probar la conexión la unidad de persistencia y verificar
     * si hay conexión.
     * @param unidadPersistencia
     * @return 
     */
    public EntityManagerFactory crearConexionUsuario(String unidadPersistencia);

    /**
     * Método para crear una conexión para un usuario de base de datos.
     * El parámetro baseDatos se tomará como la unidad de persistencia 
     * a conectar.
     * @param usuario
     * @param clave
     * @param baseDatos
     * @return 
     */
    public EntityManagerFactory crearFactoryUsuario(String usuario, String clave, String baseDatos);

}
