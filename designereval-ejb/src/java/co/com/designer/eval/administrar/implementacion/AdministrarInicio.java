package co.com.designer.eval.administrar.implementacion;

import co.com.designer.eval.administrar.interfaz.IAdministrarInicio;
import co.com.designer.eval.administrar.interfaz.IAdministrarSesiones;
import co.com.designer.eval.correo.EnvioCorreo;
import co.com.designer.eval.entidades.ConfiguracionCorreo;
import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Generales;
import co.com.designer.eval.entidades.Pruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaConvocatorias;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaEvaluados;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaGenerales;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaPruebas;
import co.com.designer.eval.persistencia.interfaz.IPersistenciaUtilidadesBD;
import co.com.designer.eval.reportes.IniciarReporteInterface;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarInicio implements IAdministrarInicio, Serializable {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaConvocatorias persistenciaConvocatorias;
    @EJB
    private IPersistenciaPruebas persistenciaPruebas;
    @EJB
    private IPersistenciaUtilidadesBD persistenciaUtilidadesBD;
    @EJB
    private IPersistenciaEvaluados persistenciaEvaluados;
    @EJB
    private IPersistenciaConfiguracionCorreo persistenciaConfiguracionCorreo;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
    @EJB
    private IniciarReporteInterface reporte;
    private transient EntityManagerFactory emf;
//    private EntityManager em;
    String idSesion;

    @Override
    public void obtenerConexion(String idSesion) {
        try {
            this.idSesion = idSesion;
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
//            if (emf != null && emf.isOpen()) {
//                em = emf.createEntityManager();
//            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerConexion: " + e);
        }
    }

    private EntityManager obtenerConexion() {
        System.out.println(this.getClass().getName() + "obtenerConexion() -2" );
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                return emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerConexion-2: " + e);
            return null;
        }
        return null;
    }

    @Override
    public List<Convocatorias> obtenerConvocatorias(String usuario) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaConvocatorias.obtenerConvocatorias(em, usuario);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerConvocatorias: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Convocatorias> obtenerConvocatoriasAlcance(String usuario) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaConvocatorias.obtenerConvocatoriasAlcance(em, usuario);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerConvocatoriasAlcance: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Evaluados> obtenerEvaluados(String usuario, BigInteger secConvocatoria) {
        List<Evaluados> lista = null;
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            lista = persistenciaEvaluados.obtenerEvaluados(em, usuario, secConvocatoria);
            //consultaConsolidado(lista);
            return lista;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerEvaluados: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public BigDecimal totalEmpleadosEvaluador(BigInteger secuenciaEvaluador) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaUtilidadesBD.totalEmpleadosEvaluador(em, secuenciaEvaluador);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.totalEmpleadosEvaluador: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public BigDecimal cantidadEvaluadosConvocatoria(BigInteger secConvocatoria) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaUtilidadesBD.cantidadEvaluadosConvocatoria(em, secConvocatoria);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.cantidadEvaluadosConvocatoria: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public BigDecimal totalEmpleadosEvaluadorConvocatoria(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaUtilidadesBD.totalEmpleadosEvaluadorConvocatoria(em, secuenciaEvaluador, secConvocatoria);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.totalEmpleadosEvaluadorConvocatoria: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public BigDecimal cantidadEvaluados(BigInteger secuenciaEvaluador, BigInteger secConvocatoria) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaUtilidadesBD.cantidadEvaluados(em, secuenciaEvaluador, secConvocatoria);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.cantidadEvaluados: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public BigDecimal obtenerSecuenciaEvaluador(String usuario) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaConvocatorias.obtenerSecuenciaEvaluador(em, usuario);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerSecuenciaEvaluador: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Pruebas> obtenerPruebasEvaluado(String usuario, BigInteger secEmplConvo) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaPruebas.obtenerPruebasEvaluado(em, usuario, secEmplConvo);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.obtenerPruebasEvaluado: " + e);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean cerrarEvaluaciones(BigDecimal secConvocatoria, String usuario) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaConvocatorias.cerrarEvaluaciones(em, secConvocatoria, usuario);
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + "cerrarEvaluaciones" + ex);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public String cerrarConvocatoria(BigDecimal secConvocatoria, String usuario) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaConvocatorias.cerrarConvocatoria(em, secConvocatoria, usuario);
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + "cerrarConvocatoria" + ex);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean enviarCorreo(String nitEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto) {
        System.out.println(this.getClass().getName() + ".enviarCorreo()");
        boolean resul = false;
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, nitEmpresa);
//            resul = EnvioCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
            EnvioCorreo envioCorreo = new EnvioCorreo();
            resul = envioCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "enviarCorreo: " + e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return resul;
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte, Map parametros, String nombreConvocatoria) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            Calendar fecha = Calendar.getInstance();
            String pathReporteGenerado = null;
            String nombreArchivo;
            if (general != null) {
                nombreArchivo = "EVAL-resumen_convocatoria_" + nombreConvocatoria + fecha.get(Calendar.YEAR) + "_" + (fecha.get(Calendar.MONTH) + 1) + "_" + fecha.get(Calendar.DAY_OF_MONTH) + "_" + fecha.get(Calendar.HOUR) + "_" + fecha.get(Calendar.MINUTE) + "_" + fecha.get(Calendar.SECOND) + "_" + fecha.get(Calendar.MILLISECOND);
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                switch (tipoReporte) {
                    case "PDF":
                        nombreArchivo = nombreArchivo + ".pdf";
                        break;
                    case "XLSX":
                        nombreArchivo = nombreArchivo + ".xlsx";
                        break;
                    case "XLS":
                        nombreArchivo = nombreArchivo + ".xls";
                        break;
                    case "CSV":
                        nombreArchivo = nombreArchivo + ".csv";
                        break;
                    case "HTML":
                        nombreArchivo = nombreArchivo + ".html";
                        break;
                    case "DOCX":
                        nombreArchivo = nombreArchivo + ".rtf";
                        break;
                    default:
                        break;
                }
                parametros.put("pathImagenes", rutaReporte);
                EntityManager em2 = emf.createEntityManager();
                pathReporteGenerado = reporte.ejecutarReporte(nombreReporte,
                        rutaReporte, rutaGenerado, nombreArchivo, tipoReporte,
                        parametros, em2);
                em2.close();
                return pathReporteGenerado;
            }
            return pathReporteGenerado;
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.generarReporte: " + ex);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean actualizarEstado(BigInteger secPrueba, String estado) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaPruebas.actualizarEstado(em, secPrueba, estado);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.actualizarEstado: " + e);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public String estaConsolidado(BigInteger secConvocatoria, BigInteger secEmplConvo) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            return persistenciaPruebas.estaConsolidado(em, secConvocatoria, secEmplConvo);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error AdministrarInicio.estaConsolidado: " + e);
            return "N";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private List<Evaluados> consultaConsolidado(List<Evaluados> lista) {
        if (lista != null) {
            for (int i = 0; i < lista.size(); i++) {
                lista.get(i).setConsolidado(estaConsolidado(lista.get(i).getEvalConvocatoria(), lista.get(i).getEmpleado()));
            }
        }
        return lista;
    }
}
