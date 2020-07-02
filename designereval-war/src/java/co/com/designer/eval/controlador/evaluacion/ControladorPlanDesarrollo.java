package co.com.designer.eval.controlador.evaluacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.administrar.interfaz.IAdministrarInicioEval;
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
    private IAdministrarInicioEval administrarInicioEval; //200610
    @EJB
    private IAdministrarPlanDesarrollo administrarPlanDesarrollo;
    @EJB
    private IAdministrarInicio administrarInicio;
    private List<EvalActividades> evalactividades;
    private List<Cursos> cursos;
    private List<Profesiones> listaProfesiones;
    private List<Convocatorias> convocatorias;
    private List<EvalPlanesDesarrollos> evalPlanesDesarrollos;
    private List<EvalSeguimientosPD> bitacoras;
    private List<Evaluados> evaluados; // usando en la versión móvil
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
    private boolean deshabiProfesiones=true;
    private boolean deshabiCursos=true;
    
    //bitacora
    private Date fechaseg;
    private String porcent="0";
    private String comentario;
    private EvalSeguimientosPD seleccionadoBitacora;
    private boolean isSeleccionadoBitacora=false; //si hay una bitacora seleccionada
    private BigInteger secuenciaBitacora;
    
    private boolean habilitaEliminarActividad=false;
    private boolean habilitaEliminarBitacora=false;
    private int tipoActividadNueva=0; // si es formal (5), no formal(6), cualquier otra (0)
    private String comentarioBitacoraEdit="Digite una observaciòn"; // comentarioEdit guardará el comentario de la bitacora seleccionada para mostrar en el formulario editar
    private Date fechaBitacoraEdit=null; //guardará el valor de la fecha de la bitacora seleccionada
    private String porcenBitacoraEdit="0"; // guardará el valor del porcentaje de la bitácora seleccionada
    private String periodicidad="TRES MESES"; // Por defecto 3 meses
    private String msj=""; 
    public int opcBitacora = 0; // 0 = Nueva bitácora, 1 = Editar Bitácora versión móvil
    
    
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
            convocatorias = administrarPlanDesarrollo.obtenerConvocatoriasAlcance(usuario);
            convocatoriaActualBigDecimal = ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).getSecConvocatoria();
            System.out.println("Convocatoria actual: " + convocatoriaActualBigDecimal);
            convocatoriaActual = (Convocatorias)((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).getConvocatoria();
            evaluador = (String) ((ControladorInicioEval) x.getApplication().evaluateExpressionGet(x, "#{controladorInicioEval}", ControladorInicioEval.class)).obtenerInformacion(1);
            cursos = administrarPlanDesarrollo.obtenerCursos();
            listaProfesiones = administrarPlanDesarrollo.obtenerProfesiones();
            evalactividades = administrarPlanDesarrollo.obtenerActividades();
            bitacoras=null; //Inicialmente no mostrar bitacoras
            isSeleccionadoActividad=false;
            isSeleccionadoBitacora=false;
            secuenciaBitacora=null;
            setearFechaActualBitacora();
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
        inicializarAdministrador();
        System.out.println("cargarPlanesDesarrollo()");
        System.out.println("evaluadoActual: " + this.evaluadoActual.getNombrePersona());
        System.out.println("secuenciaEvaluado: " + this.evaluadoActual.getSecuencia());
        evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
        setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
        //System.out.println("ultimo codigo plandesarrollo: " + this.codigoNuevo);
        System.out.println("Convocatoria seleccionada: "+convocatoriaActual.getCodigo()+"-"+convocatoriaActual.getEnfoque());
        evalactividades = administrarPlanDesarrollo.obtenerActividades();
        habilitaEliminarActividad=hasEvalPlanesDesarrollos();
        habilitaEliminarBitacora=hasBitacoras();
    }
    
    public void actividadseleccionadaMovil(){
        System.out.println("Actividad seleccionada: "+actividadSelec);
    }

    public void procesarActividadSelec(AjaxBehaviorEvent event) {
        System.out.println(this.getClass().getName() + ".procesarActividadSelec()");
        System.out.println("" + this.actividadSelec);
        for (EvalActividades cuActividad : evalactividades) {
            if (cuActividad.getSecuencia().equals(new BigInteger(this.actividadSelec))) {
                if ( (new BigInteger("5")).equals(cuActividad.getCodigo()) ){
                    System.out.println("Tipo de actividad: FORMAL (5)");
                    deshabiProfesiones = false;
                    deshabiCursos = true;
                    secCurso = "";
                    tipoActividadNueva=5;
                } else if ( (new BigInteger("6")).equals(cuActividad.getCodigo()) ){
                    System.out.println("Tipo de actividad: NO FORMAL (6)");
                    deshabiProfesiones = true;
                    deshabiCursos = false;
                    secProfesion = "";
                    tipoActividadNueva=6;
                } else {
                    System.out.println("Tipo de actividad: otra diferente a formal y no formal");
                    deshabiProfesiones = true;
                    deshabiCursos = true;
                    secProfesion = "";
                    secCurso = "";
                    tipoActividadNueva=0;
                }
                break;
            }
        }
    }

    public void procesarActividadSelecMovil() {
        System.out.println(this.getClass().getName() + ".procesarActividadSelecMovil()");
        System.out.println("" + this.actividadSelec);
        for (EvalActividades cuActividad : evalactividades) {
            if (cuActividad.getSecuencia().equals(new BigInteger(this.actividadSelec))) {
                if ( (new BigInteger("5")).equals(cuActividad.getCodigo()) ){
                    System.out.println("Tipo de actividad: FORMAL (5)");
                    deshabiProfesiones = false;
                    deshabiCursos = true;
                    secCurso = "";
                    tipoActividadNueva=5;
                    isSeleccionadoActividad=true;
                } else if ( (new BigInteger("6")).equals(cuActividad.getCodigo()) ){
                    System.out.println("Tipo de actividad: NO FORMAL (6)");
                    deshabiProfesiones = true;
                    deshabiCursos = false;
                    secProfesion = "";
                    tipoActividadNueva=6;
                    isSeleccionadoActividad=true;
                } else {
                    System.out.println("Tipo de actividad: otra diferente a formal y no formal");
                    deshabiProfesiones = true;
                    deshabiCursos = true;
                    secProfesion = "";
                    secCurso = "";
                    tipoActividadNueva=0;
                    isSeleccionadoActividad=true;
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
    
    public boolean isIsSeleccionadoBitacora() {
        return isSeleccionadoBitacora;
    }

    public void setIsSeleccionadoBitacora(boolean isSeleccionadoBitacora) {
        this.isSeleccionadoBitacora = isSeleccionadoBitacora;
    }
    
    public void onDateSelect(SelectEvent event) {
        System.out.println("Fecha seleccionada: " + event.getObject());
        System.out.println("Fecha: " + fechaseg);
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
    
        public boolean isHabilitaEliminarActividad() {
        return habilitaEliminarActividad;
    }

    public void setHabilitaEliminarActividad(boolean habilitaEliminarActividad) {
        this.habilitaEliminarActividad = habilitaEliminarActividad;
    }

    public boolean isHabilitaEliminarBitacora() {
        return habilitaEliminarBitacora;
    }

    public void setHabilitaEliminarBitacora(boolean habilitaEliminarBitacora) {
        this.habilitaEliminarBitacora = habilitaEliminarBitacora;
    }
    
    public String getComentarioBitacoraEdit() {
        return comentarioBitacoraEdit;
    }

    public void setComentarioBitacoraEdit(String comentarioBitacoraEdit) {
        this.comentarioBitacoraEdit = comentarioBitacoraEdit;
    }

    public Date getFechaBitacoraEdit() {
        return fechaBitacoraEdit;
    }

    public void setFechaBitacoraEdit(Date fechaBitacoraEdit) {
        this.fechaBitacoraEdit = fechaBitacoraEdit;
    }

    public String getPorcenBitacoraEdit() {
        return porcenBitacoraEdit;
    }

    public void setPorcenBitacoraEdit(String porcenBitacoraEdit) {
        this.porcenBitacoraEdit = porcenBitacoraEdit;
    } 

    public List<Evaluados> getEvaluados() { // 200609
        return evaluados;
    }

    public void setEvaluados(List<Evaluados> evaluados) {
        this.evaluados = evaluados;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        this.periodicidad = periodicidad;
    }

    public int getTipoActividadNueva() {
        return tipoActividadNueva;
    }

    public void setTipoActividadNueva(int tipoActividadNueva) {
        this.tipoActividadNueva = tipoActividadNueva;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }

    public int getOpcBitacora() {
        return opcBitacora;
    }

    public void seleccion() { // Cargar la pantalla en la versión Móvil
        try {
            inicializarAdministrador();
            int index = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valor"));
            String i = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
            if (i.equals("6")) {
                System.out.println("Navegando a PLAN DE DESARROLLO...");
                System.out.println("Evaluador: "+evaluador);
                convocatoriaActual = convocatorias.get(index);
                System.out.println("Convocatoria: "+convocatoriaActual.getEnfoque()+" - "+convocatoriaActual.getCodigo()+" secuencia: "+convocatoriaActual.getSecuencia());
                System.out.println("Parametros= usuario: "+usuario+ " - convocatoria: "+convocatoriaActual.getSecuencia());
                evaluados = administrarInicio.obtenerEvaluados(usuario, convocatoriaActual.getSecuencia());
                System.out.println("El valor recibido es: " + index);
                System.out.println("tipo: " + i);
                PrimefacesContextUI.ejecutar("seleccionPlan()");                
            } else if (i.equals("7")){
                System.out.println("Navegando a Bitácoras");
                System.out.println("El valor recibido es: " + index);
                System.out.println("tipo: " + i);
                seleccionado=evalPlanesDesarrollos.get(index);
                secPlanDesarrollo=seleccionado.getSecuencia();
                System.out.println("Evalplandesarrollo: "+seleccionado.getSecuencia()+ " "+seleccionado.getEvalActividad().getDescripcion());
                bitacoras=administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
                PrimefacesContextUI.ejecutar("seleccionBitacora()"); 
            }else if(i.equals("8")){
                seleccionadoBitacora=bitacoras.get(index);
                System.out.println("Se dispone a eliminar: "+ seleccionadoBitacora.getComentario());
                //PrimefacesContextUI.ejecutar("PF('opcionesReporteEvaluado').show();");

            } else if (i.equals("10")){
                seleccionadoBitacora=bitacoras.get(index);
                System.out.println("Se dispone a editar bitácora: "+ seleccionadoBitacora.getComentario());
                //PrimefacesContextUI.ejecutar("pantallaNuevaBitacora()"); 
            }
        } catch (Exception e) {
            System.out.println("Error en ControladorPlanDesarrollo.seleccion(): "+e.getMessage());
        }
    }
    
    public void seleccionPlanDesa() { //Validar si este metodo debe ir en ControladorPlanDesarrollo
        System.out.println("ControladorPlanDesarrollo.seleccionPlanDesa()");
        cargarPlanesDesarrollo();
        System.out.println("evaluado.getSecuencia() " + evaluadoActual.getSecuencia());
    }
    
    public void seleccionBitacoraMovil(){
        System.out.println("ControladorPlanDesarrollo.seleccionBitacoraMovil()");
        System.out.println("Total bitacoras: "+bitacoras.size());
        //comentario="";
        //setearFechaActualBitacora();
        //porcent="0";
    }
    
    public boolean validaNumPorcentaje(String porcentaje){
        System.out.println("Validando valor pocentaje valido...");
        try {
            int porcentajenum=Integer.parseInt(porcentaje);
            System.out.println("Porcentaje: "+porcentajenum);
            if (porcentajenum<0 || porcentajenum>100) {
                 //MensajesUI.error("El porcentaje debe ser un valor entre 0 y 100.");
                 System.out.println("Porcentaje no valido");
                 return false;
            }else{
                System.out.println("Porcentaje valido");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error ControladorPlanDesarrollo.validaNumPorcentaje: "+e.getMessage());
            return false;
        }
        
    }
    
    public void registrarBitacora() {
        if (isSeleccionadoActividad == false) {
            MensajesUI.error("Seleccione la actividad del plan de desarrolla a la que se le va a añadir la bitácora.");
        } else {
            if (fechaseg != null) {
            if (validaNumPorcentaje(porcent)) {
                if (comentario!="" && comentario!=null) {
                    try {
                        System.out.println("Creacion nueva bitacora ");
                        //System.out.println("sec EvalResultadoConv: " + evaluadoActual.getSecuencia());
                        //System.out.println("secActividad: "+ evalActividad.getSecuencia());
                        System.out.println("fecha seguimiento: " + fechaseg);
                        System.out.println("porcentaje: " + porcent);
                        System.out.println("comentario: " + comentario);
                        System.out.println("Secuencia plandesarrollo: " + secPlanDesarrollo);
                        //System.out.println("secCurso: "+cursoSeleccionado.getSecuencia());
                        if (administrarPlanDesarrollo.registrarBitacora(secPlanDesarrollo, fechaseg, comentario, porcent)) {
                            bitacoras = administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
                            isSeleccionadoBitacora = false;
                            MensajesUI.info("Bitacora creada exitosamente.");
                            porcent = "0";
                            comentario = "";
                            setearFechaActualBitacora();
                            PrimefacesContextUI.ejecutar("PF('alertanuevabitacora').hide();");
                        } else {
                            isSeleccionadoBitacora=false;
                            MensajesUI.error("No fue posible registrar la bitácora.");
                            comentario="";
                            porcent="0";
                            setearFechaActualBitacora();
                        }
                    } catch (Exception e) {
                        System.out.println("Error ControladorPlanDesarrollo.registrarBitacora(): " + e.getMessage());
                    }
                }else{
                    MensajesUI.error("Debe digitar una observación en el campo de aprendizaje adquirido (Máximo 500 carácteres");
                }
            }else{
               MensajesUI.error("El valor del porcentaje debe ser un valor númerico entre 0 y 100.");
            }
            }else{
                MensajesUI.error("La fecha no puede ser vacía.");
            }
        }
    }
    
    public void registrarBitacoraMovil() {
        try {
            System.out.println("Creacion nueva bitacora ");
            //System.out.println("sec EvalResultadoConv: " + evaluadoActual.getSecuencia());
            //System.out.println("secActividad: "+ evalActividad.getSecuencia());
            System.out.println("fecha seguimiento: " + fechaseg);
            System.out.println("porcentaje: " + porcent);
            System.out.println("comentario: " + comentario);
            System.out.println("Secuencia plandesarrollo: " + secPlanDesarrollo);
            //System.out.println("secCurso: "+cursoSeleccionado.getSecuencia());
            if (administrarPlanDesarrollo.registrarBitacora(secPlanDesarrollo, fechaseg, comentario, porcent)) {
                bitacoras = administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
                isSeleccionadoBitacora = false;
                //MensajesUI.info("Bitacora creada exitosamente.");
                porcent = "0";
                comentario = null;
                setearFechaActualBitacora();
                PrimefacesContextUI.ejecutar("PF('alertaInfoBitacora').hide();"); 
                PrimefacesContextUI.ejecutar("PF('alertaBitacoraCreada').show();"); 
            } else {
                isSeleccionadoBitacora = false;
                //MensajesUI.error("No fue posible registrar la bitácora.");
                comentario = null;
                porcent = "0";
                setearFechaActualBitacora();
                PrimefacesContextUI.ejecutar("PF('alertaInfoBitacora').hide();"); 
                PrimefacesContextUI.ejecutar("PF('alertaBitacoraError').show();"); 
            }
        } catch (Exception e) {
            System.out.println("Error ControladorPlanDesarrollo.registrarBitacora(): " + e.getMessage());
        }  
    }
    
        public void editarBitacoraMovil() {
        System.out.println("editarBitacoraMovil() ");
        try {
            System.out.println("fecha seguimiento: " + fechaseg + "porcentaje: "+porcent+ " comentario: "+comentario+ "secuencia bitácora: "+secuenciaBitacora);
            if (administrarPlanDesarrollo.editarBitacora(secuenciaBitacora, fechaseg, comentario, Integer.parseInt(porcent))) {
                bitacoras = administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
                isSeleccionadoBitacora = false;
                System.out.println("Bitacora editada exitosamente");
                PrimefacesContextUI.ejecutar("PF('alertaInfoEditarBitacora').hide();"); 
                PrimefacesContextUI.ejecutar("PF('alertaBitacoraEditada').show();"); 
            } else {
                isSeleccionadoBitacora = false;
                setearFechaActualBitacora();
                PrimefacesContextUI.ejecutar("PF('alertaInfoEditarBitacora').hide();"); 
                PrimefacesContextUI.ejecutar("PF('alertaBitacoraEditadaError').show();"); 
            }
        } catch (Exception e) {
            System.out.println("Error ControladorPlanDesarrollo.editarBitacora(): " + e.getMessage());
        }  
    }
    
    public boolean validacionNuevaBitacoraMovil() {
        System.out.println("validacionNuevaBitacoraMovil");
        boolean validInfo = false;
        this.msj = "";
        System.out.println("comentario : "+this.comentario);
        if (fechaseg != null) {
            if (validaNumPorcentaje(porcent)) {
                if (comentario != "" && comentario != null && comentario.length()>0 && comentario.length()<=500) {
                    System.out.println("Comentario: "+comentario);
                    validInfo=true;
                } else {
                    this.msj+="Debe digitar una observación en el campo de aprendizaje adquirido (Máximo 500 carácteres). ";
                    if (comentario!=null && comentario.length()>0 && comentario.length() > 500) {
                        this.msj += "Ha digitado " + comentario.length() + " carácteres.";
                    }
                    System.out.println("Comentario: "+comentario);  
                    validInfo=false;
                }
            } else {
                this.msj+="El valor del porcentaje debe ser un valor númerico entre 0 y 100.";
                validInfo=false;
            }
        } else {
            this.msj+="La fecha no puede ser vacía.";
            validInfo=false;
        }
        System.out.println("validInfo: "+validInfo);
        return validInfo;
    }
    
    public void editarBitacora() {
        if (isSeleccionadoBitacora == false) {
            MensajesUI.error("Seleccione la bitácora que va a editar.");
        } else {
           if (fechaBitacoraEdit != null) {
                if (validaNumPorcentaje(porcenBitacoraEdit)) {
                    if (comentarioBitacoraEdit != "" && comentarioBitacoraEdit != null) {
                        try {
                            System.out.println("edicion bitacora ");
                            //System.out.println("sec EvalResultadoConv: " + evaluadoActual.getSecuencia());
                            //System.out.println("secActividad: "+ evalActividad.getSecuencia());
                            System.out.println("fecha seguimiento: " + fechaBitacoraEdit);
                            System.out.println("porcentaje: " + porcenBitacoraEdit);
                            System.out.println("comentario: " + comentarioBitacoraEdit);
                            System.out.println("Secuencia plandesarrollo: " + secPlanDesarrollo);
                            System.out.println("Secuencia bitacora: " + secuenciaBitacora);
                            //System.out.println("secCurso: " + cursoSeleccionado.getSecuencia());
                            System.out.println("secuencia Bitacora a actualizar: " + secuenciaBitacora);
                            if (administrarPlanDesarrollo.editarBitacora(secuenciaBitacora, fechaBitacoraEdit, comentarioBitacoraEdit, Integer.parseInt(porcenBitacoraEdit))) {
                                bitacoras = administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
                                MensajesUI.info("Bitacora modificada exitosamente.");
                                porcenBitacoraEdit = "0";
                                comentarioBitacoraEdit = "";
                                isSeleccionadoBitacora = false;
                                PrimefacesContextUI.ejecutar("PF('alertaeditabitacora').hide();");
                            } else {
                                MensajesUI.error("No fue posible registrar la bitácora.");
                                PrimefacesContextUI.ejecutar("PF('alertaeditabitacora').hide();");
                                isSeleccionadoBitacora=false;
                            }
                        } catch (Exception e) {
                            System.out.println("Error ControladorPlanDesarrollo.editarBitacora(): " + e.getMessage());
                        }
                    } else {
                        MensajesUI.error("Debe digitar una observación en el campo de aprendizaje adquirido (Máximo 500 carácteres");
                    }
                } else {
                    MensajesUI.error("El valor del porcentaje debe ser un valor númerico entre 0 y 100.");
                }
            } else {
                MensajesUI.error("La fecha no puede ser vacía.");
            }
        }
    }
        
    public void prueba(){
        System.out.println("Prueba, se selecciono un registro!!!");
        //bitacoras=administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
        System.out.println("Secuencia plandesa: "+seleccionado.getSecuencia());
    }
    
    public void seleccionPlan(int num){ //Cuando se seleccione alguna actividad del plan de desarrollo
        //bitacoras=administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
        switch (num) {
            case 1:
                System.out.println("Se ha seleccionado una actividad del plan de desarrollo "+ seleccionado.getSecuencia());
                bitacoras=administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
                isSeleccionadoActividad=true;
                secPlanDesarrollo=seleccionado.getSecuencia();
                habilitaEliminarActividad=hasEvalPlanesDesarrollos();
                habilitaEliminarBitacora=hasBitacoras();
                isSeleccionadoBitacora=false;
                secuenciaBitacora=null;
                this.setearFechaActualBitacora();
                System.out.println("Se seleccionó el plan de desarrollo: "+secPlanDesarrollo);
                habilitaEliminarActividad=hasEvalPlanesDesarrollos();
                habilitaEliminarBitacora=hasBitacoras();
                break;
            case 2:
                bitacoras=null;
                secuenciaBitacora=null;
                secPlanDesarrollo=null;
                isSeleccionadoActividad=false;
                isSeleccionadoBitacora=false;
                System.out.println("SeleccionoPlan(2)");
                habilitaEliminarActividad=false;
                break;
            default:
                bitacoras=null;
                secuenciaBitacora=null;
                secPlanDesarrollo=null;
                isSeleccionadoActividad=false;
                isSeleccionadoBitacora=false;
                System.out.println("Por defecto plan no seleccionado");
                habilitaEliminarActividad=false;
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
                //comentario=seleccionadoBitacora.getComentario();
                habilitaEliminarBitacora=hasBitacoras();
                comentarioBitacoraEdit=seleccionadoBitacora.getComentario();
                fechaBitacoraEdit=seleccionadoBitacora.getFecha();
                porcenBitacoraEdit=seleccionadoBitacora.getPorcentaje()+"";
                comentario="";
                porcent="0";
                setearFechaActualBitacora();
                break;
            case 2:
                //seleccionadoBitacora.setSecuencia(null);
                System.out.println("caso 2");
                isSeleccionadoBitacora=false;
                secuenciaBitacora=null;
                comentario="";
                porcent="0";
                setearFechaActualBitacora();
                break;
            default:
                //seleccionadoBitacora.setSecuencia(null);
                System.out.println("caso por defecto");
                isSeleccionadoBitacora=false;
                secuenciaBitacora=null;
                comentario="";
                setearFechaActualBitacora();
                porcent="0";
                break;
        }
    }
    
    public void eliminarBitacora() {
        System.out.println("Se dispone a eliminar EvalSeguimientosPD "+secuenciaBitacora+ " del plan de desarrollo: "+secPlanDesarrollo);
        if (isSeleccionadoBitacora == false) {
            MensajesUI.error("Seleccione la bitácora que desea eliminar.");
        } else {
                try {
                    if (administrarPlanDesarrollo.eliminarBitacora(secuenciaBitacora)) {
                        MensajesUI.info("Bitácora eliminada exitosamente");
                        bitacoras = administrarPlanDesarrollo.obtenerBitacoras(seleccionado.getSecuencia());
                        secuenciaBitacora = null;
                        isSeleccionadoBitacora = false;
                        this.setIsSeleccionadoBitacora(false);
                        comentario = "";
                        porcent = null;
                    } else {
                        MensajesUI.error("No fue posible eliminar la bitácora.");
                    }
                } catch (Exception e) {
                    System.out.println("Error eliminarBitacora(): " + e.getMessage());
                }
        }
        bitacoras=administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
        isSeleccionadoBitacora=false;
        habilitaEliminarActividad=hasEvalPlanesDesarrollos();
        habilitaEliminarBitacora=hasBitacoras();
    }
    
    public void eliminarBitacoraMovil() {
        if (administrarPlanDesarrollo.eliminarBitacora(seleccionadoBitacora.getSecuencia())) {
            MensajesUI.info("Bitácora eliminada exitosamente.");
            System.out.println("Bitácora eliminada exitosamente.");
            // evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
            bitacoras = administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
            isSeleccionadoBitacora = false;
            PrimefacesContextUI.ejecutar("PF('alertaInfoElimBitacora').hide();");
            PrimefacesContextUI.ejecutar("PF('alertaBitacoraEliminada').show();");
            //PrimefacesContextUI.ejecutar("actualizaPlanes()");
        } else {
            MensajesUI.error("No fue posible eliminar la bitácora.");
            System.out.println("No fue posible eliminar la bitácora.");
            PrimefacesContextUI.ejecutar("PF('alertaInfoElimBitacora').hide();");
            PrimefacesContextUI.ejecutar("PF('alertaBitacoraElimError').show();");
        }
        isSeleccionadoBitacora = false;
        secuenciaBitacora =  null;
        habilitaEliminarActividad = hasEvalPlanesDesarrollos();
        habilitaEliminarBitacora = hasBitacoras();
        evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
        bitacoras = administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
    }
    
    public void refrescaListas(){ // todos los valores que deban reiniciarse al abrir la pantalla
        isSeleccionadoActividad=false;
        isSeleccionadoBitacora=false;
        secPlanDesarrollo=null;
        secuenciaBitacora=null;
        cargarPlanesDesarrollo();
        bitacoras=null;
    }
    
    public boolean hasBitacoras() { // Validar si una actividad tiene bitacoras asignadas
        int count = administrarPlanDesarrollo.cantidadBitacoras(secPlanDesarrollo).intValue();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
    
     public boolean hasEvalPlanesDesarrollos() { // Validar si un empleado tiene actividades asignadas
        int count = administrarPlanDesarrollo.cantidadEvalPlanesDesarrollos(evaluadoActual.getSecuencia()).intValue();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
     }
    
    public void editaBitacora() {
        System.out.println("Click en botón editar");
        System.out.println("SecBitacora: "+seleccionadoBitacora.getSecuencia()+ " Seleccionada bitacora:" + isSeleccionadoBitacora + " "+seleccionadoBitacora.getSecuencia());
    }
    
    public boolean validacionNuevaActividadMovil() {
        boolean validInfo = false;
        this.msj = "";
        System.out.println("obs: "+obsPlanDes);
        System.out.println("getOBservacion: "+this.getObservacion());
        if (actividadSelec != null && actividadSelec != "") {
            switch (tipoActividadNueva) {
                case 5:
                    System.out.println("Seleccionó capacitación formal");
                    if ((secProfesion != null && secProfesion != "") || (obsPlanDes != null && obsPlanDes != "" && obsPlanDes.length() > 0 && obsPlanDes.length() <= 500)) {
                        validInfo = true;
                    } else {
                        this.msj += "Seleccione un curso para capacitación formal ó digite una observación de máximo 500 carácteres. \n";
                    }
                    break;
                case 6:
                    System.out.println("Seleccionó capacitación no formal");
                    if ((secCurso != null && secCurso != "") || (obsPlanDes != null && obsPlanDes != "" && obsPlanDes.length() > 0 && obsPlanDes.length() <= 500)) {
                        validInfo = true;
                    } else {
                        this.msj += "Seleccione un curso para capacitación no formal ó digite una observación \n";
                    }
                    break;
                default:
                    System.out.println("Seleccionó una capacitación x");
                    if (obsPlanDes == null || obsPlanDes == "" || obsPlanDes.length() <= 0 || obsPlanDes.length() > 500) {
                        this.msj = "Digite una observación de máximo 500 carácteres \n";
                    } else {
                        validInfo = true;
                    }
                    break;
            }
        }
        return validInfo;
    }
    
        public void eliminarPlanDesarrollo() {
        if (isSeleccionadoActividad == false) {
            MensajesUI.error("Seleccione la actividad que desea eliminar");
        } else {
            if (hasBitacoras()) {
                MensajesUI.error("No es posible eliminar un plan de desarrollo con bitácoras asignadas.");
                evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia()); 
                bitacoras=null;
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
        }
        isSeleccionadoActividad=false;
        isSeleccionadoBitacora=false;
        secPlanDesarrollo=null;
        habilitaEliminarActividad=hasEvalPlanesDesarrollos();
        habilitaEliminarBitacora=hasBitacoras();
        evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());        
    }
    
    public void eliminarPlanDesarrolloMovil() {
                if (administrarPlanDesarrollo.eliminarPlanDesarrollo(seleccionado.getSecuencia())) {
                    MensajesUI.info("Actividad de plan de desarrollo eliminada exitosamente.");
                    System.out.println("Actividad de plan de desarrollo eliminada exitosamente.");
                    evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
                    this.setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
                    isSeleccionadoActividad = false;
                    PrimefacesContextUI.ejecutar("PF('alertaInfoElimActividad').hide();");
                    PrimefacesContextUI.ejecutar("PF('alertaActividadEliminada').show();");
                    //PrimefacesContextUI.ejecutar("actualizaPlanes()");
                } else {
                    MensajesUI.error("No fue posible eliminar la actividad de plan de desarrollo.");
                    System.out.println("No fue posible eliminar la actividad de plan de desarrollo.");
                    PrimefacesContextUI.ejecutar("PF('alertaInfoElimActividad').hide();");
                    PrimefacesContextUI.ejecutar("PF('alertaActividadElimError').show();");
                }
        isSeleccionadoActividad=false;
        isSeleccionadoBitacora=false;
        secPlanDesarrollo=null;
        habilitaEliminarActividad=hasEvalPlanesDesarrollos();
        habilitaEliminarBitacora=hasBitacoras();
        evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());        
    }
    
    public void seleccionEliminarPlanMovil() {
        int index = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valor"));
        String i = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
        System.out.println("Recibido valor: " + index + " - tipo: " + i);
        System.out.println("seleccionEliminarPlanMovil");
        seleccionado = evalPlanesDesarrollos.get(index);
        secPlanDesarrollo = seleccionado.getSecuencia();
        System.out.println("plan seleccionado: " + seleccionado.getObservacion());
        PrimefacesContextUI.ejecutar("PF('alertaInfoElimActividad').show();");
    }

    public void seleccionaBitacoraMovil() { // selecciona la bitácora para ser eliminada o editada
        int index = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("valor"));
        String i = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
        System.out.println("Recibido valor: " + index + " - tipo: " + i);
        seleccionadoBitacora = bitacoras.get(index);
        secuenciaBitacora = seleccionadoBitacora.getSecuencia();
        if (i.equals("9")) {
            System.out.println("seleccionEliminarBitacoraMovil");
            PrimefacesContextUI.ejecutar("PF('alertaInfoElimBitacora').show();");
        } else if (i.equals("10")) {
            opcBitacora = 1;
            System.out.println("editar bitácora");
            fechaseg = seleccionadoBitacora.getFecha();
            comentario = seleccionadoBitacora.getComentario();
            porcent = seleccionadoBitacora.getPorcentaje()+"";
            PrimefacesContextUI.ejecutar("pantallaNuevaBitacora()"); 
        }
        System.out.println("bitacora seleccionada: " +comentario);

    }
    
    public void registrarPlanDesarrolloMovil(){
            System.out.println("registrarPlanDesarrolloMovil ");
            System.out.println("obtenerUltimoCodigo(this.evaluadoActual.getSecuencia(): " + administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
            setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
            System.out.println("codigo nuevo: " + getCodigo());
            System.out.println("sec EvalResultadoConv: " + evaluadoActual.getSecuencia());
            //System.out.println("secActividad: "+ evalActividad.getSecuencia());
            System.out.println("obs: " + obsPlanDes);
            System.out.println("Curso: " + secCurso);
            System.out.println("Profesion: " + secProfesion);
            System.out.println("actividad: " + actividadSelec);
            System.out.println("Periodicidad: "+periodicidad);
            if (administrarPlanDesarrollo.registrarPlanDesarrollo(codigoNuevo, evaluadoActual.getSecuencia(), actividadSelec, obsPlanDes, secCurso, secProfesion, periodicidad)) {
                        MensajesUI.info("Actividad de plan de desarrollo creada exitosamente.");
                        this.msj="Actividad de plan de desarrollo creada exitosamente";
                        evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
                        obsPlanDes = null;
                        setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
                        isSeleccionadoActividad = false;
                        isSeleccionadoBitacora = false;
                        secuenciaBitacora = null;
                        bitacoras = null;
                        periodicidad="TRES MESES";
                        this.setIsSeleccionadoActividad(false);
                        PrimefacesContextUI.ejecutar("PF('alertaInfoActividad').hide();");
                        PrimefacesContextUI.ejecutar("PF('alertaActividadCreada').show();");
                        //PrimefacesContextUI.ejecutar("seleccionPlan()");
                    } else {
                        MensajesUI.error("No fue posible registrar la actividad de plan de desarrollo.");
                        this.msj="No fue posible registrar la actividad de plan de desarrollo.";
                        isSeleccionadoActividad = false;
                        isSeleccionadoBitacora = false;
                        secuenciaBitacora = null;
                        bitacoras = null;
                        obsPlanDes = null;
                        periodicidad="TRES MESES";
                        PrimefacesContextUI.ejecutar("PF('alertaInfoActividad').hide();");
                        PrimefacesContextUI.ejecutar("PF('alertaActividadError').show();");
                    }
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
            System.out.println("Profesion: " + secProfesion);
            System.out.println("actividad: " + actividadSelec);
            System.out.println("Periodicidad: "+periodicidad);
            //System.out.println("secCurso: "+cursoSeleccionado.getSecuencia());
            if (actividadSelec == null) {
                MensajesUI.error("Por favor seleccione la actividad a desarrollar.");
            } else {
                boolean validInfo = false;
                System.out.println("tipo de actividad a registrar: " + tipoActividadNueva + " curso: " + secCurso + " Profesion: " + secProfesion + " obs: " + obsPlanDes);
                switch (tipoActividadNueva) {
                    case 5:
                        System.out.println("Seleccionó capacitación formal");
                        if ((secProfesion != null && secProfesion != "") || (obsPlanDes != null && obsPlanDes != "" && obsPlanDes.length() > 0 && obsPlanDes.length() <= 500)) {
                            validInfo = true;
                        } else {
                            MensajesUI.error("Seleccione un curso para capacitación formal ó digite una observación de máximo 500 carácteres. ");
                        }
                        break;
                    case 6:
                        System.out.println("Seleccionó capacitación no formal");
                        if ((secCurso != null && secCurso != "") || (obsPlanDes != null && obsPlanDes != "" && obsPlanDes.length() > 0 && obsPlanDes.length() <= 500)) {
                            validInfo = true;
                        } else {
                            MensajesUI.error("Seleccione un curso para capacitación no formal ó digite una observación");
                        }
                        break;
                    default:
                        System.out.println("Seleccionó una capacitación x");
                        if (obsPlanDes == null || obsPlanDes == "" || obsPlanDes.length() <= 0 || obsPlanDes.length() > 500) {
                            MensajesUI.error("Digite una observación de máximo 500 carácteres");
                        } else {
                            validInfo = true;
                        }
                        break;
                }

                if (validInfo) {
                    if (administrarPlanDesarrollo.registrarPlanDesarrollo(codigoNuevo, evaluadoActual.getSecuencia(), actividadSelec, obsPlanDes, secCurso, secProfesion, periodicidad)) {
                        MensajesUI.info("Actividad de plan de desarrollo creada exitosamente.");
                        evalPlanesDesarrollos = administrarPlanDesarrollo.obtenerPlanesDesarrollos(evaluadoActual.getSecuencia());
                        obsPlanDes = "";
                        setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
                        isSeleccionadoActividad = false;
                        isSeleccionadoBitacora = false;
                        secuenciaBitacora = null;
                        bitacoras = null;
                        periodicidad="TRES MESES";
                        this.setIsSeleccionadoActividad(false);
                        PrimefacesContextUI.ejecutar("PF('alertanuevaactividad').hide();");
                    } else {
                        MensajesUI.error("No fue posible registrar la actividad de plan de desarrollo.");
                        PrimefacesContextUI.ejecutar("PF('alertanuevaactividad').hide();");
                        isSeleccionadoActividad = false;
                        isSeleccionadoBitacora = false;
                        secuenciaBitacora = null;
                        bitacoras = null;
                        obsPlanDes = "";
                        periodicidad="TRES MESES";
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error ControladorPlanDesarrollo.registrarPlanDEsarrollo(): " + e.getMessage());
        }
        /*this.setIsSeleccionadoActividad(false);
        secActividad=null;
        isSeleccionadoActividad=false;*/
        habilitaEliminarActividad=hasEvalPlanesDesarrollos();
        habilitaEliminarBitacora=hasBitacoras();
    }

    
    public void actualizaCod() {
        this.setCodigo(administrarPlanDesarrollo.obtenerUltimoCodigo(this.evaluadoActual.getSecuencia()));
    }
    
    public void actualizaActividades(){
       evalactividades = administrarPlanDesarrollo.obtenerActividades();        
    }
    
    public void actualizaListas(){
        System.out.println("actualizaListas()");
        this.setObservacion(null);
        actualizaCod();
        cargarPlanesDesarrollo();
        actualizaActividades();
        isSeleccionadoActividad=false;
        obsPlanDes="";
    }
    
    public void actualizaListasBitacora(){
        bitacoras=administrarPlanDesarrollo.obtenerBitacoras(secPlanDesarrollo);
        porcent="0";
        comentario=null;
        setearFechaActualBitacora();
        opcBitacora = 0;
    }
    
    public void actualizaListasMovil(){
        System.out.println("actualizaListasMovil()");
        obsPlanDes="";
        this.setObservacion(null);
        actualizaCod();
        actualizaActividades();
        cargarPlanesDesarrollo();
        //PrimefacesContextUI.ejecutar("seleccionPlan()");
    }
     
}
