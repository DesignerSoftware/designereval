package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Trivi�o
 */
public interface IPersistenciaParametrizaClave {

    public co.com.kiosko.entidades.ParametrizaClave obtenerFormatoClave(javax.persistence.EntityManager eManager, long nitEmpresa);
    
}
