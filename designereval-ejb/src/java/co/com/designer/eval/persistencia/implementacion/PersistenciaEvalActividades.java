/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.EvalActividades;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvalActividades;
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
public class PersistenciaEvalActividades implements IPersistenciaEvalActividades {

    @Override
    public List<EvalActividades> obtenerActividades(EntityManager em) {
        try {
            em.joinTransaction();
            Query q = em.createNativeQuery("SELECT SECUENCIA, CODIGO, DESCRIPCION "
                    + "FROM EVALACTIVIDADES "
                    + "ORDER BY CODIGO ASC", EvalActividades.class);
            List<EvalActividades> lst = q.getResultList();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalActividades.obtenerActividades: " + ex);
            return null;
        }
    }
    
    
    @Override
    public List<EvalActividades> obtenerActividadesEmpl(EntityManager em, BigInteger secEmpleado, BigInteger secConvocatoria) {
        try {
//            em.getTransaction().begin();
            em.joinTransaction();
            Query q = em.createNativeQuery("select " +
            " ea.secuencia, ea.codigo, ea.descripcion " +
            "from evalplanesdesarrollos epd, evalactividades ea, evalresultadosconv erc, evalconvocatorias ec, "+
            "empleados e, cursos c, personas p, conexioneseval ce " +
            "where " +
            "epd.evalactividad=ea.secuencia " +
            "and erc.empleado=e.secuencia " +
            "and epd.EVALRESULTADOCONV=erc.secuencia " +
            "and erc.evalconvocatoria=ec.secuencia " +
            "and ec.estado='ALCANCE' " +
            "and epd.curso=c.secuencia(+) " +
            "and e.persona=p.secuencia " +
            "and ce.PERSONA=p.secuencia "+
            "and e.secuencia= ? "+
            "and ec.secuencia= ? ", EvalActividades.class);
            q.setParameter(1, secEmpleado);
            q.setParameter(2, secConvocatoria);
            List<EvalActividades> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaEvalActividades.obtenerActividadesEmpl: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }
    
        public EvalActividades consultarEvalActividad(EntityManager eManager, BigDecimal secuencia) {
        try {
            String sqlQuery = "SELECT ea FROM EvalActividades ea WHERE ea.secuencia = :secuencia";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("secuencia", secuencia);
            return (EvalActividades) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEvalActividades.consultarEvalActividad: " + e);
            try {
            } catch (NullPointerException npe) {
                System.out.println("Error de nulo en la transacción.");
            }
            return null;
        }
    }
}
