package com.techimport.peru.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador para la pantalla de Catálogo de Productos.
 * Permite el acceso público sin requerir inicio de sesión.
 */
@WebServlet("/catalogo")
public class CatalogoController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CatalogoController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        logger.info("Accediendo a la pantalla de catálogo público.");
        
        // Redirigir a la vista JSP del catálogo
        request.getRequestDispatcher("/WEB-INF/views/catalogo.jsp").forward(request, response);
    }
}
