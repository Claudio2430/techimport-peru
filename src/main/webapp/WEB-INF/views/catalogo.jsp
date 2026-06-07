<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo - TechImport</title>
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        /* ═══════════════════════════════════════════════════════════════════════════
           Catálogo UI (Diseño TechGo - Flat Dark Theme)
           ═══════════════════════════════════════════════════════════════════════════ */
        :root {
            --bg-body: #0f172a; /* Azul cálido ultra oscuro */
            --bg-card: #1e293b; /* Azul cálido oscuro (tarjetas) */
            --bg-input: #1e293b;
            --border-color: rgba(255, 255, 255, 0.05);
            --text-main: #f8fafc;
            --text-muted: #94a3b8;
            --accent-blue: #3b82f6; /* Azul clásico para íconos/logos */
            --nav-height: 70px;
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
            flex-direction: column;
        }

        /* --- Header / Logo --- */
        .header {
            padding: 1.5rem 2rem 1rem 2rem;
            display: flex;
            align-items: center;
            gap: 12px;
            position: relative;
        }

        .btn-back {
            color: var(--text-muted);
            font-size: 1.2rem;
            text-decoration: none;
            margin-right: 10px;
            transition: color 0.3s;
        }

        .btn-back:hover {
            color: var(--text-main);
        }

        .logo-icon {
            width: 32px;
            height: 32px;
            background-color: var(--accent-blue);
            color: white;
            border-radius: 8px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 1rem;
        }

        .logo-text {
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .logo-title {
            font-size: 1.1rem;
            font-weight: 600;
            line-height: 1.1;
        }

        .logo-subtitle {
            font-size: 0.7rem;
            color: var(--accent-blue);
            font-weight: 500;
            letter-spacing: 0.5px;
        }

        /* --- Barra de Búsqueda --- */
        .search-container {
            padding: 0 2rem 1.5rem 2rem;
        }

        .search-box {
            display: flex;
            align-items: center;
            background-color: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 0.8rem 1rem;
            gap: 10px;
        }

        .search-box i {
            color: var(--text-muted);
            font-size: 0.9rem;
        }

        .search-box input {
            background: transparent;
            border: none;
            color: var(--text-main);
            width: 100%;
            font-size: 0.9rem;
            outline: none;
        }

        .search-box input::placeholder {
            color: var(--text-muted);
        }

        /* --- Sección Categorías --- */
        .categories-section {
            padding: 0 2rem;
            display: flex;
            flex-direction: column;
            margin-bottom: 2rem;
        }

        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .section-title {
            font-size: 1rem;
            font-weight: 600;
        }

        .see-all {
            font-size: 0.8rem;
            color: var(--accent-blue);
            text-decoration: none;
            font-weight: 500;
        }

        /* Grilla de 3 columnas para categorías pequeñas */
        .categories-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 1rem;
        }

        .category-card {
            background-color: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            padding: 1.5rem 0;
            gap: 0.5rem;
            cursor: pointer;
        }

        .category-icon {
            color: var(--accent-blue);
            font-size: 1.5rem;
        }

        .category-name {
            font-size: 0.85rem;
            font-weight: 500;
            color: var(--text-main);
        }

        /* --- Listado de Productos (Destacados) --- */
        .products-section {
            padding: 0 2rem 2rem 2rem;
        }

        .product-list {
            display: flex;
            flex-direction: column;
            gap: 0.8rem;
        }

        .product-card {
            background-color: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 12px;
            padding: 0.8rem;
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .product-img {
            width: 50px;
            height: 50px;
            border-radius: 8px;
            object-fit: cover;
            background-color: white; /* Para el fondo de los celulares/laptops */
            padding: 5px;
        }

        .product-info {
            display: flex;
            flex-direction: column;
            justify-content: center;
        }

        .product-title {
            font-size: 0.95rem;
            font-weight: 600;
            color: var(--text-main);
        }

        .product-price {
            font-size: 0.85rem;
            color: var(--accent-blue);
            font-weight: 500;
            margin-top: 2px;
        }

        .category-card {
            background-color: var(--bg-card);
            border: 1px solid var(--border-color);
            border-radius: 16px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            gap: 1rem;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        .category-card:hover {
            background-color: rgba(30, 41, 59, 0.8);
        }

        /* Responsividad Básica */
        @media (max-width: 768px) {
            .categories-grid {
                grid-template-columns: repeat(3, 1fr);
            }
        }
    </style>
</head>
<body>

    <!-- Cabecera / Logo -->
    <header class="header">
        <a href="${pageContext.request.contextPath}/" class="btn-back" title="Regresar al inicio">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
        <div class="logo-icon">
            <i class="fa-solid fa-bolt"></i>
        </div>
        <div class="logo-text">
            <span class="logo-title">TechImport</span>
            <span class="logo-subtitle">PERÚ</span>
        </div>
    </header>

    <!-- Barra de Búsqueda -->
    <div class="search-container">
        <div class="search-box">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input type="text" placeholder="Buscar productos tecnológicos...">
        </div>
    </div>

    <!-- Sección de Categorías -->
    <main class="categories-section">
        <div class="section-header">
            <h2 class="section-title">Categorías</h2>
        </div>
        
        <div class="categories-grid">
            <!-- Smartphone -->
            <div class="category-card">
                <i class="fa-solid fa-mobile-screen-button category-icon"></i>
                <span class="category-name">Smartphones</span>
            </div>

            <!-- Laptops -->
            <div class="category-card">
                <i class="fa-solid fa-laptop category-icon"></i>
                <span class="category-name">Laptops</span>
            </div>

            <!-- Accesorios -->
            <div class="category-card">
                <i class="fa-solid fa-headphones-simple category-icon"></i>
                <span class="category-name">Accesorios</span>
            </div>
        </div>
    </main>

    <!-- Sección de Destacados -->
    <section class="products-section">
        <div class="section-header">
            <h2 class="section-title">Destacados</h2>
            <a href="#" class="see-all">Ver todo &gt;</a>
        </div>
        
        <div class="product-list">
            <!-- Producto 1 -->
            <div class="product-card">
                <img src="https://illustrations.popsy.co/amber/smartphone.svg" alt="iPhone 15 Pro Max" class="product-img" style="background-color: #fdd888;">
                <div class="product-info">
                    <span class="product-title">iPhone 15 Pro Max</span>
                    <span class="product-price">S/ 5,499</span>
                </div>
            </div>

            <!-- Producto 2 -->
            <div class="product-card">
                <img src="https://illustrations.popsy.co/amber/smartphone.svg" alt="Samsung Galaxy S24 Ultra" class="product-img" style="background-color: #e5e7eb;">
                <div class="product-info">
                    <span class="product-title">Samsung Galaxy S24 Ultra</span>
                    <span class="product-price">S/ 4,899</span>
                </div>
            </div>

            <!-- Producto 3 -->
            <div class="product-card">
                <img src="https://illustrations.popsy.co/amber/work-from-home.svg" alt="MacBook Pro 16" class="product-img" style="background-color: #3b82f6;">
                <div class="product-info">
                    <span class="product-title">MacBook Pro 16"</span>
                    <span class="product-price">S/ 9,999</span>
                </div>
            </div>
        </div>
    </section>

    <!-- Sección de Ofertas Especiales -->
    <section class="products-section">
        <div class="section-header">
            <h2 class="section-title">Ofertas Especiales</h2>
            <a href="#" class="see-all">Ver todo &gt;</a>
        </div>
        
        <div class="product-list">
            <div class="product-card">
                <img src="https://illustrations.popsy.co/amber/headphones.svg" alt="Audífonos Sony WH-1000XM5" class="product-img" style="background-color: #fca5a5;">
                <div class="product-info">
                    <span class="product-title">Sony WH-1000XM5</span>
                    <span class="product-price">S/ 1,299</span>
                </div>
            </div>
        </div>
    </section>

</body>
</html>
