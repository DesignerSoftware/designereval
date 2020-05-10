package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.administrar.interfaz.IAdministrarPlanDesarrollo;
import co.com.designer.eval.controlador.ingreso.ControladorIngreso;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Cursos;
import co.com.designer.eval.entidades.EvalActividades;
import co.com.designer.eval.entidades.EvalPlanesDesarrollos;
import co.com.designer.eval.entidades.EvalSeguimientosPD;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Profesiones;
import co.com.designer.eval.utilidadesUI.MensajesUI;
import co.com.designer.eval.utilidadesUI.PrimefacesContextUI;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Thalia Manrique
 */
@ManagedBean
@SessionScoped
public class ControladorPlanDesarrollo implements Serializable {

    @EJB
    private IAdministrarPlanDesarrollo administrarPlanDesarrollo;
    private IAdministrarInicio administrarInicio;
    private List<EvalActividades> evalactividades;
    private List<Cursos> cursos;
    private List<Profesiones> listaProfesiones;
    private List<Convocatorias> convocatorias;
    private List<EvalPlanesDesarrollos> evalPlanesDesarrollos;
    private List<EvalSeguimientosPD> bitacoras;
    private EvalActividades evalActividad;
    private Cursos cursoSeleccionado;
    private Evaluados evaluadoActual;
    private String evaluado, evaluador, convocatoria, usuario;
    private Convocatorias convocatoriaActual;
    private BigInteger secPlanDesarrollo;
    private final boolean secPrueba = false;
    private String secActividad;

    private EvalPlanesDesarrollos seleccionado;
    private EvalActividades ed;
    private BigDecimal codigoNuevo; // El codigo con el que se guardará el nuevo plan
    private String obsPlanDes; //Observacion sobre la nueva actividad de plan desarrollo
    private String actividadSelec;
    private String secCurso;
    private String secProfesion;
    private boolean isSeleccionadoActividad=false;

    private String evaluadoActualPersona;
    private BigDecimal convocatoriaActualBigDecimal;
    private boolean deshabiProfesiones;
    private boolean deshabiCursos;
    
    //bitacora
    private Date fechaseg;
    private String porcent;
    private String comentario;
    private EvalSeguimientosPD seleccionadoBitacora;
    private boolean isSeleccionadoBitacora=false; //si hay una bitacora seleccionada
    private BigInteger secuenciaBitacora;
    

    public ControladorPlanDesarrollo() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControladorPlanDesarrollo.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPlanDesarrollo.obtenerConexion(ses.getId());
            usuario = ((ControladorIngreso) x.getApplication().evaluateExpressionGet(x, "#{controladorIngreso}", ControladorIngreso.class)).getUsuario();
            System.out.println("Usuario: " + usuario);
            evaluadoActual = (Evaluados) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(0);
            //evaluadoActualPersona = ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).getPruebaString();
            //evaluado = evaluadoActual.getNombrePersona();
            //System.out.println(evaluado);
            convocatorias = administrarPlanDesarrollo.obtenerConvocatorias(usuario);
            convocatoriaActualBigDecimal = ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).getSecConvocatoria();
            System.out.println("Convocatoria actual" + convocatoriaActualBigDecimal);
            //System.out.println("evaluado desde el post construct: "+ evaluado);

            evaluador = (String) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(1);
            //convocatoriaActual = (Convocatorias) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(2);
            //convocatoria = convocatoriaActual.getCodigo() + " - " + convocatoriaActual.getEnfoque();
            cursos = administrarPlanDesarrollo.obtenerCursos();
            listaProfesiones = administrarPlanDesarrollo.obtenerProfesiones();
            evalactividades = administrarPlanDesarrollo.obtenerActividades();
            //evalPlanesDesarrollos=administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
            //System.out.println("EmpleadoActual: "+evaluadoActual.getEmpleado() + " convocatoria: "+convocatoriaActual.getSecuencia());
            deshabiProfesiones = true;
            deshabiCursos = true;
            bitacoras=null; //Inicialmente no mostrar bitacoras
            isSeleccionadoActividad=false;
            isSeleccionadoBitacora=false;
            secuenciaBitacora=null;
            System.out.println("Inicializado");
            //this.setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
        } catch (ELException e) {
            System.out.println(this.getClass().getName() + ".inicializarAdministrador" + ": " + e);
        }
    }

    /*public void cargarActividades() {
        evalactividades = administrarPlanDesarrollo.obtenerActividades();
    }*/
    public void cargarPlanesDesarrollo() {
        System.out.println("cargarPlanesDesarrollo()");
        System.out.println("evaluadoActual: " + this.evaluadoActual.getNombrePersona());
        System.out.println("secuenciaEvaluado: " + this.evaluadoActual.getSecuencia());
        evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
        setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
        //System.out.println("ultimo codigo plandesarrollo: " + this.codigoNuevo);
        evalactividades = administrarPlanDesarrollo.obtenerActividades();
    }

    public void procesarActividadSelec(AjaxBehaviorEvent event) {
        System.out.println(this.getClass().getName() + ".procesarActividadSelec()");
        System.out.println("" + this.actividadSelec);
        for (EvalActividades cuActividad : evalactividades) {
            if (cuActividad.getSecuencia().equals(new BigInteger(this.actividadSelec))) {
                if ( (new BigInteger("5")).equals(cuActividad.getCodigo()) ){
                    deshabiProfesiones = false;
                    deshabiCursos = true;
                    secCurso = "";
                } else if ( (new BigInteger("6")).equals(cuActividad.getCodigo()) ){
                    deshabiProfesiones = true;
                    deshabiCursos = false;
                    secProfesion = "";
                } else {
                    deshabiProfesiones = true;
                    deshabiCursos = true;
                    secProfesion = "";
                    secCurso = "";
                }
                break;
            }
        }
    }

    public List<EvalActividades> getActividades() {
        return evalactividades;
    }

    public void setActividades(List<EvalActividades> evalac) {
        this.evalactividades = evalac;
    }

    public List<Cursos> getCursos() {
        return cursos;
    }

    public List<Profesiones> getListaProfesiones() {
        return listaProfesiones;
    }

    public void setListaProfesiones(List<Profesiones> listaProfesiones) {
        this.listaProfesiones = listaProfesiones;
    }

    public String getEvaluado() {
        return evaluado;
    }

    public Evaluados getEvaluadoActual() {
        return evaluadoActual;
    }

    public String getConvocatoria() {
        System.out.println("getConvocatoria(): " + convocatoria);
        return convocatoria;
    }

    public List<EvalPlanesDesarrollos> getPlanesDesarrollos() {
        return evalPlanesDesarrollos;
    }

    public void setSeleccionado(EvalPlanesDesarrollos selec) {
        this.seleccionado = selec;
    }

    public EvalPlanesDesarrollos getSeleccionado() {
        return seleccionado;
    }

    public boolean isIsSeleccionadoActividad() {
        return isSeleccionadoActividad;
    }

    public void setIsSeleccionadoActividad(boolean isSeleccionadoActividad) {
        this.isSeleccionadoActividad = isSeleccionadoActividad;
    }

    public BigInteger getSecuenciaPlan() {
        return seleccionado.getSecuencia();
    }

    public boolean isDeshabiProfesiones() {
        return deshabiProfesiones;
    }

    public void setDeshabiProfesiones(boolean deshabiProfesiones) {
        this.deshabiProfesiones = deshabiProfesiones;
    }

    public boolean isDeshabiCursos() {
        return deshabiCursos;
    }

    public void setDeshabiCursos(boolean deshabiCursos) {
        this.deshabiCursos = deshabiCursos;
    }
    

    public void eliminarPlanDesarrollo() {
        if (isSeleccionadoActividad == false) {
            MensajesUI.error("Seleccione la actividad que desea eliminar");
        } else {
            System.out.println("Se dispone a eliminar Evalplanesdesarrollos sec: " + seleccionado.getSecuencia());
            if (seleccionado.getSecuencia() == null) {
                MensajesUI.error("Seleccione la actividad que desea eliminar");
            }
            if (administrarPlanDesarrollo.eliminarPlanDesarrollo(seleccionado.getSecuencia())) {
                MensajesUI.info("Actividad de plan de desarrollo eliminada exitosamente.");
                evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
                this.setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
                isSeleccionadoActividad = false;
            } else {
                MensajesUI.error("No fue posible eliminar la actividad de plan de desarrollo.");
            }
        }
        //if (isSeleccionadoActividad) {

       /* }else{
            MensajesUI.error("Seleccione el plan de desarrollo que desea");
        }*/
    }

    public void registrarPlanDesarrollo() {
        try {
            System.out.println("Creacion nuevo Evalplanesdesarrollos ");
            System.out.println("obtenerUltimoCodigo(this.evaluadoActual.getSecuencia(): " + administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
            setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
            System.out.println("codigo nuevo: " + getCodigo());
            System.out.println("sec EvalResultadoConv: " + evaluadoActual.getSecuencia());
            //System.out.println("secActividad: "+ evalActividad.getSecuencia());
            System.out.println("obs: " + obsPlanDes);
            System.out.println("Curso: " + secCurso);
            System.out.println("Curso: " + secProfesion);
            System.out.println("actividad: " + actividadSelec);
            //System.out.println("secCurso: "+cursoSeleccionado.getSecuencia());
            if (administrarPlanDesarrollo.registrarPlanDesarrollo(codigoNuevo, evaluadoActual.getSecuencia(), actividadSelec, obsPlanDes, secCurso, secProfesion)) {
                MensajesUI.info("Actividad de plan de desarrollo creada exitosamente.");
                evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
                obsPlanDes = "";
                setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
                isSeleccionadoActividad=false;
                isSeleccionadoBitacora=false;
                secuenciaBitacora=null;
                bitacoras=null;
            } else {
                MensajesUI.error("No fue posible registrar la actividad de plan de desarrollo.");
            }
        } catch (Exception e) {
            System.out.println("Error ControladorPlanDesarrollo.registrarPlanDEsarrollo(): " + e.getMessage());
        }
    }

    public void actualizaCod() {
        this.setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
    }
    
    public void actualizaActividades(){
       evalactividades = administrarPlanDesarrollo.obtenerActividades();        
    }
    
    public void actualizaListas(){
        this.setObservacion(null);
        actualizaCod();
        actualizaActividades();
        cargarPlanesDesarrollo();
    }
    
    public void actualizaListasBitacora(){
        bitacoras=administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
        this.setPorcent(null);
        this.setComentario(null);
        setComentario(null);
    }

    public boolean getSecPrueba() {
        return secPrueba;
    }

    public String getSecActividad() {
        return secActividad;
    }

    public void setSecActividad(String secActividad) {
        this.secActividad = secActividad;
    }

    public EvalActividades getEd() {
        return ed;
    }

    public EvalActividades getEvalActividad() {
        return evalActividad;
    }

    public void setEvalActividad(EvalActividades evalActividad) {
        this.evalActividad = evalActividad;
    }

    public Cursos getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public void setCursoSeleccionado(Cursos cursoSeleccionado) {
        this.cursoSeleccionado = cursoSeleccionado;
    }

    public String getEvaluador() {
        return evaluador;
    }

    public void setEvaluador(String evaluador) {
        this.evaluador = evaluador;
    }

    public void setCodigo(BigDecimal codigo) {
        this.codigoNuevo = codigo.add(new BigDecimal(1));
    }

    public BigDecimal getCodigo() {
        return this.codigoNuevo;
    }

    public void setObservacion(String obs) {
        this.obsPlanDes = obs;
    }

    public String getObservacion() {
        return this.obsPlanDes;
    }

    public String getActividadSelec() {
        return actividadSelec;
    }

    public void setActividadSelec(String actividadSelec) {
        this.actividadSelec = actividadSelec;
    }

    public String getSecCurso() {
        return secCurso;
    }

    public void setSecCurso(String secCurso) {
        this.secCurso = secCurso;
    }

    public String getSecProfesion() {
        return secProfesion;
    }

    public void setSecProfesion(String secProfesion) {
        this.secProfesion = secProfesion;
    }

    public void seleccion() {
        int index = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valor"));
        String i = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
        if (i.equals("6")) {
            //evaluado = evaluados.get(index);
            //System.out.println("evaluado para abrir plan de desarrollo: " + evaluadoActual.getNombrePersona());
            System.out.println("Navegando a PLAN DE DESARROLLO...");
            PrimefacesContextUI.ejecutar("seleccionPlan()");
            System.out.println("El valor recibido es: " + index);
            System.out.println("tipo: " + i);
        }
    }

    public void seleccionPlanDesa() {
        System.out.println("ControladorPlanDesarrollo.seleccionPlanDesa()");
        System.out.println("Usuario: " + usuario);
    }

    public Date getFechaseg() {
        return fechaseg;
    }

    public void setFechaseg(Date fechaseg) {
        this.fechaseg = fechaseg;
    }

    public String getPorcent() {
        return porcent;
    }

    public void setPorcent(String porcent) {
        this.porcent = porcent;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    public void registrarBitacora() {
        if (isSeleccionadoActividad==false) {
            MensajesUI.error("Seleccione la actividad del plan de desarrolla a la que se le va a añadir la bitácora.");
        }else{
        try {
            System.out.println("Creacion nueva bitacora ");
            //System.out.println("sec EvalResultadoConv: " + evaluadoActual.getSecuencia());
            //System.out.println("secActividad: "+ evalActividad.getSecuencia());
            System.out.println("fecha seguimiento: " + fechaseg );
            System.out.println("porcentaje: " + porcent);
            System.out.println("comentario: " + comentario);
            System.out.println("Secuencia plandesarrollo: "+secPlanDesarrollo);
            //System.out.println("secCurso: "+cursoSeleccionado.getSecuencia());
            if (administrarPlanDesarrollo.registrarBitacora(secPlanDesarrollo, fechaseg, comentario, porcent)) {
                if(isSeleccionadoActividad==true){ // si hay actividad seleccionada, cargar los planes correspondientes
                    bitacoras=administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);                    
                }else{
                    bitacoras=null;
                }
                MensajesUI.info("Bitacora creada exitosamente.");
                porcent="";
                comentario = "";
            } else {
                MensajesUI.error("No fue posible registrar la bitácora.");
            }
        } catch (Exception e) {
            System.out.println("Error ControladorPlanDesarrollo.registrarBitacora(): " + e.getMessage());
        }
        }
    } 
    
    public void prueba(){
        System.out.println("Prueba, se selecciono un registro!!!");
        //bitacoras=administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
        System.out.println("Secuencia plandesa: "+seleccionado.getSecuencia());
    }

    public EvalSeguimientosPD getSeleccionadoBitacora() {
        return seleccionadoBitacora;
    }

    public void setSeleccionadoBitacora(EvalSeguimientosPD seleccionadoBitacora) {
        this.seleccionadoBitacora = seleccionadoBitacora;
    }

    public List<EvalSeguimientosPD> getBitacoras() {
        return bitacoras;
    }

    public void setBitacoras(List<EvalSeguimientosPD> bitacoras) {
        this.bitacoras = bitacoras;
    }
    
    public void seleccionPlan(int num){ //Cuando se seleccione alguna actividad del plan de desarrollo
        //bitacoras=administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
        switch (num) {
            case 1:
                System.out.println("Se ha seleccionado una actividad del plan de desarrollo "+ seleccionado.getSecuencia());
                bitacoras=administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
                isSeleccionadoActividad=true;
                secPlanDesarrollo=seleccionado.getSecuencia();
                this.setearFechaActualBitacora();
                System.out.println("Se seleccionó el plan de desarrollo: "+secPlanDesarrollo);
                break;
            case 2:
                bitacoras=null;
                isSeleccionadoActividad=false;
                isSeleccionadoBitacora=false;
                break;
            default:
                bitacoras=null;
                isSeleccionadoBitacora=false;
                break;
        }
    }
    
    public void seleccionBitacora(int tipo){
        switch (tipo) {
            case 1:
                System.out.println("Se seleccionó la bitacora: " + seleccionadoBitacora.getSecuencia()+" "+seleccionadoBitacora.getComentario());
                isSeleccionadoBitacora=true;
                setIsSeleccionadoBitacora(true);
                secuenciaBitacora=seleccionadoBitacora.getSecuencia();
                break;
            case 2:
                //seleccionadoBitacora.setSecuencia(null);
                System.out.println("caso 2");
                isSeleccionadoBitacora=false;
                secuenciaBitacora=null;
                break;
            default:
                //seleccionadoBitacora.setSecuencia(null);
                System.out.println("caso por defecto");
                isSeleccionadoBitacora=false;
                secuenciaBitacora=null;
                break;
        }
    }
    
    public void eliminarBitacora(){
        System.out.println("Se dispone a eliminar EvalSeguimientosPD ");
        if (isSeleccionadoBitacora==false) {
            MensajesUI.error("Seleccione la bitácora que desea eliminar.");
        }else{
            try {
                if (administrarPlanDesarrollo.eliminarBitacora(secuenciaBitacora)) {
                    MensajesUI.info("Bitácora eliminada exitosamente");
                    bitacoras=administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
                    secuenciaBitacora=null;
                    isSeleccionadoBitacora=false;
                    this.setIsSeleccionadoBitacora(false);
                    comentario="";
                    porcent=null;
                }else{
                    MensajesUI.error("No fue posible eliminar la bitácora.");
                }
            } catch (Exception e) {
                System.out.println("Error eliminarBitacora(): "+e.getMessage());
            }
        }
    }

    public boolean isIsSeleccionadoBitacora() {
        return isSeleccionadoBitacora;
    }

    public void setIsSeleccionadoBitacora(boolean isSeleccionadoBitacora) {
        this.isSeleccionadoBitacora = isSeleccionadoBitacora;
    }
    
    public void onDateSelect(SelectEvent event) {
           System.out.println("Fecha seleccionada: " + event.getObject());
           System.out.println("Fecha: "+fechaseg);         
       }    

    public void setearFechaActualBitacora() {
        Calendar cl = Calendar.getInstance();
        String f = cl.get(Calendar.YEAR) + "/" + (cl.get(Calendar.MONTH) + 1) + "/" + cl.get(Calendar.DAY_OF_MONTH);
        System.out.println("Fecha modify: " + f);
        fechaseg=cl.getTime();
    }

    public BigInteger getSecuenciaBitacora() {
        return secuenciaBitacora;
    }

    public void setSecuenciaBitacora(BigInteger secuenciaBitacora) {
        this.secuenciaBitacora = secuenciaBitacora;
    }
    
    
    
}
