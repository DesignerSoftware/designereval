package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.entidades.Empleados;
//import co.com.kiosko.clasesAyuda.NavegationPageURL;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Felipe Trivi�o
 */
@ManagedBean
@ViewScoped
public class ControladorKio_DatoPersonal implements Serializable {

    private Empleados empleado;
    private String urlMenuNavegation;

    public ControladorKio_DatoPersonal() {
    }

    @PostConstruct
    public void inicializar() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }
}
