package co.com.designer.eval.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@XmlRootElement
public class Preguntas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CONSECUTIVO")
    private BigInteger consecutivo;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "OBSERVACIONES")
    private String observacion;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name="PESO")
    private BigInteger peso;
    
    @Transient
    private List<Respuestas> respuestas;
    @Transient
    private BigInteger respuesta;
    @Transient
    private boolean nuevo;
    @Transient
    private List<String> arDescripcion;

    public Preguntas() {
        arDescripcion = new ArrayList();
    }

    public Preguntas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Preguntas(BigInteger secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(BigInteger consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigInteger getPeso() {
        return peso;
    }

    public void setPeso(BigInteger peso) {
        this.peso = peso;
    }
    
    public List<Respuestas> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuestas> respuestas) {
        this.respuestas = respuestas;
    }

    public BigInteger getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(BigInteger respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public List<String> getArDescripcion() {
        System.out.println("descripcion: " + descripcion);
        boolean isExtrae = true;
        if (descripcion != null && !descripcion.isEmpty()) {
            if (arDescripcion == null) {
                arDescripcion = new ArrayList();
                isExtrae = true;
            }
            if (isExtrae || arDescripcion.isEmpty()) {
                arDescripcion.clear();
                String[] arreglo = descripcion.split("#");
                for (String ob : arreglo) {
                    arDescripcion.add(ob);
                }
                System.out.println("tamanho lista desc: " + arDescripcion.size());
            }
        }
        return arDescripcion;
    }

    public void setArDescripcion(List<String> arDescripcion) {
        this.arDescripcion = arDescripcion;
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
        if (!(object instanceof Preguntas)) {
            return false;
        }
        Preguntas other = (Preguntas) object;
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
