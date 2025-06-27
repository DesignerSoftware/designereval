package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarEvaluacion;
import co.com.designer.eval.controlador.ingreso.ControladorIngreso;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.entidades.Respuestas;
import co.com.designer.eval.utilidadesUI.MensajesUI;
import co.com.designer.eval.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.math.BigDecimal;
//import java.io.*;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 */
@ManagedBean
@SessionScoped
public class ControladorEvaluacion implements Serializable {

    @EJB
    private IAdministrarEvaluacion administrarEvaluacion;
    private List<Preguntas> preguntas;

    //Informacion general
    private String evaluado, evaluador, convocatoria, prueba, observacionEvaluador;
    private BigInteger nroPreguntas, secIndigacion, secPrueba, secConvocatoria, secEvaluado;
    private BigDecimal puntajeMaximo;
    private Pruebas pruebaActual;
    private Convocatorias convocatoriaActual;
    private Evaluados evaluadoActual;
    private Integer agrupado;
    private boolean tieneRespuestas, observacionObligatoria;
    private double puntaje;
    private double porcentaje;

    public ControladorEvaluacion() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControladorEvaluacion.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEvaluacion.obtenerConexion(ses.getId());
            System.out.println("Inicializado");
        } catch (ELException e) {
            System.out.println(this.getClass().getName() + ".inicializarAdministrador" + ": " + e);
        }
    }

    public void cargarPreguntas() {
        System.out.println("ControladorEvaluacion.cargarPreguntas()");
        FacesContext x = FacesContext.getCurrentInstance();
        evaluadoActual = (Evaluados) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(0);
        evaluado = evaluadoActual.getNombrePersona();
        evaluador = (String) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(1);
        convocatoriaActual = (Convocatorias) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(2);
        convocatoria = convocatoriaActual.getCodigo() + " - " + convocatoriaActual.getEnfoque();
        pruebaActual = ((Pruebas) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(3));
        observacionObligatoria = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getCadena().isObservacion();
        prueba = pruebaActual.getPrueba();
        observacionEvaluador = pruebaActual.getObsEvaluador();
        secIndigacion = pruebaActual.getSecuencia();
        secConvocatoria = convocatoriaActual.getSecuencia();
        agrupado = convocatoriaActual.getAgrupado();
        secEvaluado = evaluadoActual.getSecuencia();
        this.secPrueba = pruebaActual.getSecPrueba();
        cargarDetallePreguntas();
    }

    public void cargarDetallePreguntas() {
        System.out.println("ControladorEvaluacion.cargarDetallePreguntas()");
        preguntas = administrarEvaluacion.obtenerCuestinonario(secPrueba, secIndigacion);
        nroPreguntas = administrarEvaluacion.obtenerNroPreguntas(secPrueba);
        validarSiExistenRespuestas();
        obtenerPuntajeMaximo();
        calcularPuntajePorcentaje();
        for (Preguntas pregunta : preguntas) {
            for (Respuestas respuesta : pregunta.getRespuestas()) {
                System.out.println("res_4: " + respuesta.getCuantitativo() + " ; " + respuesta.getCualitativo() + " ; " + respuesta.getDescripcion());
            }
        }
    }

    public void enviarRespuestas() {
        System.out.println("ControladorEvaluacion.enviarRespuestas()");
        boolean todas = true;
        for (Preguntas pregunta : preguntas) {
            if (pregunta.getRespuesta() == null) {
                todas = false;
                break;
            }
        }
        if (todas) {
            observacionEvaluador = observacionEvaluador.trim();
            if ((observacionObligatoria && (observacionEvaluador != null && !observacionEvaluador.isEmpty()))) {
                if ((observacionEvaluador.length() > 30 && observacionEvaluador.length() < 500)
                        || observacionEvaluador.length() == 30 || observacionEvaluador.length() == 500) {
                    boolean error = true;
                    
                    if (administrarEvaluacion.registrarRespuestasPuntos(preguntas, secIndigacion,
                            observacionEvaluador, porcentaje,
                            secConvocatoria, secEvaluado, agrupado, evaluadoActual.getEmpleado(), this.convocatoriaActual.getEvalVigConvocatoria())) {
                        PrimefacesContextUI.ejecutar("PF('envioExitoso').show()");
                    } else {
                        MensajesUI.error("No fue posible registrar la evaluación. Inténtelo de nuevo por favor.");
                    }
                } else {
                    MensajesUI.error("La observación debe tener al menos 30 caracteres pero sin pasar de 500.");
                }
            } else {
                MensajesUI.error("La observación es obligatoria.");
            }
        } else {
            MensajesUI.error("Antes de enviar la evaluación debe responder todas las preguntas.");
        }
    }

    public void eliminarRespuestas() {
        System.out.println("ControladorEvaluacion.eliminarRespuestas()");
        observacionEvaluador = null;
        /*if (administrarEvaluacion.eliminarRespuestas(secIndigacion)
                && administrarEvaluacion.actualizarPorcentaje(secIndigacion, observacionEvaluador, 0)
                && administrarEvaluacion.actualizarPorcentaje(secConvocatoria, secEvaluado, agrupado)) {*/
        if (administrarEvaluacion.reiniciarEvaluacion(secIndigacion, observacionEvaluador, 0,
                secConvocatoria, secEvaluado, agrupado)) {
            MensajesUI.info("Respuestas eliminadas exitosamente.");
        } else {
            MensajesUI.error("No fue posible eliminar las respuestas.");
        }
        cargarDetallePreguntas();
    }

    public void validarSiExistenRespuestas() {
        System.out.println("ControladorEvaluacion.validarSiExistenRespuestas()");
        tieneRespuestas = false;
        for (Preguntas pregunta : preguntas) {
//            if (!pregunta.isNuevo()) {
            if (pregunta.getRespuesta() != null) {
                tieneRespuestas = true;
                break;
            }
        }
    }

    public void obtenerPuntajeMaximo() {
        System.out.println("ControladorEvaluacion.obtenerPuntajeMaximo()");
        /*
        puntajeMaximo = BigDecimal.ZERO;
        for (Preguntas pregunta : preguntas) {
            BigDecimal inicio = BigDecimal.ZERO;
            for (Respuestas respuesta : pregunta.getRespuestas()) {
                if (respuesta.getCuantitativo().compareTo(inicio) == 1) {
                    inicio = respuesta.getCuantitativo();
                }
            }
            puntajeMaximo = puntajeMaximo.add(inicio);
        }
         */
        this.puntajeMaximo = pruebaActual.getIdeal();
    }

    public void calcularPuntajePorcentaje() {
        System.out.println("ControladorEvaluacion.calcularPuntajePorcentaje()");
        double puntajeInt = 0l;
        porcentaje = 0;
        for (Preguntas pregunta : preguntas) {
            if (pregunta.getRespuesta() != null) {
                for (Respuestas respuesta : pregunta.getRespuestas()) {
                    if (respuesta.getSecuencia().compareTo(pregunta.getRespuesta()) == 0) {
                        puntajeInt = puntajeInt + respuesta.getCuantitativo().doubleValue();
                        break;
                    }
                }
            }
        }
        this.puntaje = (double) Math.round((puntajeInt * 10000)) / 10000;
        porcentaje = (puntajeInt / puntajeMaximo.doubleValue()) * 100;
        porcentaje = (double) Math.round((porcentaje * 10000)) / 10000;
    }

    //GETTER AND SETTER
    public List<Preguntas> getPreguntas() {
        System.out.println("ControladorEvaluacion.getPreguntas()");
        return preguntas;
    }

    public String getEvaluado() {
        System.out.println("ControladorEvaluacion.getEvaluado()");
        return evaluado;
    }

    public String getEvaluador() {
        System.out.println("ControladorEvaluacion.getEvaluador()");
        return evaluador;
    }

    public String getConvocatoria() {
        System.out.println("ControladorEvaluacion.getConvocatoria()");
        return convocatoria;
    }

    public BigInteger getNroPreguntas() {
        System.out.println("ControladorEvaluacion.getNroPreguntas()");
        return nroPreguntas;
    }

    public BigDecimal getPuntajeMaximo() {
        System.out.println("ControladorEvaluacion.getPuntajeMaximo()");
        return puntajeMaximo;
    }

    public String getPrueba() {
        System.out.println("ControladorEvaluacion.getPrueba()");
        return prueba;
    }

    public String getObservacionEvaluador() {
        System.out.println("ControladorEvaluacion.getObservacionEvaluador()");
        return observacionEvaluador;
    }

    public void setObservacionEvaluador(String observacionEvaluador) {
        System.out.println("ControladorEvaluacion.setObservacionEvaluador()");
        this.observacionEvaluador = observacionEvaluador;
    }

    public boolean isTieneRespuestas() {
        System.out.println("ControladorEvaluacion.isTieneRespuestas()");
        return tieneRespuestas;
    }

    public boolean isHistorica(String tipo) {
        System.out.println("ControladorEvaluacion.isHistorica()" + " tipo: " + tipo);
        for (Preguntas preg : preguntas) {
            int cont = 1;
            System.out.println(cont + ": " + preg.getTipo());
            cont++;
        }
        return "HISTORICA".equalsIgnoreCase(tipo);
    }

    public boolean mostrarPosiblesRespuestas(Preguntas pregunta) {
        System.out.println("ControladorEvaluacion.mostrarPosiblesRespuestas()" + " respuesta: " + pregunta.getRespuesta());
        boolean isMostrar = true;
        if ("HISTORICA".equalsIgnoreCase(pregunta.getTipo())) {
            if (pregunta.getRespuesta() != null && !pregunta.getRespuesta().equals(BigInteger.ZERO)) {
                isMostrar = true;
            } else {
                isMostrar = false;
            }
        } else {
            isMostrar = true;
        }
        return isMostrar;
    }

    public void asignarPuntajeHistorico() {
        System.out.println("ControladorEvaluacion.asignarPuntajeHistorico()");
        boolean cnContinuar = false;
        for (Preguntas pregunta : preguntas) {
            System.out.println("pregunta: " + pregunta.getSecuencia() + " " + pregunta.getTipo());
            System.out.println("Cantidad respuestas: " + pregunta.getRespuestas());
            if ("HISTORICA".equalsIgnoreCase(pregunta.getTipo())) {
                for (Respuestas respuesta : pregunta.getRespuestas()) {
                    System.out.println("respuesta: " + respuesta);
                    if (respuesta.getCuantitativo().compareTo(BigDecimal.ZERO) == 0) {
                        BigDecimal valorHistorico = administrarEvaluacion.consultarEvaluacionHistorica(evaluadoActual.getEmpleado(), this.convocatoriaActual.getSecuencia(), this.convocatoriaActual.getEvalVigConvocatoria(), this.secIndigacion, pregunta);
                        System.out.println("valorHistorico: "+valorHistorico);
                        if (valorHistorico.compareTo(new BigDecimal("-1")) > 0) {
                            respuesta.setCuantitativo(valorHistorico);
                            pregunta.setRespuesta(respuesta.getSecuencia());
                            MensajesUI.info("Consulta realizada exitosamente.");
                            cnContinuar = true;
                        } else {
                            System.out.println("Error " + "ControladorEvaluacion.asignarPuntajeHistorico()" + "No asigno la respuesta");
                            MensajesUI.error("No fue posible consultar la evaluación anterior.");
                        }
                    }
                }
            }
        }
        //cargarDetallePreguntas();
    }

    public double getPuntaje() {
        System.out.println("ControladorEvaluacion.getPuntaje()");
        return puntaje;
    }

    public double getPorcentaje() {
        System.out.println("ControladorEvaluacion.getPorcentaje()");
        return porcentaje;
    }

    public boolean isObservacionObligatoria() {
        System.out.println("ControladorEvaluacion.isObservacionObligatoria()");
        return observacionObligatoria;
    }

    public Pruebas getPruebaActual() {
        System.out.println("ControladorEvaluacion.getPruebaActual()");
        return pruebaActual;
    }

}
