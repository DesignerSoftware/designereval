package co.com.designer.eval.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Thalia Manrique
 */

@Entity
public class EvalPlanesDesarrollos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private BigDecimal codigo;
    @Column(name = "PORCENTAJE")
    private int porcentaje;
    @Column(name = "EVALRESULTADOCONV")
    private BigInteger evalresultadoconv;
    @JoinColumn(name = "EVALACTIVIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private EvalActividades evalActividad;
    @Column(name = "OBSERVACION")
    private String observacion;
    @JoinColumn(name = "CURSO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = true)
    private Cursos curso;
    //@Column(name = "CURSO")
    //private BigInteger curso;
    /*@Column(name = "PROFESION")
    private BigInteger profesion;*/
    @JoinColumn(name = "PROFESION", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = true)
    private Profesiones profesion;
    @Column(name = "PERIODICIDADSEGUIMIENTO")
    private String periodicidad;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getCodigo() {
        return codigo;
    }

    public void setCodigo(BigDecimal codigo) {
        this.codigo = codigo;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigInteger getEvalresultadoconv() {
        return evalresultadoconv;
    }

    public void setEvalresultadoconv(BigInteger evalresultadoconv) {
        this.evalresultadoconv = evalresultadoconv;
    }

    public EvalActividades getEvalActividad() {
        return evalActividad;
    }

    public void setEvalActividad(EvalActividades evalActividad) {
        this.evalActividad = evalActividad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    /*public BigInteger getCurso() {
        return curso;
    }

    public void setCurso(BigInteger curso) {
        this.curso = curso;
    }*/

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }
    
    public Profesiones getProfesion() {
        return profesion;
    }

    public void setProfesion(Profesiones profesion) {
        this.profesion = profesion;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
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
        if (!(object instanceof EvalPlanesDesarrollos)) {
            return false;
        }
        EvalPlanesDesarrollos other = (EvalPlanesDesarrollos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.designer.eval.entidades.EvalPlanesDesarrollos[ secuencia=" + secuencia + " ]";
    }
    
}
