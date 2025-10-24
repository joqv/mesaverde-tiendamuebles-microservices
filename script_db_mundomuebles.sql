SET NAMES 'utf8mb4';
SET CHARACTER SET utf8mb4;

CREATE DATABASE IF NOT EXISTS mundomuebles;
USE mundomuebles;

CREATE TABLE rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Tabla usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100),
    email VARCHAR(100),
    estado TINYINT DEFAULT 1 NOT NULL
);

-- Tabla usuario_rol (relación muchos a muchos usuarios-rol)
CREATE TABLE usuario_rol (
    usuario_id INT NOT NULL,
    rol_id INT NOT NULL,
    PRIMARY KEY(usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES rol(id)
);


CREATE TABLE oauth2_authorization (
    id VARCHAR(100) PRIMARY KEY,
    registered_client_id VARCHAR(100) NOT NULL,
    principal_name VARCHAR(200) NOT NULL,
    authorization_grant_type VARCHAR(100) NOT NULL,
    authorized_scopes VARCHAR(1000),
    attributes TEXT,
    state VARCHAR(500),
    authorization_code_value TEXT,
    authorization_code_issued_at TIMESTAMP,
    authorization_code_expires_at TIMESTAMP,
    authorization_code_metadata TEXT,
    access_token_value TEXT,
    access_token_issued_at TIMESTAMP,
    access_token_expires_at TIMESTAMP,
    access_token_metadata TEXT,
    access_token_type VARCHAR(100),
    access_token_scopes VARCHAR(1000),
    oidc_id_token_value TEXT,
    oidc_id_token_issued_at TIMESTAMP,
    oidc_id_token_expires_at TIMESTAMP,
    oidc_id_token_metadata TEXT,
    refresh_token_value TEXT,
    refresh_token_issued_at TIMESTAMP,
    refresh_token_expires_at TIMESTAMP,
    refresh_token_metadata TEXT
);
CREATE TABLE oauth2_authorization_consent (
    registered_client_id VARCHAR(100) NOT NULL,
    principal_name VARCHAR(200) NOT NULL,
    authorities VARCHAR(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);
CREATE TABLE oauth2_registered_client (
    id VARCHAR(100) PRIMARY KEY,
    client_id VARCHAR(100) NOT NULL,
    client_id_issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret VARCHAR(200),
    client_secret_expires_at TIMESTAMP,
    client_name VARCHAR(200) NOT NULL,
    client_authentication_methods VARCHAR(1000) NOT NULL,
    authorization_grant_types VARCHAR(1000) NOT NULL,
    redirect_uris VARCHAR(1000),
    post_logout_redirect_uris VARCHAR(1000),
    scopes VARCHAR(1000),
    client_settings VARCHAR(2000),
    token_settings VARCHAR(2000)
);

DROP TABLE IF EXISTS categorias;
CREATE TABLE categorias (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL
);

DROP TABLE IF EXISTS productos;
CREATE TABLE productos (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  tipo ENUM('mueble','silla') NOT NULL,
  stock INT NOT NULL,
  -- stock_minimo INT NOT NULL DEFAULT 10,
  descripcion TEXT,
  imagen VARCHAR(150),
  categoria_id INT,
  PRIMARY KEY (id),
  FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

DROP TABLE IF EXISTS ventas;
CREATE TABLE ventas (
  id INT NOT NULL AUTO_INCREMENT,
  -- cliente_id INT,
  usuario_id INT,
  -- tipo_pago VARCHAR(50),
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  -- FOREIGN KEY (cliente_id) REFERENCES clientes(id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

DROP TABLE IF EXISTS detalles_venta;
CREATE TABLE detalles_venta (
  id INT NOT NULL AUTO_INCREMENT,
  venta_id INT NOT NULL,
  producto_id INT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2),
  PRIMARY KEY (id),
  FOREIGN KEY (venta_id) REFERENCES ventas(id),
  FOREIGN KEY (producto_id) REFERENCES productos(id)
);

DROP TABLE IF EXISTS clientes;
CREATE TABLE clientes (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE,
  telefono VARCHAR(20),
  direccion VARCHAR(255),
  PRIMARY KEY (id)
);

-- datos

INSERT INTO rol (id, name) VALUES (1, 'ADMIN');
INSERT INTO rol (id, name) VALUES (2, 'USER');

INSERT INTO usuarios (username, password, nombre, email, estado) VALUES
('alejandro', 'pass123', 'Alejandro Torres', 'alejandro@example.com', 1),
('maria', 'pass456', 'María Gómez', 'maria@example.com', 1),
('juan', 'pass789', 'Juan Pérez', 'juan@example.com', 1),
('lucia', 'pass321', 'Lucía Fernández', 'lucia@example.com', 1),
('carlos', 'pass654', 'Carlos Ruiz', 'carlos@example.com', 1);

INSERT INTO categorias (nombre) VALUES
('Salas'),
('Comedores'),
('Dormitorios'),
('Oficinas'),
('Exteriores');

INSERT INTO productos (nombre, precio, tipo, stock, descripcion, imagen, categoria_id) VALUES
('Sofá 3 plazas', 1200.00, 'mueble', 100, 'Sofá cómodo de tela gris', 'sofa3.jpg', 1),
('Mesa comedor madera', 850.00, 'mueble', 105, 'Mesa de roble para 6 personas', 'mesa_comedor.jpg', 2),
('Silla ergonómica', 450.00, 'silla', 120, 'Silla de oficina con soporte lumbar', 'silla_ergonomica.jpg', 4),
('Cama matrimonial', 1500.00, 'mueble', 107, 'Cama con base de madera y colchón incluido', 'cama.jpg', 3),
('Banco de jardín', 300.00, 'mueble', 112, 'Banco de metal para exteriores', 'banco_jardin.jpg', 5);

INSERT INTO ventas (usuario_id, total) VALUES
(1, 1650.00),
(2, 450.00),
(3, 300.00),
(4, 850.00),
(5, 1200.00);

INSERT INTO detalles_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 1200.00, 1200.00),
(1, 3, 1, 450.00, 450.00),
(2, 3, 1, 450.00, 450.00),
(3, 5, 1, 300.00, 300.00),
(4, 2, 1, 850.00, 850.00);

INSERT INTO clientes (nombre, email, telefono, direccion) VALUES
('Laura Méndez', 'laura@example.com', '555-1234', 'Av. Central 123'),
('Pedro Ramírez', 'pedro@example.com', '555-5678', 'Calle Norte 456'),
('Ana López', 'ana@example.com', '555-9012', 'Boulevard Sur 789'),
('Diego Torres', 'diego@example.com', '555-3456', 'Ruta 66 km 12'),
('Sofía Herrera', 'sofia@example.com', '555-7890', 'Camino Real 321');

--

DROP PROCEDURE IF EXISTS sp_descontar_producto;
DELIMITER $$

CREATE  PROCEDURE sp_descontar_producto (
  IN p_id_producto INT,
  IN p_cantidad INT,
  IN p_precio_unitario DECIMAL(10,2)
)
BEGIN
  -- Validar que el producto existe y tiene suficiente stock
  DECLARE v_stock_actual INT;

  SELECT stock INTO v_stock_actual
  FROM productos
  WHERE id = p_id_producto;

  IF v_stock_actual IS NULL THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Producto no encontrado';
  ELSEIF v_stock_actual < p_cantidad THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Stock insuficiente para el producto';
  ELSE
    -- Descontar el stock
    UPDATE productos
    SET stock = stock - p_cantidad
    WHERE id = p_id_producto;
  END IF;
END$$

DELIMITER ;

--
DROP PROCEDURE IF EXISTS sp_registrar_venta;
DELIMITER $$

CREATE PROCEDURE sp_registrar_venta (
  IN p_usuario_id INT,
  IN p_total DECIMAL(10,2)
)
BEGIN
  -- Insertar la venta
  INSERT INTO ventas (usuario_id, total)
  VALUES (p_usuario_id, p_total);

  -- Obtener el ID generado
  SELECT LAST_INSERT_ID() AS id_venta;

END$$

DELIMITER ;

--
DROP PROCEDURE IF EXISTS sp_registrar_detalle_venta;
DELIMITER $$

CREATE PROCEDURE sp_registrar_detalle_venta (
  IN p_venta_id INT,
  IN p_producto_id INT,
  IN p_cantidad INT,
  IN p_precio_unitario DECIMAL(10,2)
)
BEGIN
  DECLARE v_subtotal DECIMAL(10,2);

  -- Calcular subtotal
  SET v_subtotal = p_cantidad * p_precio_unitario;

  -- Insertar el detalle
  INSERT INTO detalles_venta (
    venta_id, producto_id, cantidad, precio_unitario, subtotal
  )
  VALUES (
    p_venta_id, p_producto_id, p_cantidad, p_precio_unitario, v_subtotal
  );
END$$

DELIMITER ;

--







