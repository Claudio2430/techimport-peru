<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - TechImport Perú</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- FontAwesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        /* ═══════════════════════════════════════════════════════════════════════════
           TechImport Perú — Dashboard Styles (Glassmorphism)
           ═══════════════════════════════════════════════════════════════════════════ */
        :root {
            --primary-color: #3b82f6; /* Solid Blue */
            --secondary-color: #2563eb; 
            --accent-color: #1d4ed8; 
            --bg-dark: #0f172a;
            --text-light: #f8fafc;
            --text-muted: #94a3b8;
            --glass-bg: #1e293b;
            --glass-border: rgba(255, 255, 255, 0.05);
            --glass-shadow: none;
            --sidebar-width: 280px;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--bg-dark);
            color: var(--text-light);
            min-height: 100vh;
            display: flex;
            overflow-x: hidden;
        }

        /* --- Sidebar --- */
        .sidebar {
            width: var(--sidebar-width);
            background: var(--bg-dark);
            border-right: 1px solid var(--glass-border);
            padding: 2rem;
            display: flex;
            flex-direction: column;
            position: fixed;
            height: 100vh;
            z-index: 10;
        }

        .brand {
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 3rem;
            display: flex;
            align-items: center;
            gap: 10px;
            color: var(--text-light);
        }

        .brand i {
            color: var(--primary-color);
        }

        .nav-menu {
            list-style: none;
            display: flex;
            flex-direction: column;
            gap: 1rem;
            flex-grow: 1;
        }

        .nav-item a {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 12px 20px;
            text-decoration: none;
            color: var(--text-muted);
            font-weight: 500;
            border-radius: 12px;
            transition: all 0.3s ease;
        }

        .nav-item a:hover, .nav-item a.active {
            background: rgba(99, 102, 241, 0.1);
            color: var(--text-light);
            transform: translateX(5px);
        }

        .nav-item a i {
            font-size: 1.2rem;
            width: 24px;
            text-align: center;
        }

        .user-profile {
            margin-top: auto;
            padding-top: 2rem;
            border-top: 1px solid var(--glass-border);
        }

        .logout-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            width: 100%;
            padding: 12px;
            background: rgba(239, 68, 68, 0.1);
            color: #ef4444;
            text-decoration: none;
            border-radius: 12px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: 1px solid rgba(239, 68, 68, 0.2);
        }

        .logout-btn:hover {
            background: #ef4444;
            color: white;
            box-shadow: 0 4px 15px rgba(239, 68, 68, 0.4);
        }

        /* --- Main Content --- */
        .main-content {
            flex: 1;
            margin-left: var(--sidebar-width);
            padding: 3rem;
            min-height: 100vh;
        }

        .welcome-banner {
            background: var(--primary-color);
            border-radius: 24px;
            padding: 3rem;
            margin-bottom: 3rem;
            position: relative;
            overflow: hidden;
            border: 1px solid var(--glass-border);
        }

        .welcome-banner::after {
            content: '';
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            width: 50%;
            background: url('https://illustrations.popsy.co/amber/freelancer.svg') no-repeat right center;
            background-size: contain;
            opacity: 0.8;
            pointer-events: none;
        }

        .welcome-title {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 1rem;
            text-shadow: 0 2px 10px rgba(0,0,0,0.2);
            position: relative;
            z-index: 2;
        }

        .welcome-subtitle {
            font-size: 1.1rem;
            color: rgba(255, 255, 255, 0.9);
            max-width: 500px;
            line-height: 1.6;
            position: relative;
            z-index: 2;
        }

        /* --- Dashboard Grid --- */
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
        }

        .glass-card {
            background: var(--glass-bg);
            border: 1px solid var(--glass-border);
            border-radius: 20px;
            padding: 2rem;
            position: relative;
            overflow: hidden;
        }

        .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .card-title {
            font-size: 1.2rem;
            font-weight: 600;
            color: var(--text-muted);
        }

        .card-icon {
            width: 50px;
            height: 50px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            background: rgba(255, 255, 255, 0.05);
        }

        .icon-blue { color: #3b82f6; background: rgba(59, 130, 246, 0.1); }
        .icon-green { color: #10b981; background: rgba(16, 185, 129, 0.1); }
        .icon-purple { color: #3b82f6; background: rgba(59, 130, 246, 0.1); }

        .card-value {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }

        .card-desc {
            font-size: 0.9rem;
            color: var(--text-muted);
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .text-success { color: #10b981; font-weight: 600; }

        /* --- Responsive Design --- */
        @media (max-width: 992px) {
            .welcome-banner::after {
                opacity: 0.3;
            }
        }

        @media (max-width: 768px) {
            .sidebar {
                transform: translateX(-100%);
                transition: transform 0.3s ease;
            }
            .main-content {
                margin-left: 0;
                padding: 1.5rem;
            }
            .welcome-banner {
                padding: 2rem;
            }
            .welcome-title {
                font-size: 2rem;
            }
        }
    </style>
</head>
<body>

    <!-- Sidebar Menu -->
    <aside class="sidebar">
        <div class="brand">
            <i class="fa-solid fa-microchip"></i>
            <span>TechImport</span>
        </div>

        <ul class="nav-menu">
            <li class="nav-item">
                <a href="#" class="active"><i class="fa-solid fa-house"></i> Inicio</a>
            </li>
            <li class="nav-item">
                <a href="#"><i class="fa-solid fa-box-open"></i> Mis Pedidos</a>
            </li>
            <li class="nav-item">
                <a href="#"><i class="fa-solid fa-laptop"></i> Catálogo</a>
            </li>
            <li class="nav-item">
                <a href="#"><i class="fa-solid fa-heart"></i> Favoritos</a>
            </li>
            <li class="nav-item">
                <a href="#"><i class="fa-solid fa-gear"></i> Configuración</a>
            </li>
        </ul>

        <div class="user-profile">
            <!-- Destruimos la sesión al hacer clic aquí -->
            <a href="${pageContext.request.contextPath}/usuario/cerrar-sesion" class="logout-btn">
                <i class="fa-solid fa-right-from-bracket"></i> Cerrar Sesión
            </a>
        </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
        
        <!-- Welcome Banner -->
        <section class="welcome-banner">
            <!-- Usamos JSTL y EL para mostrar el nombre del usuario logueado -->
            <h1 class="welcome-title">¡Hola, <c:out value="${sessionScope.usuario.nombres}" default="Amigo"/>! 👋</h1>
            <p class="welcome-subtitle">
                Bienvenido al panel central de TechImport Perú. Desde aquí puedes gestionar tus pedidos de tecnología, explorar nuevos productos y revisar tus listas de favoritos.
            </p>
        </section>

        <!-- Dashboard Widgets -->
        <section class="dashboard-grid">
            
            <!-- Card 1 -->
            <div class="glass-card">
                <div class="card-header">
                    <span class="card-title">Mis Pedidos</span>
                    <div class="card-icon icon-blue">
                        <i class="fa-solid fa-truck-fast"></i>
                    </div>
                </div>
                <div class="card-value">0</div>
                <div class="card-desc">
                    <span>Aún no tienes pedidos registrados.</span>
                </div>
            </div>

            <!-- Card 2 -->
            <div class="glass-card">
                <div class="card-header">
                    <span class="card-title">Ofertas Activas</span>
                    <div class="card-icon icon-purple">
                        <i class="fa-solid fa-tag"></i>
                    </div>
                </div>
                <div class="card-value">12</div>
                <div class="card-desc">
                    <span class="text-success"><i class="fa-solid fa-arrow-trend-up"></i> +3 nuevas</span> 
                    <span>esta semana</span>
                </div>
            </div>

            <!-- Card 3 -->
            <div class="glass-card">
                <div class="card-header">
                    <span class="card-title">Estado de Cuenta</span>
                    <div class="card-icon icon-green">
                        <i class="fa-solid fa-shield-check"></i>
                    </div>
                </div>
                <div class="card-value">Activo</div>
                <div class="card-desc">
                    <span>Miembro <c:out value="${sessionScope.usuario.rol}" default="CLIENTE"/></span>
                </div>
            </div>

        </section>

    </main>

</body>
</html>
