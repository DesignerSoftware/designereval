package co.com.designer.eval.conexionFuente.implementacion;

import co.com.designer.eval.conexionFuente.interfaz.ISesionEntityManagerFactory;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 */
@Stateless
public class SesionEntityManagerFactory implements ISesionEntityManagerFactory, Serializable {

    @Override
    public EntityManagerFactory crearConexionUsuario(String unidadPersistencia) {
        try {
            return Persistence.createEntityManagerFactory(unidadPersistencia);
        } catch (Exception e) {
            System.out.println("Error SesionEntityManagerFactory.crearConexionUsuario: " + e);
            return null;
        }
    }

    @Override
    public EntityManagerFactory crearFactoryUsuario(String usuario, String clave, String baseDatos) {
        /*try {
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.user", usuario);
            properties.put("javax.persistence.jdbc.password", clave);
            return Persistence.createEntityManagerFactory(baseDatos, properties);
        } catch (Exception e) {
            System.out.println("Error crearFactoryUsuario: " + e.getMessage());
            return null;
        }*/
        return crearConexionUsuario(baseDatos);
    }
}
