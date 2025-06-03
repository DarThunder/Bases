-- UDTs (User Defined Types)

-- Tipo Usuario
CREATE TYPE UsuarioType AS (
    idUsuario INTEGER,
    username VARCHAR(30),
    password VARCHAR(57),
    rol VARCHAR(20)
);

-- Tipo Producto (clase padre)
CREATE TYPE ProductoType AS (
    idProducto INTEGER,
    nombre VARCHAR(100),
    precio DECIMAL(10,2),
    color VARCHAR(30),
    talla VARCHAR(10),
    categorias VARCHAR(50)
) NOT FINAL;

-- Clases heredadas de Producto (especializaciones por categoría)
CREATE TYPE ShortType UNDER ProductoType ();
CREATE TYPE SueterType UNDER ProductoType ();
CREATE TYPE PantalonType UNDER ProductoType ();
CREATE TYPE CamisaType UNDER ProductoType ();

-- Tipo Venta
CREATE TYPE VentaType AS (
    idVenta INTEGER,
    fecha DATE,
    total DECIMAL(12,2),
    usuario REF(UsuarioType) SCOPE IS Usuarios
);

-- Tipo DetallesVenta
CREATE TYPE DetallesVentaType AS (
    idDetalle INTEGER,
    cantidad INTEGER,
    subtotal DECIMAL(10,2),
    venta REF(VentaType) SCOPE IS Ventas,
    producto REF(ProductoType) SCOPE IS Productos
);

-- TABLAS

-- Tabla Usuarios
CREATE TABLE Usuarios OF UsuarioType (
    PRIMARY KEY (idUsuario)
) REF IS OID SYSTEM GENERATED;

-- Tabla Productos (tabla padre)
CREATE TABLE Productos OF ProductoType (
    PRIMARY KEY (idProducto)
) REF IS OID SYSTEM GENERATED;

-- Tablas especializadas de Productos
CREATE TABLE Shorts OF ShortType (
    PRIMARY KEY (idProducto)
) UNDER Productos;

CREATE TABLE Sueters OF SueterType (
    PRIMARY KEY (idProducto)
) UNDER Productos;

CREATE TABLE Pantalones OF PantalonType (
    PRIMARY KEY (idProducto)
) UNDER Productos;

CREATE TABLE Camisas OF CamisaType (
    PRIMARY KEY (idProducto)
) UNDER Productos;

-- Tabla Ventas
CREATE TABLE Ventas OF VentaType (
    PRIMARY KEY (idVenta)
) REF IS OID SYSTEM GENERATED;

-- Tabla DetallesVenta
CREATE TABLE DetallesVenta OF DetallesVentaType (
    PRIMARY KEY (idDetalle)
) REF IS OID SYSTEM GENERATED;

-- INSERTS

-- Insertar Usuarios
INSERT INTO Usuarios VALUES (1, 'admin1', 'password123', 'Administrador');
INSERT INTO Usuarios VALUES (2, 'cliente1', 'cliente123', 'Cliente');
INSERT INTO Usuarios VALUES (3, 'vendedor1', 'vendedor123', 'Vendedor');
INSERT INTO Usuarios VALUES (4, 'cliente2', 'cliente456', 'Cliente');
INSERT INTO Usuarios VALUES (5, 'vendedor2', 'vendedor456', 'Vendedor');
INSERT INTO Usuarios VALUES (6, 'admin2', 'password456', 'Administrador');
INSERT INTO Usuarios VALUES (7, 'cliente3', 'cliente789', 'Cliente');
INSERT INTO Usuarios VALUES (8, 'vendedor3', 'vendedor789', 'Vendedor');
INSERT INTO Usuarios VALUES (9, 'cliente4', 'cliente012', 'Cliente');
INSERT INTO Usuarios VALUES (10, 'vendedor4', 'vendedor012', 'Vendedor');

-- Insertar Productos genéricos
INSERT INTO Productos VALUES (100, 'Producto Básico 1', 199.99, 'Rojo', 'M', 'General');
INSERT INTO Productos VALUES (101, 'Producto Básico 2', 299.99, 'Azul', 'L', 'General');
INSERT INTO Productos VALUES (102, 'Producto Básico 3', 99.99, 'Verde', 'S', 'General');
INSERT INTO Productos VALUES (103, 'Producto Básico 4', 149.99, 'Negro', 'XL', 'General');
INSERT INTO Productos VALUES (104, 'Producto Básico 5', 249.99, 'Blanco', 'M', 'General');

-- Insertar Shorts
INSERT INTO Shorts VALUES (200, 'Short Deportivo', 249.99, 'Negro', '32', 'Deportivo');
INSERT INTO Shorts VALUES (201, 'Short Casual', 199.99, 'Azul', '34', 'Casual');
INSERT INTO Shorts VALUES (202, 'Short de Baño', 179.99, 'Azul Marino', '36', 'Playa');
INSERT INTO Shorts VALUES (203, 'Short de Mezclilla', 299.99, 'Azul Claro', '38', 'Casual');
INSERT INTO Shorts VALUES (204, 'Short Deportivo Pro', 229.99, 'Rojo', '32', 'Deportivo');

-- Insertar Sueters
INSERT INTO Sueters VALUES (300, 'Suéter de Lana', 499.99, 'Café', 'M', 'Invierno');
INSERT INTO Sueters VALUES (301, 'Suéter Casual', 399.99, 'Gris', 'L', 'Casual');
INSERT INTO Sueters VALUES (302, 'Suéter Navideño', 349.99, 'Rojo', 'S', 'Festividad');
INSERT INTO Sueters VALUES (303, 'Suéter de Cuello Alto', 449.99, 'Negro', 'XL', 'Elegante');
INSERT INTO Sueters VALUES (304, 'Suéter Ligero', 299.99, 'Azul', 'M', 'Primavera');

-- Insertar Pantalones
INSERT INTO Pantalones VALUES (400, 'Pantalón de Mezclilla', 599.99, 'Azul', '32', 'Casual');
INSERT INTO Pantalones VALUES (401, 'Pantalón de Vestir', 699.99, 'Negro', '34', 'Formal');
INSERT INTO Pantalones VALUES (402, 'Pantalón Casual', 499.99, 'Gris', '36', 'Casual');
INSERT INTO Pantalones VALUES (403, 'Pantalón de Mezclilla Negro', 549.99, 'Negro', '38', 'Casual');
INSERT INTO Pantalones VALUES (404, 'Pantalón Deportivo', 399.99, 'Azul Marino', '40', 'Deportivo');

-- Insertar Camisas
INSERT INTO Camisas VALUES (500, 'Camisa Formal', 399.99, 'Blanco', 'M', 'Formal');
INSERT INTO Camisas VALUES (501, 'Camisa Casual', 299.99, 'Azul', 'L', 'Casual');
INSERT INTO Camisas VALUES (502, 'Camisa de Vestir', 349.99, 'Rosa', 'S', 'Elegante');
INSERT INTO Camisas VALUES (503, 'Camisa Hawaiana', 249.99, 'Multicolor', 'XL', 'Playa');
INSERT INTO Camisas VALUES (504, 'Camisa Formal Celeste', 429.99, 'Celeste', 'M', 'Formal');

-- Insertar Ventas
INSERT INTO Ventas VALUES (
    1000, 
    DATE '2023-01-15', 
    199.99, 
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 2)
);
INSERT INTO Ventas VALUES (
    1001, 
    DATE '2023-01-16', 
    249.99, 
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 4)
);
INSERT INTO Ventas VALUES (
    1002, 
    DATE '2023-01-17', 
    499.99, 
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 7)
);
INSERT INTO Ventas VALUES (
    1003, 
    DATE '2023-01-18', 
    599.99, 
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 9)
);
INSERT INTO Ventas VALUES (
    1004, 
    DATE '2023-01-19', 
    399.99, 
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 2)
);

-- Insertar DetallesVenta
INSERT INTO DetallesVenta VALUES (
    10001, 
    1, 
    199.99,
    (SELECT REF(v) FROM Ventas v WHERE v.idVenta = 1000),
    (SELECT REF(p) FROM Productos p WHERE p.idProducto = 100)
);
INSERT INTO DetallesVenta VALUES (
    10002, 
    1, 
    249.99,
    (SELECT REF(v) FROM Ventas v WHERE v.idVenta = 1001),
    (SELECT REF(p) FROM Shorts p WHERE p.idProducto = 200)
);
INSERT INTO DetallesVenta VALUES (
    10003, 
    1, 
    499.99,
    (SELECT REF(v) FROM Ventas v WHERE v.idVenta = 1002),
    (SELECT REF(p) FROM Sueters p WHERE p.idProducto = 300)
);
INSERT INTO DetallesVenta VALUES (
    10004, 
    1, 
    599.99,
    (SELECT REF(v) FROM Ventas v WHERE v.idVenta = 1003),
    (SELECT REF(p) FROM Pantalones p WHERE p.idProducto = 400)
);
INSERT INTO DetallesVenta VALUES (
    10005, 
    1, 
    399.99,
    (SELECT REF(v) FROM Ventas v WHERE v.idVenta = 1004),
    (SELECT REF(p) FROM Camisas p WHERE p.idProducto = 500)
);
