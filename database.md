-- =========================================
-- CREACIÓN BASE DE DATOS
-- =========================================
CREATE DATABASE fenix_sport;
USE fenix_sport;

-- =========================================
-- TABLA USUARIO
-- =========================================
CREATE TABLE usuario (
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
telefono VARCHAR(20),
activo BOOLEAN DEFAULT TRUE,
fecha_registro DATE DEFAULT (CURRENT_DATE)
);

-- =========================================
-- TABLA ROL
-- =========================================
CREATE TABLE rol (
id_rol INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(50) NOT NULL UNIQUE
);

-- =========================================
-- TABLA INTERMEDIA USUARIO_ROL
-- =========================================
CREATE TABLE usuario_rol (
id_usuario INT,
id_rol INT,
PRIMARY KEY (id_usuario, id_rol),
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
ON DELETE CASCADE
ON UPDATE CASCADE,
FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
ON DELETE CASCADE
ON UPDATE CASCADE
);

-- =========================================
-- TABLA CATEGORIA
-- =========================================
CREATE TABLE categoria (
id_categoria INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL UNIQUE,
descripcion TEXT
);

-- =========================================
-- TABLA PRODUCTO
-- =========================================
CREATE TABLE producto (
id_producto INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
descripcion TEXT,
precio DECIMAL(10,2) NOT NULL CHECK (precio > 0),
stock INT NOT NULL CHECK (stock >= 0),
imagen_url VARCHAR(255),
id_categoria INT NOT NULL,
FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
ON DELETE RESTRICT
ON UPDATE CASCADE
);

-- =========================================
-- TABLA CARRITO
-- =========================================
CREATE TABLE carrito (
id_carrito INT AUTO_INCREMENT PRIMARY KEY,
fecha_creacion DATE DEFAULT (CURRENT_DATE),
estado VARCHAR(50) NOT NULL,
id_usuario INT NOT NULL,
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
ON DELETE CASCADE
ON UPDATE CASCADE
);

-- =========================================
-- TABLA DETALLE_CARRITO
-- =========================================
CREATE TABLE detalle_carrito (
id_detalle INT AUTO_INCREMENT PRIMARY KEY,
cantidad INT NOT NULL CHECK (cantidad > 0),
precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario > 0),
id_carrito INT NOT NULL,
id_producto INT NOT NULL,
FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito)
ON DELETE CASCADE
ON UPDATE CASCADE,
FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
ON DELETE RESTRICT
ON UPDATE CASCADE
);

-- =========================================
-- TABLA PEDIDO
-- =========================================
CREATE TABLE pedido (
id_pedido INT AUTO_INCREMENT PRIMARY KEY,
fecha_pedido DATE DEFAULT (CURRENT_DATE),
total DECIMAL(10,2) NOT NULL CHECK (total >= 0),
estado VARCHAR(50) NOT NULL,
id_usuario INT NOT NULL,
FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
ON DELETE CASCADE
ON UPDATE CASCADE
);

-- =========================================
-- TABLA PAGO
-- =========================================
CREATE TABLE pago (
id_pago INT AUTO_INCREMENT PRIMARY KEY,
metodo_pago VARCHAR(50) NOT NULL,
monto DECIMAL(10,2) NOT NULL CHECK (monto > 0),
estado VARCHAR(50) NOT NULL,
fecha_pago DATE DEFAULT (CURRENT_DATE),
id_pedido INT NOT NULL,
FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)
ON DELETE CASCADE
ON UPDATE CASCADE
);