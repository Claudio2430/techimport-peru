package com.techimport.peru.controlador;

import com.techimport.peru.dao.impl.UsuarioDAOImpl;
import com.techimport.peru.modelo.Usuario;
import com.techimport.peru.servicio.UsuarioService;
import com.techimport.peru.util.ValidacionUtil;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

/**
 * Controlador Servlet para la gestión de usuarios en el sistema TechImport Perú.
 *
 * <p>Actúa como el componente <strong>Controller</strong> dentro del patrón MVC,
 * delegando la lógica de negocio al {@link UsuarioService} y redirigiendo a las
 * vistas JSP correspondientes.</p>
 *
 * <h3>Rutas manejadas (GET):</h3>
 * <ul>
 *   <li>{@code /usuario/login} – Muestra el formulario de inicio de sesión</li>
 *   <li>{@code /usuario/registro} – Muestra el formulario de registro</li>
 *   <li>{@code /usuario/listar} – Lista todos los usuarios registrados</li>
 *   <li>{@code /usuario/cerrar-sesion} – Cierra la sesión actual</li>
 *   <li>{@code /usuario/exportar-excel} – Exporta usuarios en formato Excel</li>
 * </ul>
 *
 * <h3>Rutas manejadas (POST):</h3>
 * <ul>
 *   <li>{@code /usuario/login} – Procesa la autenticación del usuario</li>
 *   <li>{@code /usuario/registro} – Procesa el registro de un nuevo usuario</li>
 * </ul>
 *
 * @author TechImport Perú - Equipo de Desarrollo
 * @version 1.0
 * @since 2026-06-08
 * @see UsuarioService
 * @see ValidacionUtil
 */
@WebServlet("/usuario/*")
public class UsuarioController extends HttpServlet {

    /** Número de versión de serialización para la clase Servlet. */
    private static final long serialVersionUID = 1L;

    /** Logger SLF4J para el registro de eventos y errores del controlador. */
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    /** Servicio de negocio para operaciones relacionadas con usuarios. */
    private UsuarioService usuarioService;

    /**
     * Inicializa el Servlet configurando las dependencias necesarias.
     *
     * <p>Crea una instancia de {@link UsuarioDAOImpl} y la inyecta en el
     * {@link UsuarioService} siguiendo el principio de Inversión de Dependencias (DIP).</p>
     *
     * @throws ServletException si ocurre un error durante la inicialización del Servlet
     */
    @Override
    public void init() throws ServletException {
        super.init();
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
        this.usuarioService = new UsuarioService(usuarioDAO);
        logger.info("UsuarioController inicializado correctamente.");
    }

    /**
     * Maneja las solicitudes HTTP GET para las rutas de usuario.
     *
     * <p>Establece la codificación UTF-8 y delega a la acción correspondiente
     * según la ruta ({@code pathInfo}) de la solicitud.</p>
     *
     * @param request  la solicitud HTTP entrante
     * @param response la respuesta HTTP saliente
     * @throws ServletException si ocurre un error en el procesamiento del Servlet
     * @throws IOException      si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/login";
        }

        try {
            switch (pathInfo) {
                case "/login":
                    mostrarLogin(request, response);
                    break;
                case "/registro":
                    mostrarRegistro(request, response);
                    break;
                case "/listar":
                    listarUsuarios(request, response);
                    break;
                case "/cerrar-sesion":
                    cerrarSesion(request, response);
                    break;
                case "/exportar-excel":
                    exportarExcel(request, response);
                    break;
                default:
                    logger.warn("Ruta GET no reconocida: {}", pathInfo);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "La ruta solicitada no fue encontrada.");
                    break;
            }
        } catch (Exception e) {
            logger.error("Error procesando solicitud GET [{}]: {}", pathInfo, e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Ocurrió un error interno en el servidor.");
        }
    }

    /**
     * Maneja las solicitudes HTTP POST para las rutas de usuario.
     *
     * <p>Establece la codificación UTF-8 y delega a la acción correspondiente
     * según la ruta ({@code pathInfo}) de la solicitud.</p>
     *
     * @param request  la solicitud HTTP entrante
     * @param response la respuesta HTTP saliente
     * @throws ServletException si ocurre un error en el procesamiento del Servlet
     * @throws IOException      si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/login";
        }

        try {
            switch (pathInfo) {
                case "/login":
                    procesarLogin(request, response);
                    break;
                case "/registro":
                    procesarRegistro(request, response);
                    break;
                default:
                    logger.warn("Ruta POST no reconocida: {}", pathInfo);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                            "La ruta solicitada no fue encontrada.");
                    break;
            }
        } catch (Exception e) {
            logger.error("Error procesando solicitud POST [{}]: {}", pathInfo, e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Ocurrió un error interno en el servidor.");
        }
    }

    // ========================
    //  Métodos GET (Vistas)
    // ========================

    /**
     * Redirige al formulario de inicio de sesión.
     *
     * @param request  la solicitud HTTP
     * @param response la respuesta HTTP
     * @throws ServletException si ocurre un error de despacho
     * @throws IOException      si ocurre un error de E/S
     */
    private void mostrarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("Mostrando vista de login.");
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * Redirige al formulario de registro de nuevo usuario.
     *
     * @param request  la solicitud HTTP
     * @param response la respuesta HTTP
     * @throws ServletException si ocurre un error de despacho
     * @throws IOException      si ocurre un error de E/S
     */
    private void mostrarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("Mostrando vista de registro.");
        request.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(request, response);
    }

    /**
     * Obtiene la lista completa de usuarios y la envía a la vista de listado.
     *
     * <p>Establece el atributo {@code "usuarios"} en el request con la lista
     * obtenida del servicio.</p>
     *
     * @param request  la solicitud HTTP
     * @param response la respuesta HTTP
     * @throws ServletException si ocurre un error de despacho
     * @throws IOException      si ocurre un error de E/S
     */
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("Listando todos los usuarios.");
        List<Usuario> usuarios = usuarioService.listarTodos();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/views/listar-usuarios.jsp").forward(request, response);
    }

    /**
     * Invalida la sesión actual del usuario y redirige a la página de login.
     *
     * @param request  la solicitud HTTP
     * @param response la respuesta HTTP
     * @throws IOException si ocurre un error de redirección
     */
    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            logger.info("Cerrando sesión del usuario: {}",
                    session.getAttribute("usuarioEmail"));
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/usuario/login");
    }

    /**
     * Genera y descarga un archivo Excel con la lista de todos los usuarios.
     *
     * <p>Utiliza Apache POI a través del servicio {@link UsuarioService#exportarUsuariosExcel()}
     * para generar el libro de trabajo Excel, configurando los headers HTTP apropiados
     * para la descarga del archivo.</p>
     *
     * @param request  la solicitud HTTP
     * @param response la respuesta HTTP
     * @throws IOException si ocurre un error al escribir en el flujo de salida
     */
    private void exportarExcel(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        logger.info("Exportando usuarios a Excel.");

        try (Workbook workbook = usuarioService.exportarUsuariosExcel()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"usuarios_techimport.xlsx\"");

            try (OutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
                outputStream.flush();
            }

            logger.info("Exportación de usuarios a Excel completada exitosamente.");
        } catch (Exception e) {
            logger.error("Error al exportar usuarios a Excel: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al generar el archivo Excel.");
        }
    }

    // ========================
    //  Métodos POST (Acciones)
    // ========================

    /**
     * Procesa la autenticación del usuario con las credenciales proporcionadas.
     *
     * <p>Sanitiza las entradas utilizando {@link ValidacionUtil#sanitizarEntrada(String)},
     * valida el formato del email y luego delega la autenticación al servicio.
     * En caso de éxito, almacena los datos del usuario en la sesión HTTP.</p>
     *
     * @param request  la solicitud HTTP con los parámetros "email" y "contrasena"
     * @param response la respuesta HTTP
     * @throws ServletException si ocurre un error de despacho
     * @throws IOException      si ocurre un error de E/S
     */
    private void procesarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = ValidacionUtil.sanitizarEntrada(request.getParameter("email"));
        String contrasena = ValidacionUtil.sanitizarEntrada(request.getParameter("contrasena"));

        logger.debug("Intento de login para el email: {}", email);

        // Validación básica de campos vacíos
        if (email == null || email.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Debe ingresar el email y la contraseña.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // Validación de formato de email
        if (!ValidacionUtil.esEmailValido(email)) {
            request.setAttribute("error", "El formato del email no es válido.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            Optional<Usuario> usuarioOpt = usuarioService.autenticar(email, contrasena);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // Crear sesión y almacenar datos del usuario
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("usuarioNombre", usuario.getNombres());
                session.setAttribute("usuarioApellido", usuario.getApellidos());
                session.setAttribute("usuarioEmail", usuario.getEmail());
                session.setAttribute("usuarioRol", usuario.getRol());
                session.setAttribute("usuario", usuario);
                session.setMaxInactiveInterval(30 * 60); // 30 minutos

                logger.info("Login exitoso para el usuario: {} (ID: {})",
                        usuario.getEmail(), usuario.getId());

                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                logger.warn("Intento de login fallido para el email: {}", email);
                request.setAttribute("error",
                        "Credenciales incorrectas. Verifique su email y contraseña.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                        .forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Error durante el proceso de autenticación: {}", e.getMessage(), e);
            request.setAttribute("error",
                    "Ocurrió un error durante la autenticación. Intente nuevamente.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    /**
     * Procesa el registro de un nuevo usuario en el sistema.
     *
     * <p>Sanitiza todas las entradas, valida el formato del email y la seguridad
     * de la contraseña, verifica que las contraseñas coincidan, y delega la creación
     * al servicio. En caso de éxito, redirige a la página de login con un mensaje
     * de confirmación.</p>
     *
     * @param request  la solicitud HTTP con los parámetros de registro
     * @param response la respuesta HTTP
     * @throws ServletException si ocurre un error de despacho
     * @throws IOException      si ocurre un error de E/S
     */
    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombres = ValidacionUtil.sanitizarEntrada(request.getParameter("nombres"));
        String apellidos = ValidacionUtil.sanitizarEntrada(request.getParameter("apellidos"));
        String email = ValidacionUtil.sanitizarEntrada(request.getParameter("email"));
        String contrasena = ValidacionUtil.sanitizarEntrada(request.getParameter("contrasena"));
        String confirmarContrasena = ValidacionUtil.sanitizarEntrada(
                request.getParameter("confirmarContrasena"));

        logger.debug("Intento de registro para el email: {}", email);

        // Validación de campos obligatorios
        if (nombres == null || nombres.trim().isEmpty()
                || apellidos == null || apellidos.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);
            return;
        }

        // Validación de formato de email
        if (!ValidacionUtil.esEmailValido(email)) {
            request.setAttribute("error", "El formato del email no es válido.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);
            return;
        }

        // Validación de seguridad de contraseña
        if (!ValidacionUtil.esContrasenaSegura(contrasena)) {
            request.setAttribute("error",
                    "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, "
                    + "minúsculas y números.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);
            return;
        }

        // Verificar que las contraseñas coincidan
        if (!contrasena.equals(confirmarContrasena)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);
            return;
        }

        try {
            Usuario nuevoUsuario = usuarioService.registrar(nombres, apellidos, email, contrasena);
            logger.info("Usuario registrado exitosamente: {} (ID: {})",
                    nuevoUsuario.getEmail(), nuevoUsuario.getId());

            // Redirigir al login con mensaje de éxito
            response.sendRedirect(request.getContextPath()
                    + "/usuario/login?mensaje=Registro exitoso. Ahora puede iniciar sesión.");
        } catch (IllegalStateException e) {
            logger.warn("Error de validación en registro: {}", e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            logger.error("Error durante el proceso de registro: {}", e.getMessage(), e);
            request.setAttribute("error",
                    "Ocurrió un error durante el registro. Intente nuevamente.");
            request.getRequestDispatcher("/WEB-INF/views/registro.jsp")
                    .forward(request, response);
        }
    }
}
