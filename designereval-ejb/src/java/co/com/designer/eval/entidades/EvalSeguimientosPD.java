package co.com.designer.eval.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Thalia Manrique
 */
@Entity
public class EvalSeguimientosPD implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "EVALPLANDESARROLLO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private EvalPlanesDesarrollos evalplandesarrollo;
    @Column(name = "FECHA")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "COMENTARIO")
    private String comentario;
    @Column(name = "PORCENTAJE")
    private int porcentaje;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public EvalPlanesDesarrollos getEvalplandesa() {
        return evalplandesarrollo;
    }

    public void setEvalplandesa(EvalPlanesDesarrollos evalplandesa) {
        this.evalplandesarrollo = evalplandesa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
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
        if (!(object instanceof EvalSeguimientosPD)) {
            return false;
        }
        EvalSeguimientosPD other = (EvalSeguimientosPD) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.designer.eval.entidades.EvalSeguimientosPD[ secuencia=" + secuencia + " ]";
    }
    
}
