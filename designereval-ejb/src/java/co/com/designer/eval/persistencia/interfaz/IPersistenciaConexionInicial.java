package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Perfiles;
import co.com.designer.eval.entidades.Personas;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IPersistenciaConexionInicial {

    public EntityManager validarConexionUsuario(EntityManagerFactory emf);

    public boolean validarUsuario(EntityManager eManager, String usuario);
    
    public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD, String esquema);

    public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD);

    public Perfiles perfilUsuario(EntityManager eManager, BigInteger secPerfil);

    /**
     * Método para ejecutar el query de seteo del rol entrada.
     * @param eManager 
     */
    public void setearRolEntrada(EntityManager eManager);
    
    public void setearRolEntrada(EntityManager eManager, String esquema);
    
    public void setearUsuario(EntityManager eManager, String rol, String pwd);

    public Personas obtenerPersona(EntityManager eManager, String usuarioBD, String pass);

    public Conexiones conexionUsuario(EntityManager eManager, String usuario);

    /**
     * Metodo para la modificacion del password de determinado usuario.
     *
     * @param em
     * @param usuario
     * @param password
     */
    public void cambiarPassword(EntityManager em, String usuario, String password);
}
