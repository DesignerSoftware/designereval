package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Perfiles;
import co.com.designer.eval.entidades.Personas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConexionInicial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Stateless
public class PersistenciaConexionInicial implements IPersistenciaConexionInicial {

    @Override
    public boolean validarUsuario(EntityManager eManager, String usuario) {
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
            String sqlQuery = "SET ROLE ROLENTRADA";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.executeUpdate();
//            sqlQuery = "select COUNT(*) from usuarios where alias = ? AND activo = 'S' ";
            sqlQuery = "select COUNT(*) from conexioneseval where seudonimo = ? AND activo = 'S' ";
            query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
//            eManager.getTransaction().commit();
            if (instancia > 0) {
                System.out.println("El usuario es correcto y esta activo");
                return true;
            } else {
                System.out.println("El usuario es incorrecto y/o esta inactivo");
//                eManager.getEntityManagerFactory().close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error validarUsuario: " + e);
//            terminarTransaccionException(eManager);
            return false;
        }
    }

    @Override
    public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD) {
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
//            Query query = eManager.createNativeQuery("SELECT p.secuencia FROM Usuarios u, Perfiles p WHERE u.alias = ? AND u.perfil = p.secuencia");
            Query query = eManager.createNativeQuery("SELECT p.secuencia FROM Perfiles p WHERE p.descripcion = ? ");
//            query.setParameter(1, usuarioBD);
            query.setParameter(1, "EVALDS");
            BigDecimal secPerfil = (BigDecimal) query.getSingleResult();
            BigInteger resultado = secPerfil.toBigInteger();
//            eManager.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.usuarioLogin() e: " + e);
//            terminarTransaccionException(eManager);
            return null;
        }
    }

    @Override
    public Personas obtenerPersona(EntityManager eManager, String usuarioBD) {
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
//            Query query = eManager.createNativeQuery("SELECT p.* FROM Usuarios u, Personas p WHERE u.alias = ? AND u.persona = p.secuencia ", Personas.class);
            Query query = eManager.createNativeQuery("SELECT p.* FROM ConexionesEval u, Personas p WHERE u.seudonimo = ? AND u.persona = p.secuencia ", Personas.class);
            query.setParameter(1, usuarioBD);
            Personas resultado = (Personas) query.getSingleResult();
//            eManager.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.usuarioLogin() e: " + e);
//            terminarTransaccionException(eManager);
            return null;
        }
    }

    @Override
    public Perfiles perfilUsuario(EntityManager eManager, BigInteger secPerfil) {
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
            Query query = eManager.createQuery("SELECT p FROM Perfiles p WHERE p.secuencia = :secPerfil ");
            query.setParameter("secPerfil", secPerfil);
            Perfiles perfil = (Perfiles) query.getSingleResult();
//            eManager.getTransaction().commit();
            return perfil;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.perfilUsuario() e: " + e);
//            terminarTransaccionException(eManager);
            return null;
        }
    }

    @Override
    public Conexiones conexionUsuario(EntityManager eManager, String usuario) {
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
            Query query = eManager.createQuery("SELECT c FROM Conexiones c WHERE c.usuarioBD = :usuario ORDER BY  c.ultimaEntrada DESC ");
            query.setParameter("usuario", usuario);
            List conexion = query.getResultList();
            Conexiones resultado = null;
            if (conexion != null && !conexion.isEmpty()) {
                resultado = (Conexiones) conexion.get(0);
            }
//            eManager.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.conexionUsuario() e: " + e);
//            terminarTransaccionException(eManager);
            return null;
        }
    }

    @Override
    public void setearRolEntrada(EntityManager eManager) {
        System.out.println(this.getClass().getName() + ".setearRolEntrada()");
        String texto = "SET ROLE ROLENTRADA";
        System.out.println("setearUsuario:texto: " + texto);
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
            String sqlQuery = texto;
            Query query = eManager.createNativeQuery(sqlQuery);
            query.executeUpdate();
//            eManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.setearRolEntrada() e: " + e);
//            terminarTransaccionException(eManager);
        }
    }

    @Override
    public void setearUsuario(EntityManager eManager, String rol, String pwd) {
        System.out.println(this.getClass().getName() + ".setearUsuario()");
        System.out.println("setearUsuario:rol: " + rol);
        System.out.println("setearUsuario:pwd: " + pwd);
        String texto = "SET ROLE " + rol + " IDENTIFIED BY " + pwd;
        System.out.println("setearUsuario:texto: " + texto);
        try {
            eManager.joinTransaction();
//            eManager.getTransaction().begin();
            String sqlQuery = texto;
            Query query = eManager.createNativeQuery(sqlQuery);
            query.executeUpdate();
//            eManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.setearUsuario() e: " + e);
//            terminarTransaccionException(eManager);
        }
    }

    @Override
    public EntityManager validarConexionUsuario(EntityManagerFactory emf) {
        try {
            EntityManager eManager = emf.createEntityManager();
            if (eManager.isOpen()) {
                return eManager;
            }
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarConexionUsuario : " + e);
            try {
                emf.close();
            } catch (NullPointerException npe) {
                System.out.println("error de nulo en el entity manager.");
            }
        }
        return null;
    }

    @Override
    public void cambiarPassword(EntityManager em, String usuario, String password) {
        System.out.println(this.getClass().getName() + ".cambiarPassword()");
        System.out.println("cambiarPassword:usuario: " + usuario);
        System.out.println("cambiarPassword:password: " + password);
//        String consulta = "alter user " + usuario + " identified by " + password + " ";
        String consulta = "update conexioneseval set pwd=generales_pkg.encrypt(" + password + ") where seudonimo = " + usuario + " ";
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query query = em.createNativeQuery(consulta);
            query.executeUpdate();
//            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("cambiarPassword:Exception: " + e);
//            terminarTransaccionException(em);
        }
    }

    public void terminarTransaccionException(EntityManager em) {
        System.out.println(this.getClass().getName() + ".terminarTransaccionException");
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            System.out.println("Antes de hacer rollback");
//            em.getTransaction().rollback();
            System.out.println("Despues de hacer rollback");
        }
    }
}
