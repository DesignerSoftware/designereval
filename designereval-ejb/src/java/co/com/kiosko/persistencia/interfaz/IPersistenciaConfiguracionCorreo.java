package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Trivi�o
 */
public interface IPersistenciaConfiguracionCorreo {

    public co.com.kiosko.entidades.ConfiguracionCorreo consultarConfiguracionServidorCorreo(javax.persistence.EntityManager eManager, java.math.BigInteger secuenciaEmpresa);
    
}
