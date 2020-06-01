package co.com.designer.eval.persistencia.implementacion;

import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPruebas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author luistrivino
 */
@Stateless
public class PersistenciaPruebas implements IPersistenciaPruebas {

    @Override
    public List<Pruebas> obtenerPruebasEvaluado(EntityManager em, String usuario, BigInteger secEmplConvo) {
        try {
            em.joinTransaction();
            em.clear();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("SELECT EI.SECUENCIA, EI.EVALRESULTADOCONV, EI.PUNTOOBTENIDO,\n"
                    + "EI.OBSEVALUADOR, EI.OBSEVALUADO, EI.EMPLEADOEVALUADOR, \n"
                    + "(SELECT PL.CODIGO||'-'||PL.DESCRIPCION \n"
                    + "FROM EVALPRUEBAS PR, EVALPLANILLAS PL \n"
                    + "WHERE PR.PLANILLA = PL.SECUENCIA \n"
                    + "AND PR.SECUENCIA = EI.EVALPRUEBA) PRUEBA, -- Usa evalpruebas para llegar a los campos de evalplanillas \n"
                    + "(SELECT PR.DESCRIPCION \n"
                    + "FROM EVALPRUEBAS PR \n"
                    + "WHERE PR.SECUENCIA = EI.EVALPRUEBA) FACTOR, -- Usa la descripción de evalpruebas para indicar el factor \n"
                    + "(SELECT PR.SECUENCIA \n"
                    + "FROM EVALPRUEBAS PR \n"
                    + "WHERE PR.SECUENCIA = EI.EVALPRUEBA) SECPRUEBA, \n"
                    + "EI.ESTADOPRUEBA \n"
                    + "FROM EVALINDAGACIONES EI \n"
                    + "WHERE EI.EMPLEADOEVALUADOR=( \n"
                    + "           SELECT P.SECUENCIA \n"
                    //                    + "           FROM USUARIOS U, PERSONAS P \n"
                    + "           FROM CONEXIONESEVAL U, PERSONAS P \n"
                    + "           WHERE U.persona=P.secuencia \n"
                    //                    + "           AND U.ALIAS=?) \n"
                    + "           AND U.SEUDONIMO = ? ) \n"
                    + "AND EI.EVALRESULTADOCONV = ? "
                    + "ORDER BY PRUEBA ", Pruebas.class);
            q.setParameter(1, usuario);
            q.setParameter(2, secEmplConvo);
            List<Pruebas> lst = q.getResultList();
//            em.getTransaction().commit();
            return lst;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaPruebas.obtenerPruebasEvalaudo: " + ex);
//            terminarTransaccionException(em);
            return null;
        }
    }

    @Override
    public boolean actualizarPorcentaje(EntityManager em, BigInteger secPrueba, String observacion, double porcentaje) {
        try {
//            em.getTransaction().begin();
//            em.joinTransaction();
            //Query q = em.createNativeQuery("UPDATE EVALINDAGACIONES A SET A.PUNTOOBTENIDO = ? , OBSEVALUADOR = ? WHERE A.SECUENCIA = ? ");
            Query q = em.createNativeQuery("UPDATE EVALINDAGACIONES A SET A.PUNTOOBTENIDO = ? , OBSEVALUADOR = ? , A.ESTADOPRUEBA = 'C' WHERE A.SECUENCIA = ? ");
            q.setParameter(1, porcentaje);
            q.setParameter(2, observacion);
            q.setParameter(3, secPrueba);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaPruebas.actualizarPorcentaje: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    @Override
    public boolean actualizarEstado(EntityManager em, BigInteger secPrueba, String estado) {
        try {
            em.joinTransaction();
            em.clear();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("UPDATE EVALINDAGACIONES SET ESTADOPRUEBA = ? WHERE SECUENCIA = ? ");
            q.setParameter(1, estado);
            q.setParameter(2, secPrueba);
            q.executeUpdate();
//            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaPruebas.actualizarEstado: " + ex);
//            terminarTransaccionException(em);
            return false;
        }
    }

    @Override
    public String estaConsolidado(EntityManager em, BigInteger secConvocatoria, BigInteger secEvaluado) {
        try {
            em.joinTransaction();
//            em.getTransaction().begin();
            Query q = em.createNativeQuery("select EVALCONVOCATORIAS_PKG.ESTACONSOLIDADO( ? , ? ) FROM DUAL ");
            q.setParameter(1, secConvocatoria);
            q.setParameter(2, secEvaluado);
            String resul = (String) q.getSingleResult();
//            em.getTransaction().commit();
            return resul;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaConvocatorias.cerrarEvaluaciones: " + ex);
//            terminarTransaccionException(em);
            return "N";
        }
    }
    
    @Override
    public String validarJefeInmediato(EntityManager em, BigDecimal secEvaluador, BigInteger secEvaluado){
        try {
            em.joinTransaction();
            Query q = em.createNativeQuery("select EVALCONVOCATORIAS_PKG.VALIDARJEFEINMEDIATO( ? , ? ) FROM DUAL ");
            q.setParameter(1, secEvaluador);
            q.setParameter(2, secEvaluado);
            String resul = (String) q.getSingleResult();
            return resul;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName()+": "+"Error PersistenciaConvocatorias.validarJefeInmediato: " + ex);
            return "N";
        }
    }
    
    private void terminarTransaccionException(EntityManager em) {
        System.out.println(this.getClass().getName() + ".terminarTransaccionException");
//        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
        if (em != null && em.isOpen()) {
            System.out.println(this.getClass().getName()+": "+"Antes de hacer rollback");
//            em.getTransaction().rollback();
            em.close();
            System.out.println(this.getClass().getName()+": "+"Despues de hacer rollback");
        }
    }
}
