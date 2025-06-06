package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.entidades.Respuestas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Familia Triviño Amad
 */
public interface IPersistenciaRespuestas {

    public List<Respuestas> obtenerRespuestas(EntityManager em, BigInteger secPregunta);

    public List<Respuestas> obtenerRespuestas(EntityManager em, BigInteger secPregunta, String Historica);

    public boolean registrarRespuesta(EntityManager em, BigInteger secIndagacion, BigInteger secPregunta, BigInteger secRespuesta);

    public BigInteger consultarRespuesta(EntityManager em, BigInteger secIndagacion, BigInteger secPregunta);

    public boolean actualizarRespuesta(EntityManager em, BigInteger secIndagacion, BigInteger secPregunta, BigInteger secRespuesta);

    public boolean eliminarRespuestas(EntityManager em, BigInteger secIndagacion);

    public boolean registrarActualizarRespuesta(EntityManager em, List<Preguntas> preguntas, BigInteger secIndagacion);

    /**
     * Método para consultar el puntaje obtenido por un emleado en 
     * una evaluación anterior a una fecha específica. La consulta no incluye 
     * la fecha que se recibe como parámetro.
     * 
     * @param em Manejador de la conexión.
     * @param secEmpleado secuencia del empleado.
     * @param dtFechaCorte Fecha límite para realizar la consulta.
     * @return Puntaje obtenido por el empleado en esa evaluación.
     */
    public BigDecimal consultarPuntajeEvalAnterior(EntityManager em, BigInteger secEmpleado, Date dtFechaCorte);
    
    public boolean registrarActualizarRespuestaHistorica(EntityManager em, Preguntas pregunta, BigInteger secIndagacion, BigDecimal resAnterior);
    
}
