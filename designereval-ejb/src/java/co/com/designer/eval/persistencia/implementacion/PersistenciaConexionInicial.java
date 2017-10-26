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

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    private EntityManager em;

    @Override
    public boolean validarUsuario(EntityManager eManager, String usuario) {
        try {
            em = eManager;
            em.getTransaction().begin();
            String sqlQuery = "SET ROLE ROLENTRADA";
            Query query = em.createNativeQuery(sqlQuery);
            query.executeUpdate();
            sqlQuery = "select COUNT(*) from usuarios where alias = ? AND activo = 'S' ";
            query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
            em.getTransaction().commit();
            if (instancia > 0) {
                System.out.println("El usuario es correcto y esta activo");
                return true;
            } else {
                System.out.println("El usuario es incorrecto y/o esta inactivo");
                em.getEntityManagerFactory().close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error validarUsuario: " + e);
            return false;
        }
    }

    @Override
    public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD) {
        try {
            em = eManager;
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT p.secuencia FROM Usuarios u, Perfiles p WHERE u.alias = ? AND u.perfil = p.secuencia");
            query.setParameter(1, usuarioBD);
            em.getTransaction().commit();
            BigDecimal secPerfil = (BigDecimal) query.getSingleResult();
            return secPerfil.toBigInteger();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.usuarioLogin() e: " + e);
            return null;
        }
    }

    @Override
    public Personas obtenerPersona(EntityManager eManager, String usuarioBD) {
        try {
            em = eManager;
            em.getTransaction().begin();
            Query query = em.createNativeQuery("SELECT p.* FROM Usuarios u, Personas p WHERE u.alias = ? AND u.persona = p.secuencia ", Personas.class);
            query.setParameter(1, usuarioBD);
            em.getTransaction().commit();
            return (Personas) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.usuarioLogin() e: " + e);
            return null;
        }
    }

    @Override
    public Perfiles perfilUsuario(EntityManager eManager, BigInteger secPerfil) {
        try {
            em = eManager;
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT p FROM Perfiles p WHERE p.secuencia = :secPerfil ");
            query.setParameter("secPerfil", secPerfil);
            Perfiles perfil = (Perfiles) query.getSingleResult();
            em.getTransaction().commit();
            return perfil;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.perfilUsuario() e: " + e);
            return null;
        }
    }

    @Override
    public Conexiones conexionUsuario(EntityManager eManager, String usuario) {
        try {
            em = eManager;
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM Conexiones c WHERE c.usuarioBD = :usuario ORDER BY  c.ultimaEntrada DESC ");
            query.setParameter("usuario", usuario);
            List conexion = query.getResultList();
            Conexiones resultado = null;
            if (conexion != null && !conexion.isEmpty()) {
                resultado = (Conexiones) conexion.get(0);
            }
            em.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.conexionUsuario() e: " + e);
            if (em != null && em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        }
    }

    @Override
    public void setearUsuario(EntityManager eManager, String rol, String pwd) {
        System.out.println(this.getClass().getName() + ".setearUsuario()");
        System.out.println("setearUsuario:rol: " + rol);
        System.out.println("setearUsuario:pwd: " + pwd);
        String texto = "SET ROLE " + rol + " IDENTIFIED BY " + pwd;
//        String texto = "SET ROLE ? IDENTIFIED BY ? ";
        System.out.println("setearUsuario:texto: " + texto);
        em = eManager;
        try {
            em.getTransaction().begin();
            String sqlQuery = texto;
            Query query = em.createNativeQuery(sqlQuery);
//            query.setParameter(1, rol);
//            query.setParameter(2, pwd);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaConexionInicial.setearUsuario() e: " + e);
        }
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(EntityManager eManager, String usuario, String clave, String nitEmpresa) {
        boolean resultado = false;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS ck, EMPLEADOS e, EMPRESAS em "
                    + "WHERE ck.EMPLEADO = e.SECUENCIA "
                    + "AND e.empresa = em.secuencia "
                    + "AND e.codigoempleado = ? "
                    + "AND ck.PWD = GENERALES_PKG.ENCRYPT( ? ) "
                    + "AND em.nit = ? ";
            Query query = eManager.createNativeQuery(sqlQuery);
            query.setParameter(1, usuario);
            query.setParameter(2, clave);
            query.setParameter(3, nitEmpresa);
            BigDecimal retorno = (BigDecimal) query.getSingleResult();
            Integer instancia = retorno.intValueExact();
//            eManager.getTransaction().commit();
            /*if (instancia > 0) {
             //System.out.println("El usuario y clave son correctos.");
             return true;
             } else {
             //System.out.println("El usuario o clave son incorrectos");
             return false;
             }*/
            resultado = instancia > 0;
//            return resultado;
        } catch (Exception e) {
            System.out.println("Error PersistenciaConexionInicial.validarIngresoUsuarioRegistrado: " + e);
//            return false;
            resultado = false;
        }
        return resultado;
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
    public void cambiarPassword(EntityManager em, String usuario, String password) throws Exception {
        System.out.println(this.getClass().getName() + ".cambiarPassword()");
        System.out.println("cambiarPassword:usuario: " + usuario);
        System.out.println("cambiarPassword:password: " + password);
//        String consulta = "alter user ? identified by ? ";
        String consulta = "alter user " + usuario + " identified by " + password + " ";
//        String usuario = "";
        try {
            em.clear();
            em.getTransaction().begin();
            Query query = em.createNativeQuery(consulta);
//            query.setParameter(1, usuario);
//            query.setParameter(2, password);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("cambiarPassword:Exception: " + e.getCause());
            throw e;
        }
    }
}
