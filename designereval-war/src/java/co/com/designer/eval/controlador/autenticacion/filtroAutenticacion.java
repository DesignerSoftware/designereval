package co.com.designer.eval.controlador.autenticacion;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Triviño
 */
@WebFilter(filterName = "filtroAutenticacion", urlPatterns = {"/faces/*"})
public class filtroAutenticacion implements Filter {

    public filtroAutenticacion() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession ses = req.getSession(false);
            String reqURI = req.getRequestURI();
            if (reqURI.equals(req.getContextPath() + "/") || reqURI.contains("/faces/Ingreso/ingreso.xhtml") || reqURI.contains("/faces/Movil/ingreso.xhtml")
                    || reqURI.contains("/faces/index.xhtml")
                    || reqURI.contains("/faces/resources/")
                    || (ses != null && ses.getAttribute("idUsuario") != null)
                    || reqURI.contains("/public/") || reqURI.contains("javax.faces.resource")) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath());
            }
        } catch (IOException | ServletException t) {
            System.out.println(this.getClass().getName() + t.getMessage());
        }
    }

    @Override
    public void destroy() {
    }
}
