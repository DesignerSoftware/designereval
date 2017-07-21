package co.com.kiosko.controlador.kiosko;

import co.com.kiosko.administrar.interfaz.IAdministrarInicioKiosko;
import co.com.kiosko.controlador.ingreso.ControladorIngreso;
import co.com.kiosko.utilidadesUI.MensajesUI;
import co.com.kiosko.utilidadesUI.PrimefacesContextUI;
import java.io.*;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;

/**
 *
 * @author Felipe Trivi�o
 */
@ManagedBean
@SessionScoped
public class ControladorInicioKiosko implements Serializable {

    @EJB
    private IAdministrarInicioKiosko administrarInicioKiosko;
    private String usuario;
    private Date ultimaConexionEmpleado;
    //FOTO EMPLEADO
    private FileInputStream fis;
    private StreamedContent fotoEmpleado;
    private StreamedContent logoEmpresa;
    private String pathFoto;
    private BigInteger identificacionEmpleado;
    private String nitEmpresa;
    private String fondoEmpresa;

    public ControladorInicioKiosko() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControladorInicioKiosko.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInicioKiosko.obtenerConexion(ses.getId());
            ultimaConexionEmpleado = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUltimaConexion();
            nitEmpresa = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit();
            pathFoto = administrarInicioKiosko.fotoEmpleado();
            obtenerFotoEmpleado();
            System.out.println("Inicializado");
        } catch (ELException e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void obtenerFotoEmpleado() {
        String formatoFotoEmpleado="image/jpg";
        String rutaFoto = pathFoto + ".jpg";
        if (rutaFoto != null) {
            try {
                fis = new FileInputStream(new File(rutaFoto));
                fotoEmpleado = new DefaultStreamedContent(fis, formatoFotoEmpleado);
            } catch (FileNotFoundException e) {
                try {
                    fis = new FileInputStream(new File(pathFoto + "sinFoto.jpg"));
                    fotoEmpleado = new DefaultStreamedContent(fis, formatoFotoEmpleado);
                    //System.out.println("Foto del empleado no encontrada. \n" + e);
                } catch (FileNotFoundException ex) {
                    System.out.println("ERROR. \n" + ex);
                }
            }
        }
    }

    //SUBIR FOTO EMPLEADO
    public void subirFotoEmpleado(FileUploadEvent event) throws IOException {
        //RequestContext context = RequestContext.getCurrentInstance();
        String extension = event.getFile().getFileName().split("[.]")[1];
        Long tamanho = event.getFile().getSize();
        if (extension.equals("jpg") || extension.equals("JPG")) {
            if (tamanho <= 153600) {
                //identificacionEmpleado = conexionEmpleado.getEmpleado().getPersona().getNumerodocumento();
                transformarArchivo(tamanho, event.getFile().getInputstream());
                obtenerFotoEmpleado();
                PrimefacesContextUI.ejecutar("PF('subirFoto').hide()");
                PrimefacesContextUI.actualizar("principalForm");
                MensajesUI.info("Foto cargada con �xito.");
            } else {
                MensajesUI.error("El tama�o m�ximo permitido es de 150 KB.");
            }
        } else {
            MensajesUI.error("Solo se admiten imagenes con formato (.JPG).");
        }
    }

    public void transformarArchivo(long size, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(pathFoto + identificacionEmpleado + ".jpg"));
            int reader = 0;
            byte[] bytes = new byte[(int) size];
            while ((reader = in.read(bytes)) != -1) {
                out.write(bytes, 0, reader);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("Error: ControladorInicioKiosko.transformarArchivo: " + e);
        }
    }

    public void obtenerLogoEmpresa() {
        String formatoFotoEmpleado="image/png";
        String logo = "";
        String rutaLogo = pathFoto + logo+".png";
        if (rutaLogo != null) {
            try {
                fis = new FileInputStream(new File(rutaLogo));
                logoEmpresa = new DefaultStreamedContent(fis, formatoFotoEmpleado, logo);
            } catch (FileNotFoundException e) {
                try {
                    rutaLogo=pathFoto+"sinLogo.png";
                    fis = new FileInputStream(new File(rutaLogo));
                    logoEmpresa = new DefaultStreamedContent(fis, formatoFotoEmpleado, rutaLogo);
                } catch (FileNotFoundException ex) {
                    System.out.println("ERROR. No se encontro el logo de la empresa. \n");
                    System.out.println("ruta: "+rutaLogo);
                    System.out.println("execption: "+ex);
                }
            }
        }
    }
    /*private void consultaNitEmpresa(){
        nitEmpresa = String.valueOf(conexionEmpleado.getEmpleado().getEmpresa().getNit());
    }*/
    
    public void obtenerFondoEmpresa(){
        //String rutaFondo = null;
        //consultaNitEmpresa();
        for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
            if ( elemento.getNit().equals( nitEmpresa ) ){
                fondoEmpresa = elemento.getFondo();
            }
        }
    }

    //GETTER AND SETTER
    public StreamedContent getFotoEmpleado() {
        if (fotoEmpleado != null) {
            try {
                fotoEmpleado.getStream().available();
            } catch (IOException e) {
                obtenerFotoEmpleado();
            }
        }
        return fotoEmpleado;
    }

    public void setFotoEmpleado(StreamedContent fotoEmpleado) {
        this.fotoEmpleado = fotoEmpleado;
    }

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
        System.out.println("ControladorInicioKiosko.getFondoEmpresa");
        if (fondoEmpresa == null) {
            obtenerFondoEmpresa();
        }
        System.out.println("fondoEmpresa: "+fondoEmpresa);
        return fondoEmpresa;
    }

    public void setFondoEmpresa(String fondoEmpresa) {
        this.fondoEmpresa = fondoEmpresa;
    }

}
