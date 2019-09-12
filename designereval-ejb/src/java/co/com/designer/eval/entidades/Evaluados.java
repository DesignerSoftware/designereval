package co.com.designer.eval.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@XmlRootElement
public class Evaluados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "EMPLEADO")
    private BigInteger empleado;
    @Column(name = "NOMBREPERSONA")
    private String nombrePersona;
    @Column(name = "EMAIL")
    private String eMail;
    @Column(name = "PUNTAJEOBTENIDO")
    private BigDecimal puntajeObtenido;
    @Column(name = "FECHAPERIODODESDE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodoDesde;
    @Column(name = "FECHAPERIODOHASTA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodoHasta;
    @Column(name = "EVALCONVOCATORIA")
    private BigInteger evalConvocatoria;
    @Column(name = "NOMBREPRUEBA")
    private String nombrePrueba;
    @Column(name = "ESTADOEVAL")
    private String estadoEval;
    @Column(name = "CONSOLIDADO")
    private String consolidado;
    @Transient
    private boolean esConsolidado;

    public Evaluados() {
    }

    public Evaluados(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Evaluados(BigInteger secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getEmpleado() {
        return empleado;
    }

    public void setEmpleado(BigInteger empleado) {
        this.empleado = empleado;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public BigDecimal getPuntajeObtenido() {
        return puntajeObtenido;
    }

    public void setPuntajeObtenido(BigDecimal puntajeObtenido) {
        this.puntajeObtenido = puntajeObtenido;
    }

    public Date getPeriodoDesde() {
        return periodoDesde;
    }

    public void setPeriodoDesde(Date periodoDesde) {
        this.periodoDesde = periodoDesde;
    }

    public Date getPeriodoHasta() {
        return periodoHasta;
    }

    public void setPeriodoHasta(Date periodoHasta) {
        this.periodoHasta = periodoHasta;
    }

    public BigInteger getEvalConvocatoria() {
        return evalConvocatoria;
    }

    public void setEvalConvocatoria(BigInteger evalConvocatoria) {
        this.evalConvocatoria = evalConvocatoria;
    }

    public String getNombrePrueba() {
        return nombrePrueba;
    }

    public void setNombrePrueba(String nombrePrueba) {
        this.nombrePrueba = nombrePrueba;
    }

    public String getEstadoEval() {
        return estadoEval;
    }

    public void setEstadoEval(String estadoEval) {
        this.estadoEval = estadoEval;
    }

    public String getConsolidado() {
        return consolidado;
    }

    public void setConsolidado(String consolidado) {
        this.consolidado = consolidado;
    }

    public boolean isEsConsolidado() {
        esConsolidado = "S".equalsIgnoreCase(consolidado);
        return esConsolidado;
    }

    public void setEsConsolidado(boolean esConsolidado) {
        this.esConsolidado = esConsolidado;
        consolidado = (this.esConsolidado?"S":"N");
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Evaluados)) {
            return false;
        }
        Evaluados other = (Evaluados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.ParametrizaClave[ secuencia=" + secuencia + " ]";
    }

}
