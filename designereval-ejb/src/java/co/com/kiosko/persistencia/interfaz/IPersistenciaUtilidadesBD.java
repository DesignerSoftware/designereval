package co.com.kiosko.persistencia.interfaz;

/**
 *
 * @author Felipe Trivi�o
 */
public interface IPersistenciaUtilidadesBD {

    public byte[] encriptar(javax.persistence.EntityManager eManager, java.lang.String valor);

    public String desencriptar(javax.persistence.EntityManager eManager, byte[] valor);
    
}
