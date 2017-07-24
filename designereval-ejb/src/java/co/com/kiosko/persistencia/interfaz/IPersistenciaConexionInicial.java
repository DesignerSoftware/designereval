package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.Perfiles;
import co.com.kiosko.entidades.Personas;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface IPersistenciaConexionInicial {

    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa);

    public EntityManager validarConexionUsuario(EntityManagerFactory emf);

    public boolean validarUsuario(EntityManager eManager, String usuario);

    public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD);

    public Perfiles perfilUsuario(EntityManager eManager, BigInteger secPerfil);

    public void setearUsuario(EntityManager eManager, String rol, String pwd);

    public Personas obtenerPersona(EntityManager eManager, String usuarioBD);

}
