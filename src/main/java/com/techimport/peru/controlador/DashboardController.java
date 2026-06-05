package com.techimport.peru.controlador;

import com.techimport.peru.modelo.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controlador para la pantalla principal del Dashboard.
 * Solo accesible para usuarios autenticados.
 */
@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificamos si la sesión existe y tiene el atributo "usuario"
        if (session == null || session.getAttribute("usuario") == null) {
            logger.warn("Intento de acceso no autorizado al dashboard. Redirigiendo a login.");
            response.sendRedirect(request.getContextPath() + "/usuario/login?error=Debes iniciar sesion primero");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        logger.info("Usuario {} accedió al dashboard.", usuario.getEmail());

        // Redirigir a la vista JSP del dashboard
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
