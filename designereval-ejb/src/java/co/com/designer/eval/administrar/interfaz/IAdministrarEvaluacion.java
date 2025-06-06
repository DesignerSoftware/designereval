package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Preguntas;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Felipe Triviño
 */
public interface IAdministrarEvaluacion {

    public void obtenerConexion(String idSesion);

    public List<Preguntas> obtenerCuestinonario(BigInteger secPrueba, BigInteger secIndagacion);

    public BigInteger obtenerNroPreguntas(BigInteger secPrueba);

    public boolean registrarRespuesta(BigInteger secIndagacion, BigInteger secPregunta, BigInteger secRespuesta);

    public boolean actualizarRespuesta(BigInteger secIndagacion, BigInteger secPregunta, BigInteger secRespuesta);

    public boolean eliminarRespuestas(BigInteger secIndagacion);

    public boolean actualizarPorcentaje(BigInteger secPrueba, String observacion, double porcentaje);

    public boolean actualizarPorcentaje(BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado);

    public boolean registrarActualizarRespuesta(List<Preguntas> preguntas, BigInteger secIndagacion);
    public boolean registrarRespuestasPuntos(List<Preguntas> preguntas, BigInteger secIndagacion,
            String observacionEvaluador, double porcentaje,
            BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado);

    public boolean reiniciarEvaluacion(BigInteger secIndagacion, String observacion, double porcentaje, BigInteger secConvocatoria, BigInteger secEvaluado, Integer agrupado);
    public boolean consultarEvaluacionHistorica(BigInteger secEmpleado, Date fechaCorte, BigInteger secIndagacion, Preguntas pregunta);
}
