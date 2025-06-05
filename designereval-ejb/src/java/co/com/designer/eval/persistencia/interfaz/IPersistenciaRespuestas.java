package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Preguntas;
import co.com.designer.eval.entidades.Respuestas;
import java.math.BigInteger;
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
    
}
