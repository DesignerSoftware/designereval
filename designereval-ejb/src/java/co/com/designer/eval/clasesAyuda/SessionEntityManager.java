package co.com.designer.eval.clasesAyuda;

import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 * @version 2.0
 */
public class SessionEntityManager {

    private String idSession;
    private String unidadPersistencia;
    private EntityManagerFactory emf;

    public SessionEntityManager(String idSession, String unidadPersistencia) {
        this.idSession = idSession;
        this.unidadPersistencia = unidadPersistencia;
    }

    public SessionEntityManager(String idSession, String unidadPersistencia, EntityManagerFactory emf) {
        this.idSession = idSession;
        this.unidadPersistencia = unidadPersistencia;
        this.emf = emf;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void cerrarEMF() {
        emf.close();
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }
    
    public String getUnidadPersistencia() {
        return unidadPersistencia;
    }

    public void setUnidadPersistencia(String unidadPersistencia) {
        System.out.println(this.getClass().getName() + ".setUnidadPersistencia()");
        System.out.println(this.getClass().getName()+": "+"unidad de persistencia: " + this.unidadPersistencia);
        this.unidadPersistencia = unidadPersistencia;
    }

}
