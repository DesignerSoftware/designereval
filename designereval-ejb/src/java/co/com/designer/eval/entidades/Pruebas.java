package co.com.designer.eval.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@XmlRootElement
public class Pruebas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "EVALRESULTADOCONV")
    private BigInteger evalResultadoConv;
    @Column(name = "PUNTOOBTENIDO")
    private BigDecimal puntoObtenido;
    @Column(name = "OBSEVALUADOR")
    private String obsEvaluador;
    @Column(name = "OBSEVALUADO")
    private String obsEvaluado;
    @Column(name = "EMPLEADOEVALUADOR")
    private BigInteger empleadoEvaluador;
    @Column(name = "PRUEBA")
    private String prueba;
    @Column(name = "FACTOR")
    private String factor;
    @Column(name = "SECPRUEBA")
    private BigInteger secPrueba;
    @Column(name = "ESTADOPRUEBA")
    private String estado;
    @Column(name = "IDEAL")
    private BigDecimal ideal;

    public Pruebas() {
    }

    public Pruebas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Pruebas(BigInteger secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getEvalResultadoConv() {
        return evalResultadoConv;
    }

    public void setEvalResultadoConv(BigInteger evalResultadoConv) {
        this.evalResultadoConv = evalResultadoConv;
    }

    public BigDecimal getPuntoObtenido() {
        return puntoObtenido;
    }

    public void setPuntoObtenido(BigDecimal puntoObtenido) {
        this.puntoObtenido = puntoObtenido;
    }

    public String getObsEvaluador() {
        return obsEvaluador;
    }

    public void setObsEvaluador(String obsEvaluador) {
        this.obsEvaluador = obsEvaluador;
    }

    public String getObsEvaluado() {
        return obsEvaluado;
    }

    public void setObsEvaluado(String obsEvaluado) {
        this.obsEvaluado = obsEvaluado;
    }

    public BigInteger getEmpleadoEvaluador() {
        return empleadoEvaluador;
    }

    public void setEmpleadoEvaluador(BigInteger empleadoEvaluador) {
        this.empleadoEvaluador = empleadoEvaluador;
    }

    public BigInteger getSecPrueba() {
        return secPrueba;
    }

    public void setSecPrueba(BigInteger secPrueba) {
        this.secPrueba = secPrueba;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pruebas)) {
            return false;
        }
        Pruebas other = (Pruebas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    public String getPrueba() {
        return prueba;
    }

    public void setPrueba(String prueba) {
        this.prueba = prueba;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getIdeal() {
        return ideal;
    }

    public void setIdeal(BigDecimal ideal) {
        this.ideal = ideal;
    }
    

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.ParametrizaClave[ secuencia=" + secuencia + " ]";
    }

}
