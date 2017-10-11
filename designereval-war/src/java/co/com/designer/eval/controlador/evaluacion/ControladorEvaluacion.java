package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarEvaluacion;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.entidades.Respuestas;
import co.com.designer.eval.utilidadesUI.MensajesUI;
import co.com.designer.eval.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
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
 * @author Felipe Trivi�o
 */
@ManagedBean
@SessionScoped
public class ControladorEvaluacion implements Serializable {

    @EJB
    private IAdministrarEvaluacion administrarEvaluacion;
    private List<Preguntas> preguntas;

    //Informacion general
    private String evaluado, evaluador, convocatoria, prueba, observacionEvaluador;
    private BigInteger nroPreguntas, puntajeMaximo, secIndigacion, secPrueba, secConvocatoria, secEvaluado;
    private Pruebas pruebaActual;
    private Convocatorias convocatoriaActual;
    private Evaluados evaluadoActual;
    private Integer agrupado;
    private boolean tieneRespuestas;
    private int puntaje;
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
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void cargarPreguntas() {
        FacesContext x = FacesContext.getCurrentInstance();
        evaluadoActual = (Evaluados) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(0);
        evaluado = evaluadoActual.getNombrePersona();
        evaluador = (String) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(1);
        convocatoriaActual = (Convocatorias) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(2);
        convocatoria = convocatoriaActual.getCodigo() + " - " + convocatoriaActual.getEnfoque();
        pruebaActual = ((Pruebas) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(3));
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
        preguntas = administrarEvaluacion.obtenerCuestinonario(secPrueba, secIndigacion);
        nroPreguntas = administrarEvaluacion.obtenerNroPreguntas(secPrueba);
        validarSiExistenRespuestas();
        obtenerPuntajeMaximo();
        calcularPuntajePorcentaje();
    }

    public void enviarRespuestas() {
        boolean todas = true;
        for (Preguntas pregunta : preguntas) {
            if (pregunta.getRespuesta() == null) {
                todas = false;
                break;
            }
        }
        if (todas) {
            boolean error = false;
            for (Preguntas pregunta : preguntas) {
                if (pregunta.isNuevo()
                        && administrarEvaluacion.registrarRespuesta(secIndigacion, pregunta.getSecuencia(), pregunta.getRespuesta())) {
                } else if (!pregunta.isNuevo()
                        && administrarEvaluacion.actualizarRespuesta(secIndigacion, pregunta.getSecuencia(), pregunta.getRespuesta())) {
                } else {
                    MensajesUI.error("No fue posible registrar las respuesta.");
                    error = true;
                    break;
                }
            }
            if (observacionEvaluador != null && observacionEvaluador.length()<500 ){
                MensajesUI.error("La observaci�n es obligatoria y no debe ser mayor a 500 letras.");
                error = true;
            }
            if (!error) {
                if (administrarEvaluacion.actualizarPorcentaje(secIndigacion, observacionEvaluador, porcentaje)
                        && administrarEvaluacion.actualizarPorcentaje(secConvocatoria, secEvaluado, agrupado)) {
                    PrimefacesContextUI.ejecutar("PF('envioExitoso').show()");
                } else {
                    MensajesUI.error("No fue posible registrar el puntaje, ni la observaci�n en la prueba.");
                }
                //MensajesUI.info("Respuestas guardadas exitosamente.");
            }
        } else {
            MensajesUI.error("Antes de enviar la evaluaci�n debe responder todas las preguntas.");
        }
    }

    public void eliminarRespuestas() {
        observacionEvaluador = null;
        if (administrarEvaluacion.eliminarRespuestas(secIndigacion)
                && administrarEvaluacion.actualizarPorcentaje(secIndigacion, observacionEvaluador, 0)
                && administrarEvaluacion.actualizarPorcentaje(secConvocatoria, secEvaluado, agrupado)) {
            MensajesUI.info("Respuestas eliminadas exitosamente.");
        } else {
            MensajesUI.error("No fue posible eliminar las respuestas.");
        }
        cargarDetallePreguntas();
    }

    public void validarSiExistenRespuestas() {
        tieneRespuestas = false;
        for (Preguntas pregunta : preguntas) {
            if (!pregunta.isNuevo()) {
                tieneRespuestas = true;
                break;
            }
        }
    }

    public void obtenerPuntajeMaximo() {
        puntajeMaximo = BigInteger.ZERO;
        for (Preguntas pregunta : preguntas) {
            BigInteger inicio = BigInteger.ZERO;
            for (Respuestas respuesta : pregunta.getRespuestas()) {
                if (respuesta.getCuantitativo().compareTo(inicio) == 1) {
                    inicio = respuesta.getCuantitativo();
                }
            }
            puntajeMaximo = puntajeMaximo.add(inicio);
        }
    }

    public void calcularPuntajePorcentaje() {
        puntaje = 0;
        porcentaje = 0;
        for (Preguntas pregunta : preguntas) {
            if (pregunta.getRespuesta() != null) {
                for (Respuestas respuesta : pregunta.getRespuestas()) {
                    if (respuesta.getSecuencia().compareTo(pregunta.getRespuesta()) == 0) {
                        puntaje = puntaje + respuesta.getCuantitativo().intValue();
                        break;
                    }
                }
            }
        }
        porcentaje = (puntaje * 100) / puntajeMaximo.intValue();
    }

    //GETTER AND SETTER
    public List<Preguntas> getPreguntas() {
        return preguntas;
    }

    public String getEvaluado() {
        return evaluado;
    }

    public String getEvaluador() {
        return evaluador;
    }

    public String getConvocatoria() {
        return convocatoria;
    }

    public BigInteger getNroPreguntas() {
        return nroPreguntas;
    }

    public BigInteger getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public String getPrueba() {
        return prueba;
    }

    public String getObservacionEvaluador() {
        return observacionEvaluador;
    }

    public void setObservacionEvaluador(String observacionEvaluador) {
        if (observacionEvaluador.length() <= 500) {
            this.observacionEvaluador = observacionEvaluador;
        }else{
            MensajesUI.error("La observaci�n debe ser menor a 500 letras.");
        }
    }

    public boolean isTieneRespuestas() {
        return tieneRespuestas;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

}
