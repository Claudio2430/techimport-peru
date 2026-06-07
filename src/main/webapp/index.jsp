<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TechImport Perú - Compras Tecnológicas</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        /* ═══════════════════════════════════════════════════════════════════════════
           TechImport Perú — Landing Page (E-commerce UI)
           ═══════════════════════════════════════════════════════════════════════════ */
        :root {
            --primary: #3b82f6; /* Azul clásico solid */
            --primary-hover: #2563eb;
            --secondary: #1d4ed8;
            --dark-bg: #0f172a;
            --dark-card: #1e293b;
            --text-light: #f8fafc;
            --text-muted: #94a3b8;
            --glass-border: rgba(255, 255, 255, 0.05);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Inter', sans-serif;
        }

        body {
            background-color: var(--dark-bg);
            color: var(--text-light);
            overflow-x: hidden;
        }

        /* --- Navbar / Header --- */
        header {
            position: fixed;
            top: 0;
            width: 100%;
            padding: 1.5rem 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: var(--dark-bg);
            border-bottom: 1px solid var(--glass-border);
            z-index: 1000;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: 800;
            display: flex;
            align-items: center;
            gap: 10px;
            color: var(--text-light);
            text-decoration: none;
        }

        .logo i {
            color: var(--primary);
            font-size: 1.8rem;
        }

        .nav-links {
            display: flex;
            gap: 2rem;
            list-style: none;
        }

        .nav-links a {
            color: var(--text-muted);
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s;
        }

        .nav-links a:hover {
            color: var(--text-light);
        }

        /* Botones de Autenticación a un lado */
        .auth-buttons {
            display: flex;
            gap: 1rem;
        }

        .btn {
            padding: 0.6rem 1.5rem;
            border-radius: 8px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s ease;
            cursor: pointer;
            border: none;
        }

        .btn-outline {
            background: transparent;
            color: var(--text-light);
            border: 1px solid var(--glass-border);
        }

        .btn-outline:hover {
            background: rgba(255, 255, 255, 0.1);
        }

        .btn-primary {
            background: var(--primary);
            color: white;
        }

        .btn-primary:hover {
            background: var(--primary-hover);
        }

        /* --- Hero Section --- */
        .hero {
            min-height: 100vh;
            display: flex;
            align-items: center;
            padding: 0 5%;
            margin-top: 60px; /* offset por el navbar */
        }

        .hero-content {
            flex: 1;
            max-width: 600px;
            z-index: 2;
        }

        .badge {
            display: inline-block;
            padding: 0.4rem 1rem;
            background: var(--dark-card);
            color: var(--primary);
            border: 1px solid var(--glass-border);
            border-radius: 50px;
            font-weight: 600;
            font-size: 0.9rem;
            margin-bottom: 1.5rem;
        }

        .hero h1 {
            font-size: 4rem;
            font-weight: 800;
            line-height: 1.1;
            margin-bottom: 1.5rem;
            background: linear-gradient(135deg, #fff, #94a3b8);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .hero h1 span {
            color: var(--primary);
            -webkit-text-fill-color: var(--primary);
        }

        .hero p {
            font-size: 1.2rem;
            color: var(--text-muted);
            margin-bottom: 2.5rem;
            line-height: 1.6;
        }

        .hero-buttons {
            display: flex;
            gap: 1.5rem;
        }

        .hero-image {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            position: relative;
            z-index: 1;
        }

        /* Animación flotante de los productos */
        .tech-item {
            background: var(--dark-card);
            border: 1px solid var(--glass-border);
            border-radius: 20px;
            padding: 2rem;
            text-align: center;
            position: relative;
        }

        .tech-item img {
            width: 250px;
        }

        .price-tag {
            position: absolute;
            top: -15px;
            right: -15px;
            background: var(--primary);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: 700;
            font-size: 1.1rem;
        }

        /* --- Productos Destacados (Mini Grid) --- */
        .features {
            padding: 5rem 5%;
            background: var(--dark-card);
            border-top: 1px solid var(--glass-border);
        }

        .section-title {
            text-align: center;
            font-size: 2.5rem;
            margin-bottom: 3rem;
            color: var(--text-light);
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
        }

        .feature-card {
            background: var(--dark-bg);
            border: 1px solid var(--glass-border);
            border-radius: 16px;
            padding: 2rem;
            text-align: center;
        }

        .feature-icon {
            font-size: 2.5rem;
            color: var(--primary);
            margin-bottom: 1rem;
        }

        /* Responsividad */
        @media (max-width: 992px) {
            .hero {
                flex-direction: column;
                text-align: center;
                padding-top: 6rem;
            }
            .hero-content {
                margin-bottom: 4rem;
            }
            .hero-buttons {
                justify-content: center;
            }
            .nav-links {
                display: none; /* Ocultar en móviles por simplicidad */
            }
        }
    </style>
</head>
<body>

    <!-- Cabecera de Navegación -->
    <header>
        <a href="#" class="logo">
            <i class="fa-solid fa-laptop-code"></i> TechImport
        </a>

        <!-- Enlaces centrales (decorativos por ahora) -->
        <ul class="nav-links">
            <li><a href="#">Laptops</a></li>
            <li><a href="#">Componentes</a></li>
            <li><a href="#">Periféricos</a></li>
            <li><a href="#">Ofertas</a></li>
        </ul>

        <!-- BOTONES DE INICIAR SESIÓN Y REGISTRO -->
        <div class="auth-buttons">
            <a href="${pageContext.request.contextPath}/usuario/login" class="btn btn-outline">Iniciar Sesión</a>
            <a href="${pageContext.request.contextPath}/usuario/registro" class="btn btn-primary">Registrarse</a>
        </div>
    </header>

    <!-- Sección Principal (Hero) -->
    <section class="hero">
        <div class="hero-content">
            <div class="badge">Nuevos Ingresos 2026</div>
            <h1>La mejor <span>tecnología</span> al alcance de un clic.</h1>
            <p>
                En TechImport Perú encuentras los componentes más exclusivos, laptops de última generación y los accesorios gaming que necesitas para elevar tu setup al siguiente nivel.
            </p>
            <div class="hero-buttons">
                <!-- El botón de compra lleva al catálogo -->
                <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary" style="padding: 1rem 2rem; font-size: 1.1rem;">Explorar Catálogo <i class="fa-solid fa-arrow-right"></i></a>
            </div>
        </div>

        <div class="hero-image">
            <div class="tech-item">
                <div class="price-tag">S/ 1,299</div>
                <!-- Usamos un SVG moderno de una laptop como placeholder representativo -->
                <img src="https://illustrations.popsy.co/amber/work-from-home.svg" alt="Laptop Gaming">
                <h3 style="margin-top: 1rem;">Laptop Gamer RTX Series</h3>
                <p style="color: var(--text-muted); font-size: 0.9rem; margin-top: 0.5rem;">Stock Disponible</p>
            </div>
        </div>
    </section>

    <!-- Sección de Características -->
    <section class="features">
        <h2 class="section-title">¿Por qué comprar con nosotros?</h2>
        <div class="grid">
            <div class="feature-card">
                <i class="fa-solid fa-truck-fast feature-icon"></i>
                <h3>Envío Nacional</h3>
                <p style="color: var(--text-muted); margin-top: 1rem;">Llegamos a todas las provincias del Perú con los mejores curiers.</p>
            </div>
            <div class="feature-card">
                <i class="fa-solid fa-shield-halved feature-icon"></i>
                <h3>Garantía Real</h3>
                <p style="color: var(--text-muted); margin-top: 1rem;">Productos 100% originales con garantía directa de la marca.</p>
            </div>
            <div class="feature-card">
                <i class="fa-solid fa-headset feature-icon"></i>
                <h3>Soporte Técnico</h3>
                <p style="color: var(--text-muted); margin-top: 1rem;">Expertos listos para ayudarte a armar tu PC soñada.</p>
            </div>
        </div>
    </section>

</body>
</html>
