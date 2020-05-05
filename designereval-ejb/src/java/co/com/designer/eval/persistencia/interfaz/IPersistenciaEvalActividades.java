package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.EvalActividades;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author Thalia Manrique
 */
@Local
public interface IPersistenciaEvalActividades {
    public List<EvalActividades> obtenerActividades(EntityManager em);
    public List<EvalActividades> obtenerActividadesEmpl(EntityManager em, BigInteger usuario, BigInteger secConvocatoria);
    public EvalActividades consultarEvalActividad(EntityManager eManager, BigDecimal secuencia);
}
