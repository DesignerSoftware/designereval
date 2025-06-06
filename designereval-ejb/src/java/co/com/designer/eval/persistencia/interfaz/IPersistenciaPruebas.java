package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.Pruebas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author luistrivino
 */
public interface IPersistenciaPruebas {

    public List<Pruebas> obtenerPruebasEvaluado(EntityManager em, String usuario, BigInteger secEmplConvo);

    //public boolean actualizarPorcentaje(EntityManager em, BigInteger secPrueba, String observacion, double porcentaje);
    public boolean actualizarPorcentaje(EntityManager em, BigInteger secPrueba, String observacion, double porcentaje, String estado);

    public boolean actualizarEstado(EntityManager em, BigInteger secPrueba, String estado);
    
    public String estaConsolidado(EntityManager em, BigInteger secConvocatoria, BigInteger secEvaluado);
    
    public String validarJefeInmediato(EntityManager em, BigDecimal secEvaluador, BigInteger secEvaluado);
}
