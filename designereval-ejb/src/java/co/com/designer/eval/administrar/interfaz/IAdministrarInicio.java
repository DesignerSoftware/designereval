package co.com.designer.eval.administrar.interfaz;

import co.com.designer.eval.entidades.Convocatorias;
import co.com.designer.eval.entidades.Evaluados;
import co.com.designer.eval.entidades.Pruebas;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luistrivino
 */
public interface IAdministrarInicio {

    public void obtenerConexion(String idSesion);

    public List<Convocatorias> obtenerConvocatorias(String usuario);

    public List<Evaluados> obtenerEvaluados(String usuario, BigInteger secConvocatoria);

    public BigDecimal cantidadEvaluados(BigInteger secuenciaEvaluador, BigInteger secConvocatoria);

    public BigDecimal totalEmpleadosEvaluadorConvocatoria(BigInteger secuenciaEvaluador, BigInteger secConvocatoria);

    public BigDecimal cantidadEvaluadosConvocatoria(BigInteger secConvocatoria);

    public BigDecimal totalEmpleadosEvaluador(BigInteger secuenciaEvaluador);

    public BigDecimal obtenerSecuenciaEvaluador(String usuario);

    public List<Pruebas> obtenerPruebasEvaluado(String usuario, BigInteger secEmplConvo);

    public boolean cerrarEvaluaciones(BigDecimal secConvocatoria, String usuario);
    
    public String cerrarConvocatoria(BigDecimal secConvocatoria, String usuario);

    public List<Convocatorias> obtenerConvocatoriasAlcance(String usuario);

    public boolean enviarCorreo(String nitEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto);

    public String generarReporte(String nombreReporte, String tipoReporte, Map parametros, String nombreConvocatoria);

    public boolean actualizarEstado(BigInteger secPrueba, String estado);

    public String estaConsolidado(BigInteger secConvocatoria, BigInteger secEmplConvo);
    
    public boolean validarJefeInmediato(BigDecimal secuenciaEvaluador, BigDecimal evaluado);
}
