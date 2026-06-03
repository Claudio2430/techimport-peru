
DROP DATABASE IF EXISTS techimport_peru;

CREATE DATABASE techimport_peru
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE techimport_peru;

CREATE TABLE usuario (
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    nombres        VARCHAR(100)   NOT NULL        COMMENT 'Nombres del usuario',
    apellidos      VARCHAR(100)   NOT NULL        COMMENT 'Apellidos del usuario',
    email          VARCHAR(150)   NOT NULL UNIQUE COMMENT 'Correo electrónico (login)',
    contrasena     VARCHAR(64)    NOT NULL        COMMENT 'Hash SHA-256 de la contraseña',
    salt           VARCHAR(24)    NOT NULL        COMMENT 'Salt aleatorio para hashing',
    rol            ENUM('ADMIN', 'CLIENTE')
                                  DEFAULT 'CLIENTE'
                                                  COMMENT 'Rol del usuario en el sistema',
    fecha_creacion TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
                                                  COMMENT 'Fecha de registro',
    activo         BOOLEAN        DEFAULT TRUE    COMMENT 'Soft-delete flag'
) ENGINE=InnoDB
  COMMENT='Usuarios del sistema (clientes y administradores)';

CREATE INDEX idx_usuario_rol ON usuario(rol);

CREATE TABLE proveedor (
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(150)   NOT NULL        COMMENT 'Razón social del proveedor',
    pais           VARCHAR(100)   NOT NULL        COMMENT 'País de origen',
    contacto_email VARCHAR(150)                   COMMENT 'Email de contacto',
    telefono       VARCHAR(20)                    COMMENT 'Teléfono de contacto',
    activo         BOOLEAN        DEFAULT TRUE    COMMENT 'Soft-delete flag'
) ENGINE=InnoDB
  COMMENT='Proveedores internacionales de productos tecnológicos';

CREATE INDEX idx_proveedor_pais ON proveedor(pais);

CREATE TABLE producto (
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(200)   NOT NULL        COMMENT 'Nombre del producto',
    descripcion    TEXT                           COMMENT 'Descripción detallada',
    precio         DECIMAL(10,2)  NOT NULL        COMMENT 'Precio en soles (PEN)',
    stock          INT            DEFAULT 0       COMMENT 'Unidades disponibles',
    categoria      VARCHAR(100)                   COMMENT 'Categoría del producto',
    imagen_url     VARCHAR(500)                   COMMENT 'URL de la imagen principal',
    proveedor_id   INT                            COMMENT 'FK al proveedor',
    fecha_creacion TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
                                                  COMMENT 'Fecha de registro',
    activo         BOOLEAN        DEFAULT TRUE    COMMENT 'Soft-delete flag',

    CONSTRAINT fk_producto_proveedor
        FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB
  COMMENT='Catálogo de productos tecnológicos';

CREATE INDEX idx_producto_categoria   ON producto(categoria);
CREATE INDEX idx_producto_proveedor   ON producto(proveedor_id);
CREATE INDEX idx_producto_precio      ON producto(precio);

CREATE TABLE pedido (
    id              INT            AUTO_INCREMENT PRIMARY KEY,
    usuario_id      INT            NOT NULL       COMMENT 'FK al usuario que realizó el pedido',
    fecha_pedido    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
                                                  COMMENT 'Fecha y hora del pedido',
    estado          ENUM('PENDIENTE', 'PROCESANDO', 'ENVIADO', 'ENTREGADO', 'CANCELADO')
                                   DEFAULT 'PENDIENTE'
                                                  COMMENT 'Estado actual del pedido',
    total           DECIMAL(10,2)  NOT NULL        COMMENT 'Monto total del pedido',
    direccion_envio TEXT           NOT NULL        COMMENT 'Dirección de entrega',

    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB
  COMMENT='Pedidos (órdenes de compra)';

CREATE INDEX idx_pedido_usuario ON pedido(usuario_id);
CREATE INDEX idx_pedido_estado  ON pedido(estado);
CREATE INDEX idx_pedido_fecha   ON pedido(fecha_pedido);

CREATE TABLE detalle_pedido (
    id              INT            AUTO_INCREMENT PRIMARY KEY,
    pedido_id       INT            NOT NULL       COMMENT 'FK al pedido padre',
    producto_id     INT            NOT NULL       COMMENT 'FK al producto',
    cantidad        INT            NOT NULL       COMMENT 'Cantidad solicitada',
    precio_unitario DECIMAL(10,2)  NOT NULL       COMMENT 'Precio unitario al momento de la compra',
    subtotal        DECIMAL(10,2)  NOT NULL       COMMENT 'cantidad × precio_unitario',

    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (pedido_id) REFERENCES pedido(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id) REFERENCES producto(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB
  COMMENT='Detalle (líneas) de cada pedido';

CREATE INDEX idx_detalle_pedido   ON detalle_pedido(pedido_id);
CREATE INDEX idx_detalle_producto ON detalle_pedido(producto_id);

CREATE TABLE pago (
    id                      INT            AUTO_INCREMENT PRIMARY KEY,
    pedido_id               INT            NOT NULL       COMMENT 'FK al pedido',
    metodo_pago             ENUM('TARJETA_CREDITO', 'TARJETA_DEBITO', 'PAYPAL', 'TRANSFERENCIA')
                                           NOT NULL       COMMENT 'Método de pago utilizado',
    monto                   DECIMAL(10,2)  NOT NULL       COMMENT 'Monto pagado',
    fecha_pago              TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
                                                          COMMENT 'Fecha y hora del pago',
    estado                  ENUM('PENDIENTE', 'COMPLETADO', 'FALLIDO', 'REEMBOLSADO')
                                           DEFAULT 'PENDIENTE'
                                                          COMMENT 'Estado del pago',
    referencia_transaccion  VARCHAR(100)                   COMMENT 'Código de referencia externo',

    CONSTRAINT fk_pago_pedido
        FOREIGN KEY (pedido_id) REFERENCES pedido(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB
  COMMENT='Pagos asociados a pedidos';


CREATE INDEX idx_pago_pedido ON pago(pedido_id);
CREATE INDEX idx_pago_estado ON pago(estado);

INSERT INTO usuario (nombres, apellidos, email, contrasena, salt, rol) VALUES
    ('Carlos',  'Mendoza Ríos',
     'admin@techimport.pe',
     'a3f5b8c1d2e4f6a7b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2',
     'c2FsdF9hZG1pbl8xMjM=',
     'ADMIN'),
    ('María',   'García López',
     'maria.garcia@gmail.com',
     'b4c6d8e0f2a4b6c8d0e2f4a6b8c0d2e4f6a8b0c2d4e6f8a0b2c4d6e8f0a2b4c6',
     'c2FsdF9jbGllbnRlXzQ=',
     'CLIENTE');

INSERT INTO proveedor (nombre, pais, contacto_email, telefono) VALUES
    ('Shenzhen TechParts Co., Ltd.',
     'China',
     'ventas@shenzhentechparts.com',
     '+86-755-8888-1234'),
    ('Tokyo Electronics Inc.',
     'Japón',
     'export@tokyoelectronics.jp',
     '+81-3-5555-6789'),
    ('Silicon Valley Distributors LLC',
     'Estados Unidos',
     'wholesale@svdistributors.com',
     '+1-408-555-0199');


INSERT INTO producto (nombre, descripcion, precio, stock, categoria, imagen_url, proveedor_id) VALUES
    ('Laptop ASUS ROG Strix G16',
     'Laptop gaming con Intel Core i9-14900HX, 32GB DDR5, RTX 4070, pantalla 16\" 240Hz QHD+. Ideal para gaming y desarrollo.',
     7499.99, 15, 'Laptops',
     '/images/productos/asus-rog-strix-g16.jpg',
     1),
    ('iPhone 16 Pro Max 256GB',
     'Smartphone Apple con chip A18 Pro, pantalla Super Retina XDR 6.9\", cámara de 48MP con zoom óptico 5x. Titanio natural.',
     5299.00, 25, 'Smartphones',
     '/images/productos/iphone-16-pro-max.jpg',
     3),
    ('Monitor Samsung Odyssey G9 49\"',
     'Monitor ultrawide curvo DQHD 5120×1440, 240Hz, 1ms, HDR1000, panel VA. Experiencia inmersiva para gaming y productividad.',
     4899.50, 8, 'Monitores',
     '/images/productos/samsung-odyssey-g9.jpg',
     2),
    ('Teclado Mecánico Keychron Q1 Pro',
     'Teclado mecánico inalámbrico 75%, switches Gateron Jupiter Red, carcasa de aluminio CNC, gasket mount, QMK/VIA.',
     699.90, 40, 'Periféricos',
     '/images/productos/keychron-q1-pro.jpg',
     1),
    ('Sony WH-1000XM5 Auriculares',
     'Auriculares over-ear con cancelación de ruido líder en la industria, 30 horas de batería, Bluetooth 5.3, audio Hi-Res.',
     1349.00, 30, 'Audio',
     '/images/productos/sony-wh1000xm5.jpg',
     2);

INSERT INTO pedido (usuario_id, estado, total, direccion_envio) VALUES
    (2, 'PROCESANDO', 6648.90,
     'Av. Javier Prado Este 4600, Santiago de Surco, Lima 15023, Perú');

INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
    (1, 2, 1, 5299.00, 5299.00),
    (1, 5, 1, 1349.00, 1349.00),
    (1, 4, 1,    0.90,    0.90);

INSERT INTO pago (pedido_id, metodo_pago, monto, estado, referencia_transaccion) VALUES
    (1, 'TARJETA_CREDITO', 6648.90, 'COMPLETADO', 'TXN-2026-06-07-00001');

