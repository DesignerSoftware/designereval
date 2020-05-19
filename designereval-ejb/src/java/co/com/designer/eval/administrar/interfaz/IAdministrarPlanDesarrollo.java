/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Cursos;
import co.com.designer.eval.entidades.EvalActividades;
import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import co.com.designer.eval.entidades.EvalSeguimientosPD;
import co.com.designer.eval.entidades.Profesiones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Thalia Manrique
 */
@Local
public interface IAdministrarPlanDesarrollo {
    public void obtenerConexion(String idSesion);
    public List<EvalActividades> obtenerActividades();
    public List<EvalActividades> obtenerActividadesEmpl(BigInteger secuenciaEvaluador, BigInteger secConvocatoria);
    public List<Cursos> obtenerCursos();
    public List<Profesiones> obtenerProfesiones();
    public List<EvalPlanesDesarrollos> obtenerPlanesDesarrollos(BigInteger secEvalResultadoConv);
    public boolean eliminarPlanDesarrollo(BigInteger secPlanDesarrollo);
    public boolean registrarPlanDesarrollo(BigDecimal secCodigo, //BigInteger secIndagacion,
            BigInteger secEvalResultado, String secEvalActividad, String observacion, String secCurso, String secProfesion);
    public BigDecimal obtenerUltimoCodigo(BigInteger secEvalResultadoConv);
    public EvalActividades consultarEvalActividad(BigDecimal secuencia);
    public List<Convocatorias> obtenerConvocatorias(String usuario);
    public boolean registrarBitacora(BigInteger secPlanDesarrollo, 
           Date fecha, String comentario, String porcentaje);
    public List<EvalSeguimientosPD> obtenerBitacoras(BigInteger secPlanDesarrollo);    
    public boolean eliminarBitacora(BigInteger secBitacora);
    public BigDecimal cantidadBitacoras(BigInteger secEvalResultadoConv);
    public BigDecimal cantidadEvalPlanesDesarrollos(BigInteger secEvalResultadoConv);
}
