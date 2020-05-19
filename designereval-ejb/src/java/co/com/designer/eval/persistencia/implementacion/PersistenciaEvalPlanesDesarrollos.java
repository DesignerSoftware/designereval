/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalPlanesDesarrollos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Soporte
 */
@Stateless
public class PersistenciaEvalPlanesDesarrollos implements IPersistenciaEvalPlanesDesarrollos {
    
    @Override
    public List<EvalPlanesDesarrollos> obtenerPlanesDesarrollos(EntityManager em, BigInteger secEvalPlanDesarrollos) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createQuery("select epd from EvalPlanesDesarrollos epd where epd.evalresultadoconv=:secResulConv order by epd.codigo asc", EvalPlanesDesarrollos.class);
            // q.setParameter(1, secEvalPlanDesarrollos);
            q.setParameter("secResulConv", secEvalPlanDesarrollos);
            /*q.setParameter(2, secConvocatoria);*/
            List<EvalPlanesDesarrollos> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaEvalPlanesDesarrollos.obtenerPlanesDesarrollos: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }
    
    @Override
    public boolean eliminarPlanDesarrollo(EntityManager em, BigInteger secPlanDesarrollo) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("DELETE EVALPLANESDESARROLLOS "
                    + "WHERE SECUENCIA = ? ");
            q.setParameter(1, secPlanDesarrollo);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalPlanesDesarrollos.eliminarPlanDesarrollo: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }
    
    @Override
    public boolean registrarPlanDesarrollo(EntityManager em, BigDecimal secCodigo, //BigInteger secIndagacion,
            //BigInteger secEvalResultado, BigInteger secEvalActividad, String observacion, BigInteger secCurso) {
            BigInteger secEvalResultado, String secEvalActividad, String observacion, String secCurso, String secProfesion) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("INSERT INTO EVALPLANESDESARROLLOS (CODIGO, EVALRESULTADOCONV, EVALACTIVIDAD, OBSERVACION, CURSO, PROFESION) " +
                " VALUES (?, ?, ?, ?, ?, ?) ");
            q.setParameter(1, secCodigo);
            //q.setParameter(2, secIndagacion);
            q.setParameter(2, secEvalResultado);
            q.setParameter(3, secEvalActividad);
            q.setParameter(4, observacion);
            q.setParameter(5, secCurso);
            q.setParameter(6, secProfesion);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalPlanesDesarrollos.registrarPlanDesarrollo: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    public BigDecimal obtenerUltimoCodigo(EntityManager eManager, BigInteger secEvalResultadoConv) {
        BigDecimal resultado=new BigDecimal(0);
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
//            Query query = eManager.createNativeQuery("SELECT p.* FROM Usuarios u, Personas p WHERE u.alias = ? AND u.persona = p.secuencia ", Personas.class);
            Query query = eManager.createNativeQuery("select nvl(max(e.codigo), 0) cod from evalplanesdesarrollos e where evalresultadoconv = ?");
            query.setParameter(1, secEvalResultadoConv);
            resultado = (BigDecimal) query.getSingleResult();
            System.out.println("resultado ultimoCodigo: "+resultado);
//            eManager.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalPlanesDesarrollos.obtenerUltimoCodigo: " + e);
//            terminarTransaccionException(eManager);
            return resultado;
        }
    }
    
    
    @Override
    public BigDecimal countBitacoras(EntityManager eManager, BigInteger secEvalPlanesDesarrollos) {
        BigDecimal resultado=new BigDecimal(0);
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
//            Query query = eManager.createNativeQuery("SELECT p.* FROM Usuarios u, Personas p WHERE u.alias = ? AND u.persona = p.secuencia ", Personas.class);
            Query query = eManager.createNativeQuery("select count(*) from evalplanesdesarrollos epd, evalseguimientospd esp where epd.secuencia=esp.evalplandesarrollo and epd.secuencia = ?");
            query.setParameter(1, secEvalPlanesDesarrollos);
            resultado = (BigDecimal) query.getSingleResult();
            System.out.println("cantidad de bitacoraso: "+resultado);
//            eManager.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalPlanesDesarrollos.countBitacoras: " + e);
//            terminarTransaccionException(eManager);
            return resultado;
        }
    }
    
    @Override
    public BigDecimal cantidadEvalPlanesDesarrollos(EntityManager eManager, BigInteger secEvalResultadoConv) {
        BigDecimal resultado = new BigDecimal(0);
        try {
//            eManager.getTransaction().begin();
            eManager.joinTransaction();
//            Query query = eManager.createNativeQuery("SELECT p.* FROM Usuarios u, Personas p WHERE u.alias = ? AND u.persona = p.secuencia ", Personas.class);
            Query query = eManager.createNativeQuery("select count(*) from EvalPlanesDesarrollos epd where epd.evalresultadoconv=?");
            query.setParameter(1, secEvalResultadoConv);
            resultado = (BigDecimal) query.getSingleResult();
            System.out.println("cantidad de evalplanesdesarrollos: " + resultado);
//            eManager.getTransaction().commit();
            return resultado;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error PersistenciaEvalPlanesDesarrollos.cantidadEvalPlanesDesarrollos: " + e);
//            terminarTransaccionException(eManager);
            return resultado;
        }
    }
}
