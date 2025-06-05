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
public class Respuestas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CUALITATIVO")
    private String cualitativo;
    @Column(name = "CUANTITATIVO")
    private BigDecimal cuantitativo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
//    @Transient
//    private BigDecimal puntajeManual;

    public Respuestas() {
    }

    public Respuestas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Respuestas(BigInteger secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getCualitativo() {
        return cualitativo;
    }

    public void setCualitativo(String cualitativo) {
        this.cualitativo = cualitativo;
    }

    public BigDecimal getCuantitativo() {
        return cuantitativo;
    }

    public void setCuantitativo(BigDecimal cuantitativo) {
        this.cuantitativo = cuantitativo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

//    public BigDecimal getPuntajeManual() {
//        return puntajeManual;
//    }
//
//    public void setPuntajeManual(BigDecimal puntajeManual) {
//        this.puntajeManual = puntajeManual;
//    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Respuestas)) {
            return false;
        }
        Respuestas other = (Respuestas) object;
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
