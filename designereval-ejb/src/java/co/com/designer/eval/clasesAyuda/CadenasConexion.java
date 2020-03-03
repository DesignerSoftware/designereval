package co.com.designer.eval.clasesAyuda;

/**
 *
 * @author Felipe Triviño
 */
public class CadenasConexion implements Comparable {

    private String id;
    private String descripcion;
    private String cadena;
    private String nit;
    private String fondo;
    private String grupo;
    private boolean observacion;
    private String esquema;

    public CadenasConexion(String id, String descripcion, String cadena, String nit, String fondo, String grupo, boolean observacion, String esquema) {
        this.id = id;
        this.descripcion = descripcion;
        this.cadena = cadena;
        this.nit = nit;
        this.fondo = fondo;
        this.grupo = grupo;
        this.observacion = observacion;
        this.esquema = esquema;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public boolean isObservacion() {
        return observacion;
    }

    public void setObservacion(boolean observacion) {
        this.observacion = observacion;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }
    
    @Override
    public int compareTo(Object o) {
        int resultado;
        CadenasConexion ck = (CadenasConexion) o;
        if (this.getId().equalsIgnoreCase(ck.getId())) {
            resultado = 0;
        } else if (Integer.parseInt(this.getId()) < Integer.parseInt(ck.getId())) {
            resultado = -1;
        } else {
            resultado = 1;
        }
        return resultado;
    }

}
