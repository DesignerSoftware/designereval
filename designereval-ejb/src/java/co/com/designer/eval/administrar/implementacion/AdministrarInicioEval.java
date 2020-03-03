package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.entidades.Generales;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaGenerales;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import co.com.designer.eval.administrar.interfaz.IAdministrarInicioEval;
import java.io.Serializable;
import javax.ejb.Stateful;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarInicioEval implements IAdministrarInicioEval, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
    private transient EntityManagerFactory emf;
//    private EntityManager em;
    private String idSesion;

    @Override
    public void obtenerConexion(String idSesion) {
        EntityManager em = null;
        try {
            this.idSesion = idSesion;
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                em = emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicioEval.obtenerConexion: " + e);
        }
    }

    private EntityManager obtenerConexion() throws Exception {
//        EntityManager em = null;
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                return emf.createEntityManager();
            }
            throw new Exception("EntityManager nulo");
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicioEval.obtenerConexion: " + e);
            throw new Exception(e);
        }
    }

    @Override
    public String obtenerRutaImagenes() {
        EntityManager em = null;
        String rutaFoto = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            if (general != null) {
                rutaFoto = general.getPathfoto();
            } else {
                rutaFoto = null;
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicioEval.obtenerRutaImagenes: " + e);
        }
        return rutaFoto;
    }

    @Override
    public String logoEmpresa(String nit) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaGenerales.logoEmpresa(em, nit);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicioEval.obtenerRutaImagenes: " + e);
            return null;
        }
    }
}
