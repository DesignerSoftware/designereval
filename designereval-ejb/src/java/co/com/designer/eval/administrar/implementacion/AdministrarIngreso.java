package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarIngreso;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.clasesAyuda.ExtraeCausaExcepcion;
import co.com.designer.eval.clasesAyuda.SessionEntityManager;
import co.com.designer.eval.conexionFuente.implementacion.SesionEntityManagerFactory;
import co.com.designer.eval.conexionFuente.interfaz.ISesionEntityManagerFactory;
import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Perfiles;
import co.com.designer.eval.entidades.Personas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConexionInicial;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConexiones;
import java.io.Serializable;
import java.math.BigInteger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 */
@Stateful
public class AdministrarIngreso implements IAdministrarIngreso, Serializable {

    @EJB
    private IPersistenciaConexionInicial persistenciaConexionInicial;
    @EJB
    private IPersistenciaConexiones persistenciaConexiones;
    @EJB
    private IAdministrarSesiones administrarSessiones;

    private String unidadPersistencia;
    private ISesionEntityManagerFactory sessionEMF;

//    private transient EntityManagerFactory emf;
//    private EntityManager em;
    private BigInteger secPerfil;
    private Perfiles perfilUsuario;
    private Personas persona;

    public AdministrarIngreso() {
        sessionEMF = new SesionEntityManagerFactory();
    }

    @Override
    public boolean conexionIngreso(String unidadPersistencia) {
        System.out.println(this.getClass().getName() + ".conexionIngreso()");
        System.out.println("Unidad de persistencia: " + unidadPersistencia);
        boolean resul;
        try {
            EntityManagerFactory emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                resul = true;
            } else {
                System.out.println(this.getClass().getName() + ": " + "Error la unidad de persistencia no existe, revisar el archivo XML de persistencia.");
                resul = false;
            }
            if (resul) {
                this.unidadPersistencia = unidadPersistencia;
                if (emf != null && emf.isOpen()) {
                    EntityManager em = emf.createEntityManager();
                    em.close();
                }
                System.out.println(this.getClass().getName() + ": " + "Unid. Pesistencia asignada.");
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error general: " + e);
            resul = false;
//            emf = null;
        }
        return resul;
    }

    @Override
    public Personas conexionUsuario(String baseDatos, String usuario, String clave) {
        EntityManager em = null;
        persona = null;
        try {
            em = sessionEMF.crearConexionUsuario(unidadPersistencia).createEntityManager();
            persistenciaConexionInicial.setearRolEntrada(em);
            secPerfil = persistenciaConexionInicial.usuarioLogin(em, usuario);
            if (secPerfil != null) {
                perfilUsuario = persistenciaConexionInicial.perfilUsuario(em, secPerfil);
                if (perfilUsuario != null) {
//                    cerrarConexiones();
                    em = sessionEMF.crearFactoryUsuario(usuario, clave, baseDatos).createEntityManager();
                    if (setearRol()) {
                        persona = persistenciaConexionInicial.obtenerPersona(em, usuario, clave);
                    } else {
                        System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: Error seteando el rol.");
//                        cerrarConexiones();
                    }
                } else {
                    System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: perfilUsuario es nulo.");
//                    cerrarConexiones();
                }
            } else {
                System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: secPerfil es nulo.");
//                cerrarConexiones();
            }
            em.close();
            return persona;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: " + e);
//            cerrarConexiones();
            return null;
        }
    }

    @Override
    public Personas conexionUsuario(String baseDatos, String esquema, String usuario, String clave) {
        EntityManager em = sessionEMF.crearConexionUsuario(unidadPersistencia).createEntityManager();
        if (esquema != null && !esquema.isEmpty()) {
            try {
                persona = null;
                persistenciaConexionInicial.setearRolEntrada(em, esquema);
                secPerfil = persistenciaConexionInicial.usuarioLogin(em, usuario, esquema);
                if (secPerfil != null) {
                    perfilUsuario = persistenciaConexionInicial.perfilUsuario(em, secPerfil);
                    if (perfilUsuario != null) {
                        cerrarConexiones();
//                        EntityManagerFactory emf = sessionEMF.crearFactoryUsuario(usuario, clave, baseDatos);
                        EntityManagerFactory emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
                        em = emf.createEntityManager();
                        if (setearRol()) {
                            persona = persistenciaConexionInicial.obtenerPersona(em, usuario, clave);
                            if (persona == null){
                                System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: Error obteniendo la persona.");
                            }
                        } else {
                            System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: Error seteando el rol.");
//                            cerrarConexiones();
                        }
                    } else {
                        System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: perfilUsuario es nulo.");
//                        cerrarConexiones();
                    }
                } else {
                    System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: secPerfil es nulo.");
//                    cerrarConexiones();
                }
                return persona;
            } catch (Exception e) {
                System.out.println(this.getClass().getName() + ": " + "Error creando EMF AdministrarIngreso.conexionUsuario: " + e);
//                cerrarConexiones();
                return null;
            }
        } else {
            return conexionUsuario(baseDatos, usuario, clave);
        }
    }

    public boolean setearRol() {
        try {
            EntityManager em = sessionEMF.crearConexionUsuario(unidadPersistencia).createEntityManager();
            if (em != null) {
                if (em.isOpen()) {
                    persistenciaConexionInicial.setearUsuario(em, perfilUsuario.getDescripcion(), perfilUsuario.getPwd());
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarIngreso.validarConexionUsuario: " + e);
            return false;
        }
    }

    @Override
    public Conexiones ultimaConexionUsuario(String usuario) {
        try {
            EntityManager em = sessionEMF.crearConexionUsuario(unidadPersistencia).createEntityManager();
            if (em != null) {
                if (em.isOpen()) {
                    return persistenciaConexionInicial.conexionUsuario(em, usuario);
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarIngreso.ultimaConexionUsuario: " + e);
            return null;
        }
    }

    @Override
    public boolean insertarUltimaConexion(Conexiones conexion) {
        EntityManager em = null;
        try {
            em = sessionEMF.crearConexionUsuario(unidadPersistencia).createEntityManager();
            if (em != null) {
                if (em.isOpen()) {
                    conexion.setSid(persistenciaConexiones.consultarSIDActual(em));
                }
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error consultando el SID del usuario");
            System.out.println(this.getClass().getName() + ": " + "exc: " + e.getMessage());
        }
        try {
            if (em != null) {
                if (em.isOpen()) {
                    return persistenciaConexiones.insertarUltimaConexion(em, conexion);
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarIngreso.insertarUltimaConexion: " + e);
            return false;
        }
    }

    @Override
    public boolean adicionarConexionUsuario(String idSesion) {
        System.out.println(this.getClass().getName() + ".adicionarConexionUsuario()");
        boolean resul;
        try {
            EntityManagerFactory emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            SessionEntityManager sem = new SessionEntityManager(idSesion, unidadPersistencia, emf);
            administrarSessiones.adicionarSesion(sem);
            resul = true;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error general: " + e);
            resul = false;
        }
        return resul;
    }

    @Override
    public void cerrarSession(String idSesion) {
        System.out.println(this.getClass().getName() + ".modificarUltimaConexion()");
        try {
            administrarSessiones.borrarSesion(idSesion);
            //emf.close();
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error general " + "cerrarSession" + ": " + e);
        }
    }

    public Personas getPersona() {
        return persona;
    }

    @Override
    public String cambiarPassword(String usuario, String password) {
        System.out.println(this.getClass().getName() + ".cambiarPassword()");
        String res = "";
        EntityManager em = null;
        try {
            em = sessionEMF.crearConexionUsuario(unidadPersistencia).createEntityManager();
            persistenciaConexionInicial.cambiarPassword(em, usuario, password);
            if (em != null && em.isOpen()) {
                em.close();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarIngreso:cambiarPassword: " + e);
            res = ExtraeCausaExcepcion.obtenerMensajeSQLException(e);
            if (em == null || !em.isOpen()) {
                em = sessionEMF.crearConexionUsuario(unidadPersistencia).createEntityManager();
//                em = emf.createEntityManager();
            }
            setearRol();
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return res;
    }

    public void cerrarConexiones() {
        /*try {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarIngreso.cerrarConexiones: " + e);
        }*/
    }
}//Fin de la clase.
