<%-- 
    login.jsp - Página de Inicio de Sesión
    Sistema TechImport Perú
    
    Vista de login con diseño plano (flat) estilo TechGo.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión | TechImport Perú</title>

    <%-- Google Fonts - Inter --%>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        :root {
            --bg-body: #0f172a; 
            --bg-card: #1e293b; 
            --border-color: rgba(255, 255, 255, 0.05);
            --text-main: #f8fafc;
            --text-muted: #94a3b8;
            --accent-blue: #3b82f6; 
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', sans-serif;
        }

        body {
            background-color: var(--bg-body);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-container {
            width: 100%;
            max-width: 440px;
            padding: 20px;
        }

        .login-card {
            background-color: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 16px;
            padding: 40px;
        }

        .login-header {
            text-align: center;
            margin-bottom: 30px;
            position: relative;
        }

        .btn-back {
            position: absolute;
            left: 0;
            top: 0;
            color: var(--text-muted);
            font-size: 1.2rem;
            text-decoration: none;
            transition: color 0.3s;
        }

        .btn-back:hover {
            color: var(--text-main);
        }

        .logo-icon {
            width: 50px;
            height: 50px;
            background-color: var(--accent-blue);
            color: white;
            border-radius: 12px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 1.5rem;
            margin: 0 auto 15px;
        }

        .login-header h1 {
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 5px;
        }

        .login-header p {
            font-size: 0.9rem;
            color: var(--text-muted);
        }

        /* Alertas */
        .alert {
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.85rem;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .alert-error {
            background: rgba(239, 68, 68, 0.1);
            color: #fca5a5;
        }

        .alert-success {
            background: rgba(34, 197, 94, 0.1);
            color: #86efac;
        }

        /* Formularios */
        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-size: 0.8rem;
            font-weight: 600;
            color: var(--text-muted);
            margin-bottom: 8px;
        }

        .form-control {
            width: 100%;
            padding: 12px 16px;
            background-color: var(--bg-body);
            border: 1px solid var(--border-color);
            border-radius: 8px;
            color: var(--text-main);
            font-size: 0.95rem;
            outline: none;
        }

        .form-control:focus {
            border-color: var(--accent-blue);
        }

        /* Botón */
        .btn-login {
            width: 100%;
            padding: 14px;
            background-color: var(--accent-blue);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            margin-top: 10px;
        }

        .btn-login:hover {
            background-color: #2563eb;
        }

        .login-footer {
            text-align: center;
            margin-top: 24px;
            font-size: 0.85rem;
            color: var(--text-muted);
        }

        .login-footer a {
            color: var(--accent-blue);
            text-decoration: none;
            font-weight: 600;
        }
    </style>
</head>
<body>

    <main class="login-container">
        <div class="login-card">

            <div class="login-header">
                <a href="${pageContext.request.contextPath}/" class="btn-back" title="Regresar al inicio">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
                <div class="logo-icon">
                    <i class="fa-solid fa-bolt"></i>
                </div>
                <h1>TechImport Perú</h1>
                <p>Ingrese sus credenciales para continuar</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <span><i class="fa-solid fa-triangle-exclamation"></i> ${error}</span>
                </div>
            </c:if>

            <c:if test="${not empty param.mensaje}">
                <div class="alert alert-success">
                    <span><i class="fa-solid fa-check"></i> ${param.mensaje}</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/usuario/login" method="POST">

                <div class="form-group">
                    <label for="email">Correo Electrónico</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="nombre@ejemplo.com" required value="${param.email}">
                </div>

                <div class="form-group">
                    <label for="contrasena">Contraseña</label>
                    <input type="password" id="contrasena" name="contrasena" class="form-control" placeholder="Ingrese su contraseña" required>
                </div>

                <button type="submit" class="btn-login">
                    Iniciar Sesión
                </button>
            </form>

            <div class="login-footer">
                <p>¿No tienes una cuenta? <a href="${pageContext.request.contextPath}/usuario/registro">Regístrate aquí</a></p>
            </div>

        </div>
    </main>

</body>
</html>
