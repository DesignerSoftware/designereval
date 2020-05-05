package co.com.designer.convertidor;

import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.entidades.EvalActividades;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalActividades;


import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
@FacesConverter("ConvertidorActividades")
public class ConvertidorActividades implements Converter {

    @EJB
    IAdministrarSesiones administrarSesiones;
    @EJB
    IPersistenciaEvalActividades persistenciaAct;
    private EntityManagerFactory emf;

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            emf = administrarSesiones.obtenerConexionSesion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        Object dato =null;
        if (value != null && value.trim().length() > 0) {
            try {
                EntityManager em = emf.createEntityManager();
                dato = persistenciaAct.consultarEvalActividad(em, new BigDecimal(value));
                em.close();
            } catch (NumberFormatException e) {
                System.out.println("getAsObject: "+e);
            }
        } else {
            dato = null;
        }
        return dato;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((EvalActividades) object).getSecuencia());
        } else {
            return null;
        }
    }
}
