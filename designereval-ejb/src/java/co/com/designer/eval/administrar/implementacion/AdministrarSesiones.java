package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.clasesAyuda.SessionEntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Singleton
public class AdministrarSesiones implements IAdministrarSesiones, Serializable {

    private final List<SessionEntityManager> sessionesActivas;

    public AdministrarSesiones() {
        sessionesActivas = new ArrayList<>();
    }

    @Override
    public void adicionarSesion(SessionEntityManager session) {
        try {
            System.out.println(this.getClass().getName() + "." + "adicionarSesion" + "()");
            sessionesActivas.add(session);
        } catch (Exception e) {
            System.out.println(this.getClass().getName()+": "+"Error AdministrarSesiones.adicionarSesion: " + e);
        }
    }

    @Override
    public EntityManagerFactory obtenerConexionSesion(String idSesion) {
        System.out.println(this.getClass().getName() + "." + "obtenerConexionSesion" + "()");
        SessionEntityManager sesionActual = null;
        EntityManagerFactory emf = null;
        try {
            if (!sessionesActivas.isEmpty()) {
                for (int i = 0; i < sessionesActivas.size(); i++) {
                    if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                        sesionActual = sessionesActivas.get(i);
                        i = sessionesActivas.size();
                    }
                }
            }
            if (sesionActual != null) {
                emf = sesionActual.getEmf();
                System.out.println(this.getClass().getName()+": "+"Se creó entityManagerFactory.");
                System.out.println(this.getClass().getName()+": "+"eManager" + emf.toString());
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName()+": "+"error en " + "obtenerConexionSesion");
            System.out.println(this.getClass().getName()+": "+"Causa: " + e);
            sesionActual = null;
        }
        return emf;
    }

    @Override
    public void borrarSesion(String idSesion) {
        try {
            System.out.println(this.getClass().getName() + "." + "borrarSesion()");
            if (!sessionesActivas.isEmpty()) {
                for (int i = 0; i < sessionesActivas.size(); i++) {
                    if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                        sessionesActivas.get(i).setIdSession("");
                        sessionesActivas.remove(sessionesActivas.get(i));
                        i = sessionesActivas.size();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName()+": "+"Error AdministrarSesiones.borrarSesion: " + e);
        }
    }
}
