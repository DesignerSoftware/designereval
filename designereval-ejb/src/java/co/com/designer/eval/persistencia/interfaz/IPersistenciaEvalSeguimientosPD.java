package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.EvalSeguimientosPD;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Thalia Manrique
 */

public interface IPersistenciaEvalSeguimientosPD {
    
    public List<EvalSeguimientosPD> obtenerEvalSeguimientosPD(EntityManager em, BigInteger secEvalPlanDesarrollo);
    public boolean registrarEvalSeguimientoPD(EntityManager em, BigInteger secEvalplandesarrollo,
            Date fecha, String comentario, String porcentaje);
    public boolean editarEvalSeguimientoPD(EntityManager em, BigInteger secEvalSEguimiento,
            Date fecha, String comentario, int porcentaje);
    public boolean eliminarBitacora(EntityManager em, BigInteger secBitacora);
    public int countBitacoras(EntityManager em, BigInteger secPlanDesarrollo);    
}
