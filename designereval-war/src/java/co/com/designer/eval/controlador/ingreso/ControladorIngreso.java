package co.com.designer.eval.controlador.ingreso;

import co.com.designer.eval.administrar.interfaz.IAdministrarIngreso;
import co.com.designer.eval.clasesAyuda.CadenasConexion;
import co.com.designer.eval.clasesAyuda.LeerArchivoXML;
import co.com.designer.eval.controlador.autenticacion.Util;
import co.com.designer.eval.entidades.Conexiones;
import co.com.designer.eval.entidades.Personas;
import co.com.designer.eval.utilidadesUI.MensajesUI;
import co.com.designer.eval.utilidadesUI.PrimefacesContextUI;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Trivi�o
 */
@ManagedBean
@SessionScoped
public class ControladorIngreso implements Serializable {

    @EJB
    private IAdministrarIngreso administrarIngreso;
    private String usuario, clave, unidadPersistenciaIngreso;
    private Date ultimaConexion;
    private boolean ingresoExitoso;
    private String nit;
    private final String logo;
    private String grupo;
    private List<SelectItem> listaGrupos;
    private String grupoSeleccionado;
    private Personas persona;
    private Conexiones conexion;
    private CadenasConexion cadena;

    public ControladorIngreso() {
        logo = "logonominadesignertrans.png";
    }

    @PostConstruct
    public void inicializarAdministrador() {
    }

    public List<CadenasConexion> obtenerCadenasConexion() {
        return obtenerCadenasConexionGrupo();
    }

    public List<CadenasConexion> obtenerCadenasConexionGrupo() {
        List<CadenasConexion> listaResultado;
        boolean resultadoValidacion = false;
        if ((grupo != null)) {
            if (!grupo.isEmpty()) {
                resultadoValidacion = true;
            }
        }
        if (resultadoValidacion) {
            listaResultado = (new LeerArchivoXML()).leerArchivoEmpresasGrupo(this.grupo);
        } else {
            listaResultado = (new LeerArchivoXML()).leerArchivoEmpresasGrupo("0");
        }
        return listaResultado;
    }

    public List<SelectItem> obtenerGruposCadenasConexion() {
        List<String> listaOriginal = (new LeerArchivoXML()).obtenerGruposEmpresas();
        Collections.sort(listaOriginal);
        List<SelectItem> listaRetorno = new ArrayList<>();
        for (int i = 0; i < listaOriginal.size(); i++) {
            listaRetorno.add(new SelectItem(listaOriginal.get(i), listaOriginal.get(i)));
        }
        return listaRetorno;
    }

    private CadenasConexion validarUnidadPersistencia(String unidadP) {
        CadenasConexion resultado = null;
        for (CadenasConexion elemento : (new LeerArchivoXML()).leerArchivoEmpresasConexion()) {
            if (elemento.getId().equals(unidadP)) {
                resultado = elemento;
                break;
            }
        }
        return resultado;
    }

    public String ingresar() throws IOException {
        String retorno = null;
        FacesContext contexto = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
        try {
            if (!ingresoExitoso) {
                cadena = validarUnidadPersistencia(unidadPersistenciaIngreso);
                usuario = usuario.trim();
                if (usuario != null && !usuario.isEmpty()
                        && clave != null && !clave.isEmpty()
                        && cadena != null) {
                    nit = cadena.getNit();
                    if (administrarIngreso.conexionIngreso(cadena.getCadena())) {
                        //if (administrarIngreso.validarUsuario(usuario)) {
                        persona = administrarIngreso.conexionUsuario(cadena.getCadena(), usuario, clave);
                        if (persona != null) {
                            administrarIngreso.adicionarConexionUsuario(ses.getId());
                            conexion = administrarIngreso.ultimaConexionUsuario(usuario);
                            ultimaConexion = conexion.getUltimaEntrada();
                            guardarUltimaConexion();
                            ingresoExitoso = true;
                            HttpSession session = Util.getSession();
                            session.setAttribute("idUsuario", usuario);
                            imprimir("Conectado a: " + session.getId());
                            retorno = "plantilla";
                        } else {
                            //CONTRASE�A INVALIDA.
                            MensajesUI.error("Contrase�a invalida.");
                            ingresoExitoso = false;
                        }
                        /*} else {
                            //EL USUARIO NO EXISTE O ESTA INACTIVO.
                            MensajesUI.error("El usuario " + usuario + " no existe o esta inactivo, por favor contactar al �rea de soporte.");
                            ingresoExitoso = false;
                        }*/
                    } else {
                        //UNIDAD DE PERSISTENCIA INVALIDA - REVISAR ARCHIVO DE CONFIGURACION
                        MensajesUI.fatal("Unidad de persistencia inv�lida, por favor contactar al �rea de soporte.");
                        ingresoExitoso = false;
                    }
                } else {
                    MensajesUI.error("Todos los campos son de obligatorio ingreso.");
                    ingresoExitoso = false;
                }
            } else {
                usuario = "";
                HttpSession session = Util.getSession();
                System.out.println("la session con " + session.getAttribute("idUsuario") + " termino.");
                session.setAttribute("idUsuario", "");
                session.removeAttribute("idUsuario");
                ingresoExitoso = false;
                nit = null;
                FacesContext context = FacesContext.getCurrentInstance();
                ExternalContext ec = context.getExternalContext();

                try {
                    ec.invalidateSession();
                } catch (Exception npe) {
                    System.out.println("ExternalContext vacio");
                }
                administrarIngreso.cerrarSession(ses.getId());
                if (grupoSeleccionado != null) {
                    ec.redirect(ec.getRequestContextPath() + "/" + "?grupo=" + grupoSeleccionado);
                } else {
                    ec.redirect(ec.getRequestContextPath());
                }
            }
        } catch (EJBTransactionRolledbackException etre) {
            System.out.println(this.getClass().getName() + ".ingresar() exception");
            System.out.println("La transacci�n se deshizo.");
            System.out.println(etre);
        }
        return retorno;
    }

    public String entrar() throws IOException {
        String retorno;
        if (ingresoExitoso) {
            retorno = "plantilla";
        } else {
            try {
                retorno = ingresar();
            } catch (IOException ioe) {
                throw ioe;
            }
        }
        return retorno;
    }

    public boolean validarGrupo() {
        boolean respuesta;
        if ((this.grupo == null) || (this.grupo.isEmpty())) {
            PrimefacesContextUI.ejecutar("PF('dlgSolicitudGrupo').show();");
            respuesta = false;
        } else {
            this.grupoSeleccionado = this.grupo;
            respuesta = true;
        }
        return respuesta;
    }

    public void obtenerParametroURL() {
        String ruta;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        imprimir("aplicacion: " + ec.getRequestContextPath());
        ruta = ec.getRequestContextPath() + "/" + "?grupo=" + grupoSeleccionado;
        imprimir("ruta: " + ruta);
        try {
            ec.redirect(ruta);
        } catch (IOException ex) {
            imprimir("error al redireccionar");
        }
    }

    public void guardarUltimaConexion() throws UnknownHostException {
        FacesContext contextoF = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) (contextoF.getExternalContext().getRequest());
        String Ip, nombreEquipo;
        java.net.InetAddress localMachine;
        if (request.getRemoteAddr().startsWith("127.0.0.1")) {
            localMachine = java.net.InetAddress.getLocalHost();
            Ip = localMachine.getHostAddress();
        } else {
            Ip = request.getRemoteAddr();
        }
        localMachine = java.net.InetAddress.getByName(Ip);
        nombreEquipo = localMachine.getHostName();
        Conexiones registroConexion = new Conexiones();
        registroConexion.setDireccionIP(Ip);
        registroConexion.setEstacion(nombreEquipo);
        registroConexion.setSecuencia(BigInteger.valueOf(1));
        registroConexion.setUltimaEntrada(new Date());
        registroConexion.setUsuarioso(System.getProperty("os.name") + " / " + System.getProperty("user.name"));
        registroConexion.setUsuarioBD(usuario);
        registroConexion.setSid(BigInteger.ZERO);
        administrarIngreso.insertarUltimaConexion(registroConexion);
    }

    //GETTER AND SETTER
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUnidadPersistenciaIngreso() {
        List<CadenasConexion> cadenas = obtenerCadenasConexion();
        unidadPersistenciaIngreso = (cadenas.size() == 1) ? cadenas.get(0).getId() : null;
        return unidadPersistenciaIngreso;
    }

    public void setUnidadPersistenciaIngreso(String unidadPersistenciaIngreso) {
        this.unidadPersistenciaIngreso = unidadPersistenciaIngreso;
    }

    public boolean isIngresoExitoso() {
        return ingresoExitoso;
    }

    public Date getUltimaConexion() {
        return ultimaConexion;
    }

    public String getNit() {
        return nit;
    }

    public String getLogo() {
        return logo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
        //validarGrupo();
    }

    public String getGrupoSeleccionado() {
        imprimir("getGrupoSeleccionado: " + this.grupoSeleccionado);
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(String grupoSeleccionado) {
        imprimir("setGrupoSeleccionado: " + grupoSeleccionado);
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public List<SelectItem> getListaGrupos() {
        imprimir("getListaGrupos");
        listaGrupos = obtenerGruposCadenasConexion();
        return listaGrupos;
    }

    public void setListaGrupos(List<SelectItem> listaGrupos) {
        imprimir("setListaGrupos");
        this.listaGrupos = listaGrupos;
    }

    private void imprimir(String mensajeConsola) {
        if (true) {
            System.out.println(mensajeConsola);
        }
    }

    public Personas getPersona() {
        return persona;
    }

    public CadenasConexion getCadena() {
        return cadena;
    }

    public void setCadena(CadenasConexion cadena) {
        this.cadena = cadena;
    }
}
