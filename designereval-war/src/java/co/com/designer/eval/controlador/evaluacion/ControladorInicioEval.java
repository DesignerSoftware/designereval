package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.administrar.interfaz.IAdministrarPlanDesarrollo;
import co.com.designer.eval.clasesAyuda.ExtraeCausaExcepcion;
import co.com.designer.eval.controlador.ingreso.ControladorIngreso;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.utilidadesUI.MensajesUI;
import co.com.designer.eval.utilidadesUI.PrimefacesContextUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@ManagedBean
@SessionScoped
public class ControladorInicioEval implements Serializable {

    @EJB
    private IAdministrarInicio administrarInicio;
    private String usuario, nitEmpresa, email, pathReporteGenerado;
    private List<Convocatorias> convocatorias;
    private List<Evaluados> evaluados;
    private List<Pruebas> pruebas;
    private BigDecimal secuenciaEvaluador, totalEmpleadosAsignados, empleadosConvocados, empleadosAsignados, empleadosEvaluados;
    private BigDecimal secConvocatoria;
    private BigDecimal secEvaluado;
    private int estadoConvocatoria = 2;

    //SELECCION
    private Convocatorias convocatoria;
    private Evaluados evaluado;
    private Pruebas prueba;

    //private Convocatorias convocatoria;
    
    private String pruebastr="Hello world";
    
    //módulo evaluaciones
    private List<EvalPlanesDesarrollos> evalPlanesDesarrollos;
    @EJB
    private IAdministrarPlanDesarrollo administrarPlanDesarrollo;
            
    public ControladorInicioEval() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControladorInicioEval.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarInicio.obtenerConexion(ses.getId());
            usuario = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUsuario();
            convocatorias = administrarInicio.obtenerConvocatorias(usuario);
            secuenciaEvaluador = administrarInicio.obtenerSecuenciaEvaluador(usuario);
            if (secuenciaEvaluador != null) {
                totalEmpleadosAsignados = administrarInicio.totalEmpleadosEvaluador(secuenciaEvaluador.toBigInteger());
            }
            nitEmpresa = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getNit();
            email = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getPersona().getEmail();
            System.out.println("INICIALIZADO!!!!  " + ses.getId());
        } catch (ELException e) {
            System.out.println(this.getClass().getName() + ".inicializarAdministrador" + ": " + e);
        }
    }

    //EVENTOS
    public void seleccionConvocatoria(int tipo) {
        //1 Si - 0 No
        switch (tipo) {
            case 1:
                evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoria.getSecuencia());
                empleadosConvocados = administrarInicio.cantidadEvaluadosConvocatoria(convocatoria.getSecuencia());
                empleadosAsignados = administrarInicio.totalEmpleadosEvaluadorConvocatoria(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                empleadosEvaluados = administrarInicio.cantidadEvaluados(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                evaluado = null;
                secConvocatoria = new BigDecimal(convocatoria.getSecuencia());
                break;
            case 2:
                evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoria.getSecuencia());
                empleadosConvocados = administrarInicio.cantidadEvaluadosConvocatoria(convocatoria.getSecuencia());
                empleadosAsignados = administrarInicio.totalEmpleadosEvaluadorConvocatoria(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                empleadosEvaluados = administrarInicio.cantidadEvaluados(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
                secConvocatoria = new BigDecimal(convocatoria.getSecuencia());
                break;
            default:
                evaluados = null;
                evaluado = null;
                empleadosConvocados = null;
                empleadosAsignados = null;
                empleadosEvaluados = null;
                secConvocatoria = null;
                break;
        }
        pruebas = null;
    }

    public void seleccionEvaluado(int tipo) {
        //1 Si - 0 No
        if (tipo == 1 && evaluado != null) {
            pruebas = administrarInicio.obtenerPruebasEvaluado(usuario, evaluado.getSecuencia());
            //evaluado.setConsolidado(administrarInicio.estaConsolidado(evaluado.getEvalConvocatoria(), evaluado.getSecuencia()));
            this.secEvaluado = new BigDecimal(evaluado.getSecuencia());
        } else {
            pruebas = null;
            this.secEvaluado = null;
        }
    }
    
    public void seleccionPlanDesa() { //Validar si este metodo debe ir en ControladorPlanDesarrollo
            //evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoria.getSecuencia());
            //empleadosConvocados = administrarInicio.cantidadEvaluadosConvocatoria(convocatoria.getSecuencia());
            //empleadosAsignados = administrarInicio.totalEmpleadosEvaluadorConvocatoria(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
            //empleadosEvaluados = administrarInicio.cantidadEvaluados(secuenciaEvaluador.toBigInteger(), convocatoria.getSecuencia());
            //secConvocatoria = new BigDecimal(convocatoria.getSecuencia());
            //pruebas = administrarInicio.obtenerPruebasEvaluado(usuario, evaluado.getSecuencia());
            //evaluado.setConsolidado(administrarInicio.estaConsolidado(evaluado.getEvalConvocatoria(), evaluado.getSecuencia()));
            evalPlanesDesarrollos=administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluado.getSecuencia());
            System.out.println("evaluado.getSecuencia() "+evaluado.getSecuencia());
            this.secEvaluado = new BigDecimal(evaluado.getSecuencia());
            System.out.println("ControladorInicioEval.seleccionPlanDesa()");
    }

    public Object obtenerInformacion(int opcion) {
        /*
        0 - Evaluado
        1 - Evaluador
        2 - Convocatoria
        3 - Prueba
        4 - usuario
        d - null
         */
        FacesContext x = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
        switch (opcion) {
            case 0:
                return evaluado;
            case 1:
//                FacesContext x = FacesContext.getCurrentInstance();
//                HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
                return ((ControladorInformacionBasica) x.getApplication().evaluateExpressionGet(x, "#{controladorInformacionBasica}", ControladorInformacionBasica.class
                )).getPersona().getNombreCompleto();
            case 2:
                return convocatoria;
            case 3:
                return prueba;
            case 4:
                return ((ControladorInformacionBasica) x.getApplication().evaluateExpressionGet(x, "#{controladorInformacionBasica}", ControladorInformacionBasica.class
                )).getUsuario();
            default:
                return null;
        }
    }

    public void seleccion() {
        int index = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valor"));
        String i = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
        if (i.equals("0")) {
            convocatoria = convocatorias.get(index);
            PrimefacesContextUI.ejecutar("seleccionConvocatoria();");
        } else if (i.equals("1")) {
            evaluado = evaluados.get(index);
            PrimefacesContextUI.ejecutar("seleccionEvaluado();");
        } else if (i.equals("2")) {
            prueba = pruebas.get(index);
            PrimefacesContextUI.ejecutar("seleccionPrueba();");
        } else if (i.equals("3")) {
            secConvocatoria = new BigDecimal(convocatorias.get(index).getSecuencia());
            PrimefacesContextUI.ejecutar("PF('alertaCC').show();");
        } else if (i.equals("4")) {
            secConvocatoria = new BigDecimal(convocatorias.get(index).getSecuencia());
            PrimefacesContextUI.ejecutar("PF('opcionesReporteCerrar').show();");
        } else if (i.equals("5")) {
            System.out.println("Empleado seleccionado en tipo 5");
            evaluado = evaluados.get(index);
            System.out.println("evaluado: " + evaluado.getNombrePersona());
            System.out.println("consolidado: " + evaluado.getConsolidado());
            System.out.println("booleano: " + evaluado.isEsConsolidado());
            secEvaluado = new BigDecimal(evaluado.getSecuencia());
            PrimefacesContextUI.ejecutar("PF('opcionesReporteEvaluado').show();");
        } else if(i.equals("6")){
            evaluado = evaluados.get(index);
            //System.out.println("evaluado para abrir plan de desarrollo: " + evaluado.getNombrePersona());
            //prueba = pruebas.get(index);
            secConvocatoria = new BigDecimal(convocatorias.get(index).getSecuencia());
            secEvaluado = new BigDecimal(evaluado.getSecuencia());
            evalPlanesDesarrollos=administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluado.getSecuencia());
            PrimefacesContextUI.ejecutar("seleccionPlan()");
        }
        else {
            prueba = pruebas.get(index);
            cambiarEstado(prueba.getSecuencia(), prueba.getEstado());
        }
    }

//    public void obtenerSecuenciaConvocatoria(BigInteger sec) {
    public void obtenerSecuenciaConvocatoria(BigDecimal sec) {
        System.out.println("obtenerSecuenciaConvocatoria");
        secConvocatoria = sec;
        System.out.println("obtenerSecuenciaConvocatoria-sec: " + sec);
    }

    public void obtenerSecuenciaEvaluado(BigDecimal sec) {
        System.out.println("obtenerSecuenciaEvaluado");
        secConvocatoria = new BigDecimal(convocatoria.getSecuencia());
        secEvaluado = sec;
        System.out.println("convocatoria: " + secConvocatoria);
        System.out.println("Evaluado: " + secEvaluado);
    }

    private void reiniciarDatos() {
        convocatoria = null;
        convocatorias = administrarInicio.obtenerConvocatorias(usuario);
        evaluados = null;
        evaluado = null;
        empleadosConvocados = null;
        empleadosAsignados = null;
        empleadosEvaluados = null;
        pruebas = null;
    }

    public void cerrarEvaluaciones() {
        System.out.println(".cerrarEvaluaciones()");
        try {
            secConvocatoria = new BigDecimal(convocatoria.getSecuencia());
            System.out.println("Secuencia de la convocatoria: " + secConvocatoria);
            if (administrarInicio.cerrarEvaluaciones(secConvocatoria, this.usuario)) {
                //MensajesUI.info("Convocatoria cerrada exitosamente.");
//                reiniciarDatos();
                PrimefacesContextUI.ejecutar("PF('opcionesReporteCerrar').show()");
            } else {
                MensajesUI.error("Error al cerrar la convocatoria.");
            }
        } catch (Exception e) {
            MensajesUI.error(ExtraeCausaExcepcion.obtenerMensajeSQLException(e));
        }
        System.out.println(this.getClass().getName() + ".cerrarEvaluaciones");
        System.out.println("cerrarEvaluaciones-convocatoria: " + convocatoria);
        System.out.println("cerrarEvaluaciones-secConvocatoria: " + secConvocatoria);
    }

    public void cerrarConvocatoria() {
        String resultado = "";
        try {
            resultado = administrarInicio.cerrarConvocatoria(secConvocatoria, this.usuario);
            reiniciarDatos();
            MensajesUI.info(resultado);
            //PrimefacesContextUI.ejecutar("PF('opcionesReporteCerrar').show()");
            /*ArrayList lista = new ArrayList();
            lista.add(":principalForm:dtbConvocatorias");
            lista.add(":principalForm:dtbEvaluados");
            lista.add(":principalForm:informacion");
            lista.add(":principalForm:dtbPruebas");
            PrimefacesContextUI.actualizarLista(lista);*/
        } catch (Exception ex) {
            MensajesUI.error(ExtraeCausaExcepcion.obtenerMensajeSQLException(ex));
        }
    }

    public void generarReporteHistoEval(int codReporte) {
        try {
            envioCorreoCierreConvocatoria(codReporte);
            MensajesUI.info("Convocatoria cerrada exitosamente.");
        } catch (Exception e) {
            MensajesUI.error(ExtraeCausaExcepcion.obtenerMensajeSQLException(e));
        }
    }

    public void cargarConvocatorias() {
        if (estadoConvocatoria == 1) {
            convocatorias = administrarInicio.obtenerConvocatoriasAlcance(usuario);
        } else {
            convocatorias = administrarInicio.obtenerConvocatorias(usuario);
        }
        convocatoria = null;
        evaluados = null;
        evaluado = null;
        empleadosConvocados = null;
        empleadosAsignados = null;
        empleadosEvaluados = null;
        pruebas = null;
    }

    public void refrescarListas() {
        seleccionConvocatoria(2);
        seleccionEvaluado(1);
    }

    public void envioCorreoCierreConvocatoria(int codReporte) {
        if (convocatoria != null && convocatoria.getSecuencia() != null) {
            Convocatorias c = convocatoria;
            if (email != null && !email.isEmpty()) {
                if (c != null) {
                    secConvocatoria = new BigDecimal(c.getSecuencia());
                    generarReporte(c, codReporte);
                    if (pathReporteGenerado != null) {
                        String resul = "";
                        boolean resbl1 = false;
                        boolean resbl2 = false;
                        resbl1 = administrarInicio.enviarCorreo(nitEmpresa, email,
                                "Reporte Evaluación Competencias - " + c.getCodigo(), "Mensaje enviado automáticamente, por favor no responda a este correo.",
                                pathReporteGenerado);
                        if (resbl1) {
                            resul = resul + "Se ha enviado un reporte con los resultados de la convocatoria a la dirección de correo del usuario conectado.";
                        } else {
                            resul = resul + "No fue posible enviar el reporte consolidado de la convocatoria seleccionada a la dirección de correo del usuario conectado.";
                        }
                        resbl2 = administrarInicio.enviarCorreo(nitEmpresa, this.evaluado.geteMail(),
                                "Reporte Evaluación Competencias - " + c.getCodigo(), "Mensaje enviado automáticamente, por favor no responda a este correo.",
                                pathReporteGenerado);
                        if (resbl2) {
                            resul = resul + " Se ha enviado un reporte con los resultados de la convocatoria a la dirección de correo del empleado evaluado.";
                        } else {
                            resul = resul + " No fue posible enviar el reporte consolidado de la convocatoria seleccionada a la dirección de correo del empleado evaluado.";
                        }
                        if (resbl1 && resbl2) {
                            MensajesUI.info(resul);
                        } else {
                            MensajesUI.error(resul + " Comuníquese con soporte.");
                        }
                    } else {
                        //MensajesUI.error("No fue posible enviar el reporte consolidado de la convocatoria que acaba de cerrar, por favor comuníquese con soporte.");
                        MensajesUI.error("No fue posible generar el reporte consolidado de la convocatoria seleccionada, por favor comuníquese con soporte.");
                    }
                } else {
                    MensajesUI.error("Error al intentar obtener la convocatoria, por favor comuníquese con soporte.");
                }
            } else {
                MensajesUI.error("No es posible enviar el correo de confirmacion, ya que no esta configurado el correo de destino.");
            }
        } else {
            System.out.println("Secuencia de la convocatoria nula.");
            MensajesUI.error("Secuencia de la convocatoria nula..");
            //PrimefacesContextUI.ejecutar("PF('estadoReporte').hide();");
        }
        PrimefacesContextUI.ejecutar("PF('estadoReporte').hide();");
    }

    public void descargarReporte(int codReporte) {
        System.out.println("descargarReporte");
        System.out.println("descargarReporte-convocatoria: " + convocatoria);
        System.out.println("descargarReporte-convocatoria: " + secConvocatoria);
        if (convocatoria != null && convocatoria.getSecuencia() != null) {
            Convocatorias c = convocatoria;
            if (c != null) {
                secConvocatoria = new BigDecimal(c.getSecuencia());
                generarReporte(c, codReporte);
                if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error: INICIARREPORTE")) {
                    PrimefacesContextUI.ejecutar("setTimeout(function(){ document.getElementById('principalForm:descargarReporte').click(); }, 2000);");
                } else {
                    MensajesUI.error("Error al generar el reporte, por favor comuníquese con soporte.");
                    PrimefacesContextUI.ejecutar("PF('estadoReporte').hide();");
                }
            } else {
                MensajesUI.error("Error al intentar obtener la convocatoria, por favor comuníquese con soporte.");
                PrimefacesContextUI.ejecutar("PF('estadoReporte').hide();");
            }
        } else {
            System.out.println("Secuencia de la convocatoria nula.");
            PrimefacesContextUI.ejecutar("PF('estadoReporte').hide();");
        }
        System.out.println(this.getClass().getName()+"descargarReporte");
        System.out.println("descargarReporte-convocatoria: " + convocatoria);
        System.out.println("descargarReporte-convocatoria: " + secConvocatoria);
    }

    public void generarReporte(Convocatorias c, int codReporte) {
        Map parametros = new HashMap();
        System.out.println(this.getClass().getName()+"generarReporte");
        System.out.println("generarReporte-secConvocatoria: " + secConvocatoria);
        System.out.println("generarReporte-secEvaluado: " + secEvaluado);
        System.out.println("generarReporte-codReporte: " + codReporte);
        switch (codReporte) {
            case 1: //Se usa para generar reporte por convocatoria
                parametros.put("secuenciaconvocatoria", secConvocatoria);
                parametros.put("aliasEvaluador", this.usuario);
                pathReporteGenerado = administrarInicio.generarReporte("evalcompetenciacerrada", "PDF", parametros, c.getCodigo());
                if (pathReporteGenerado == null) {
                    MensajesUI.error("El reporte por evaluador de la convocatoria no se pudo generar.");
                }
                reiniciarDatos();
                break;
            case 2: //Se usa para generar reporte por empleado
                parametros.put("secuenciaconvocatoria", secConvocatoria);
                parametros.put("secEvaluado", secEvaluado);
                parametros.put("aliasEvaluador", this.usuario);
                pathReporteGenerado = administrarInicio.generarReporte("evalconvevaluado", "PDF", parametros, c.getCodigo());
                if (pathReporteGenerado == null) {
                    MensajesUI.error("El reporte por evaluador de la convocatoria no se pudo generar.");
                }
                break;
            case 3: //Se usar para generar el reporte consolidado de todos los evaluadores de la convocatoria
                parametros.put("secuenciaconvocatoria", secConvocatoria);
                parametros.put("secEvaluado", secEvaluado);
                parametros.put("aliasEvaluador", this.usuario);
                pathReporteGenerado = administrarInicio.generarReporte("evalconvdetallado", "PDF", parametros, c.getCodigo());
                if (pathReporteGenerado == null) {
                    MensajesUI.error("El reporte consolidado por persona de la convocatoria no se pudo generar.");
                }
                break;
            case 4:
                parametros.put("secuenciaconvocatoria", secConvocatoria);
                parametros.put("secEvaluado", secEvaluado);
                parametros.put("aliasEvaluador", this.usuario);
                pathReporteGenerado = administrarInicio.generarReporte("evalconvresumido", "PDF", parametros, c.getCodigo());
                if (pathReporteGenerado == null) {
                    MensajesUI.error("El reporte resumido por persona de la convocatoria no se pudo generar.");
                }
                break;
            default:

        }
    }

    public void cambiarEstado(BigInteger secPrueba, String estado) {
        administrarInicio.actualizarEstado(secPrueba, estado);
    }

    public void cambiarEstadoMobil(int index) {
        prueba = pruebas.get(index);
        administrarInicio.actualizarEstado(prueba.getSecuencia(), prueba.getEstado());
    }

    public void descarga() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        File f = new File(pathReporteGenerado);
        ec.responseReset();
        ec.setResponseContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.
        OutputStream output = ec.getResponseOutputStream();

        byte[] buf = new byte[2048];
        InputStream is = new FileInputStream(f);
        int c = 0;

        while ((c = is.read(buf, 0, buf.length)) > 0) {
            output.write(buf, 0, c);
            output.flush();
        }

        output.close();
        is.close();

        fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
    }

    //GETTER AND SETTER
    public List<Convocatorias> getConvocatorias() {
        return convocatorias;
    }

    public List<Evaluados> getEvaluados() {
        return evaluados;
    }

    public BigDecimal getTotalEmpleadosAsignados() {
        return totalEmpleadosAsignados;
    }

    public BigDecimal getEmpleadosConvocados() {
        return empleadosConvocados;
    }

    public BigDecimal getEmpleadosAsignados() {
        return empleadosAsignados;
    }

    public BigDecimal getEmpleadosEvaluados() {
        return empleadosEvaluados;
    }

    public Convocatorias getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Convocatorias convocatoria) {
        this.convocatoria = convocatoria;
    }

    public Evaluados getEvaluado() {
        return evaluado;
    }

    public void setEvaluado(Evaluados evaluado) {
        this.evaluado = evaluado;
    }

    public List<Pruebas> getPruebas() {
        return pruebas;
    }

    public Pruebas getPrueba() {
        return prueba;
    }

    public void setPrueba(Pruebas prueba) {
        this.prueba = prueba;
    }

    public int getEstadoConvocatoria() {
        return estadoConvocatoria;
    }

    public void setEstadoConvocatoria(int estadoConvocatoria) {
        this.estadoConvocatoria = estadoConvocatoria;
    }
    
    public String getPruebastr(){
        return this.pruebastr;
    }
    
    public void setPruebastr(String str){
        this.pruebastr=str;
    }

    public BigDecimal getSecConvocatoria() {
        return secConvocatoria;
    }

    public void setSecConvocatoria(BigDecimal secConvocatoria) {
        this.secConvocatoria = secConvocatoria;
    }
    
    //Modulo plan de desarrollo

    public List<EvalPlanesDesarrollos> getPlanes(){
        return evalPlanesDesarrollos;
    }

    
    
}
