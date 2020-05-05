package co.com.designer.eval.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@XmlRootElement
public class Convocatorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "EVALVIGCONVOCATORIA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date evalVigConvocatoria;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "ENFOQUE")
    private String enfoque;
    @Column(name = "AGRUPADO")
    private Integer agrupado;
    @Column(name = "FECHAINICIO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "FECHALIMITE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaLimite;
    @Column(name = "OBJETIVOS")
    private String objetivos;

    public Convocatorias() {
    }

    public Convocatorias(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Convocatorias(BigInteger secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getEvalVigConvocatoria() {
        return evalVigConvocatoria;
    }

    public void setEvalVigConvocatoria(Date evalVigConvocatoria) {
        this.evalVigConvocatoria = evalVigConvocatoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEnfoque() {
        return enfoque;
    }

    public void setEnfoque(String enfoque) {
        this.enfoque = enfoque;
    }

    public Integer getAgrupado() {
        return agrupado;
    }

    public void setAgrupado(Integer agrupado) {
        this.agrupado = agrupado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Convocatorias)) {
            return false;
        }
        Convocatorias other = (Convocatorias) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.Convocatorias[ secuencia=" + secuencia + " ]";
    }

}
