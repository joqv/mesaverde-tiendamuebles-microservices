CREATE DATABASE IF NOT EXISTS mundomuebles;
USE mundomuebles;

DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE,
  PRIMARY KEY (id)
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

INSERT INTO usuarios (username, password, nombre, email) VALUES
('alejandro', 'pass123', 'Alejandro Torres', 'alejandro@example.com'),
('maria', 'pass456', 'María Gómez', 'maria@example.com'),
('juan', 'pass789', 'Juan Pérez', 'juan@example.com'),
('lucia', 'pass321', 'Lucía Fernández', 'lucia@example.com'),
('carlos', 'pass654', 'Carlos Ruiz', 'carlos@example.com');

INSERT INTO categorias (nombre) VALUES
('Salas'),
('Comedores'),
('Dormitorios'),
('Oficinas'),
('Exteriores');

INSERT INTO productos (nombre, precio, tipo, stock, descripcion, imagen, categoria_id) VALUES
('Sofá 3 plazas', 1200.00, 'mueble', 10, 'Sofá cómodo de tela gris', 'sofa3.jpg', 1),
('Mesa comedor madera', 850.00, 'mueble', 5, 'Mesa de roble para 6 personas', 'mesa_comedor.jpg', 2),
('Silla ergonómica', 450.00, 'silla', 20, 'Silla de oficina con soporte lumbar', 'silla_ergonomica.jpg', 4),
('Cama matrimonial', 1500.00, 'mueble', 7, 'Cama con base de madera y colchón incluido', 'cama.jpg', 3),
('Banco de jardín', 300.00, 'mueble', 12, 'Banco de metal para exteriores', 'banco_jardin.jpg', 5);

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
  IN p_precio_unitario DECIMAL(10,2),
  IN p_usuario VARCHAR(50)
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
  IN p_total DECIMAL(10,2),
  OUT p_id_venta INT
)
BEGIN
  -- Insertar la venta
  INSERT INTO ventas (usuario_id, total)
  VALUES (p_usuario_id, p_total);

  -- Obtener el ID generado
  SET p_id_venta = LAST_INSERT_ID();
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

-- Declarar variable para capturar el ID de la venta
SET @usuario_id = 1;
SET @total = 1650.00;
SET @id_venta = 0;

-- Llamar al procedimiento
CALL sp_registrar_venta(@usuario_id, @total, @id_venta);

-- Ver el ID generado
SELECT @id_venta;
select*from ventas;

-- -----------

SET @venta_id = @id_venta;
SET @producto_id = 1;
SET @cantidad = 1;
SET @precio_unitario = 1200.00;

CALL sp_registrar_detalle_venta(@venta_id, @producto_id, @cantidad, @precio_unitario);

-- --------------

SET @id_producto = 1;
SET @cantidad = 1;
SET @precio_unitario = 1200.00;
SET @usuario = 'alejandro';

CALL sp_descontar_producto(@id_producto, @cantidad, @precio_unitario, @usuario);

select * from productos;







