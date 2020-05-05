package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Thalia Manrique
 */

public interface IPersistenciaEvalSeguimientosPD {
    
    public List<EvalPlanesDesarrollos> obtenerEvalSeguimientosPD(EntityManager em, BigInteger secEvalPlanDesarrollo);
    public boolean registrarEvalSeguimientoPD(EntityManager em, BigDecimal secEvalplandesarrollo,
            Date fecha, String comentario, String porcentaje);
    
}
