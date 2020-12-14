package co.com.designer.eval.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.Transient;
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
//    @Transient
//    private String dtVigConvocatoria;
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

    public String getDtVigConvocatoria() {
        String stVigConvocatoria = "";
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(this.evalVigConvocatoria);
        int dia, mes, agno;
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH) + 1;
        agno = cal.get(Calendar.YEAR);
        if (dia < 10) {
            stVigConvocatoria = "0" + String.valueOf(dia);
        } else {
            stVigConvocatoria = String.valueOf(dia);
        }
        stVigConvocatoria = stVigConvocatoria + "/";
        if (mes < 10) {
            stVigConvocatoria = stVigConvocatoria + "0" + String.valueOf(mes);
        } else {
            stVigConvocatoria = stVigConvocatoria + String.valueOf(mes);
        }
        stVigConvocatoria = stVigConvocatoria + "/" + agno;

//        return dtVigConvocatoria;
        System.out.println(this.getClass().getName() + ".getDtVigConvocatoria: " + stVigConvocatoria);
        return stVigConvocatoria;
    }

    public void setDtVigConvocatoria(String dtVigConvocatoria) {
        String[] fecha = dtVigConvocatoria.split("/");
        Calendar cal = Calendar.getInstance();
        int dia, mes, agno;
        if (fecha.length == 3) {
            try {
                dia = Integer.parseInt(fecha[0]);
                mes = Integer.parseInt(fecha[1]);
                agno = Integer.parseInt(fecha[2]);
//                if (dia<=0 || mes <= 0 || agno <= 0){
//                    throw new NumberFormatException();
//                }
                if (agno <= 1960 || agno >= 9999) {
                    throw new NumberFormatException();
                }
                if (mes <= 0 || mes > 12) {
                    throw new NumberFormatException();
                }
                switch (mes) {
                    case 1:
                        if (dia <= 0 || dia > 31) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 2:
                        if (dia <= 0 || dia > 29) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 3:
                        if (dia <= 0 || dia > 31) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 4:
                        if (dia <= 0 || dia > 30) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 5:
                        if (dia <= 0 || dia > 31) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 6:
                        if (dia <= 0 || dia > 30) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 7:
                        if (dia <= 0 || dia > 31) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 8:
                        if (dia <= 0 || dia > 31) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 9:
                        if (dia <= 0 || dia > 30) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 10:
                        if (dia <= 0 || dia > 31) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 11:
                        if (dia <= 0 || dia > 30) {
                            throw new NumberFormatException();
                        }
                        break;
                    case 12:
                        if (dia <= 0 || dia > 31) {
                            throw new NumberFormatException();
                        }
                        break;
                    default:
                        throw new NumberFormatException();
                }
                cal.clear();
//                cal.set(Calendar.DAY_OF_MONTH, dia);
//                cal.set(Calendar.MONTH, mes);
//                cal.set(Calendar.YEAR, agno);
                cal.set(agno, mes, dia);
            } catch (NumberFormatException npe) {
                System.out.println(this.getClass().getName() + ".setDtVigConvocatoria: " + dtVigConvocatoria
                        + "Error capturando fecha");
            }
        }
        System.out.println(this.getClass().getName() + ".setDtVigConvocatoria: " + dtVigConvocatoria);
//        this.dtVigConvocatoria = dtVigConvocatoria;
        this.evalVigConvocatoria = cal.getTime();
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
