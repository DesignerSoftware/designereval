package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.controlador.ingreso.ControladorIngreso;
//import java.io.*;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import co.com.designer.eval.clasesAyuda.CadenasConexion;
import co.com.designer.eval.clasesAyuda.LeerArchivoXML;
import co.com.designer.eval.entidades.Personas;
import co.com.designer.eval.administrar.interfaz.IAdministrarInicioEval;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorInformacionBasica implements Serializable {

    @EJB
    private IAdministrarInicioEval administrarInicioEval;
    private Date ultimaConexionEmpleado;
    private Personas persona;
    private String usuario;
    private StreamedContent logoEmpresa;
    private String pathImagenes;
    private String nitEmpresa;
    private String fondoEmpresa;
    private FileInputStream fis;

    public ControladorInformacionBasica() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControladorInformacionBasica.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInicioEval.obtenerConexion(ses.getId());
            persona = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getPersona();
            usuario = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUsuario();
            ultimaConexionEmpleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUltimaConexion();
            nitEmpresa = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit();
            pathImagenes = administrarInicioEval.obtenerRutaImagenes();
            System.out.println("Inicializado");
        } catch (ELException e) {
            System.out.println(this.getClass().getName() + ".inicializarAdministrador" + ": " + e);
        }
    }

    public void obtenerLogoEmpresa() {
        String formatoFotoEmpleado = "image/png";
        String rstLogo = administrarInicioEval.logoEmpresa(nitEmpresa);
        String logo;
        try {
            logo = rstLogo.substring(0, rstLogo.length() - 4);
        } catch (NullPointerException npe) {
            System.out.println(this.getClass().getName() + ".inicializarAdministrador npe" + ": " + npe);
            logo = null;
        }
        if (logo != null && !"".equalsIgnoreCase(logo)) {
            String rutaLogo = pathImagenes + logo + ".png";
            if (rutaLogo != null) {
                try {
                    fis = new FileInputStream(new File(rutaLogo));
                    logoEmpresa = new DefaultStreamedContent(fis, formatoFotoEmpleado, logo);
                    System.out.println("rutaLogo: " + rutaLogo);
                } catch (FileNotFoundException e) {
                    try {
                        rutaLogo = pathImagenes + "sinLogo.png";
                        fis = new FileInputStream(new File(rutaLogo));
                        logoEmpresa = new DefaultStreamedContent(fis, formatoFotoEmpleado, rutaLogo);
                        System.out.println(this.getClass().getName() + ".obtenerLogoEmpresa e" + ": " + e);
                    } catch (FileNotFoundException ex) {
                        System.out.println(this.getClass().getName() + ".obtenerLogoEmpresa ex" + ": " + ex);
                    }
                }
            }
        }
    }

    public void obtenerFondoEmpresa() {
        for (CadenasConexion elemento : (new LeerArchivoXML()).leerArchivoEmpresasConexion()) {
            if (elemento.getNit().equals(nitEmpresa)) {
                fondoEmpresa = elemento.getFondo();
            }
        }
    }

    //GETTER AND SETTER
    public Date getUltimaConexionEmpleado() {
        return ultimaConexionEmpleado;
    }

    public StreamedContent getLogoEmpresa() {
        if (logoEmpresa != null) {
            try {
                logoEmpresa.getStream().available();
            } catch (IOException e) {
                obtenerLogoEmpresa();
            }
        } else {
            obtenerLogoEmpresa();
        }
        return logoEmpresa;
    }

    public void setLogoEmpresa(StreamedContent logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }

    public String getFondoEmpresa() {
        if (fondoEmpresa == null) {
            obtenerFondoEmpresa();
        }
        System.out.println("fondoEmpresa: " + fondoEmpresa);
        return fondoEmpresa;
    }

    public void setFondoEmpresa(String fondoEmpresa) {
        this.fondoEmpresa = fondoEmpresa;
    }

    public Personas getPersona() {
        return persona;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
