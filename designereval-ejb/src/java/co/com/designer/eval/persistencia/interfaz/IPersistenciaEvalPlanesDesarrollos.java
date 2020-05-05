/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.designer.eval.persistencia.interfaz;

import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author Soporte
 */
@Local
public interface IPersistenciaEvalPlanesDesarrollos {
    public List<EvalPlanesDesarrollos> obtenerPlanesDesarrollos(EntityManager em, BigInteger secEvalPlanDesarrollos);
    public boolean eliminarPlanDesarrollo(EntityManager em, BigInteger secPlanDesarrollo);
    public boolean registrarPlanDesarrollo(EntityManager em, BigDecimal secCodigo, //BigInteger secIndagacion,
            BigInteger secEvalResultado, String secEvalActividad, String observacion, String secCurso, String secProfesion);
    //BigInteger secEvalResultado, BigInteger secEvalActividad, String observacion, BigInteger secCurso);
    public BigDecimal obtenerUltimoCodigo(EntityManager eManager, BigInteger secEvalResultadoConv);
}
