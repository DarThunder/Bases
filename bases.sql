--UDTs
CREATE TYPE DireccionType AS (
    calle VARCHAR(100),
    colonia VARCHAR(100),
    numero VARCHAR(20),
    ciudad VARCHAR(50),
    idDireccion INTEGER,
    estado VARCHAR(50),
    codigoPostal VARCHAR(10),
    pais VARCHAR(50)
);

CREATE TYPE ProductoType AS (
    nombre VARCHAR(100),
    precio DECIMAL(10,2),
    idProducto INTEGER,
    color VARCHAR(30),
    talla VARCHAR(10),
    cantidad INTEGER
) NOT FINAL;

CREATE TYPE short UNDER ProductoType ();
CREATE TYPE sueter UNDER ProductoType ();
CREATE TYPE pantalon UNDER ProductoType ();
CREATE TYPE camisa UNDER ProductoType ();

CREATE TYPE UsuarioType AS (
    idUsuario INTEGER,
    username VARCHAR(30),
    password VARCHAR(57),
    rol VARCHAR(20),
    direccion REF(DireccionType) SCOPE IS Direcciones
);

CREATE TYPE VentaType AS (
    usuario REF(UsuarioType) SCOPE IS Usuarios,
    producto REF(ProductoType) SCOPE IS Productos,
    folio INTEGER,
    fecha DATE,
    total DECIMAL(12,2),
    cantidad INTEGER
);

-- Tablas
CREATE TABLE Direcciones OF DireccionType (
    PRIMARY KEY (idDireccion)
) REF IS OID SYSTEM GENERATED;

CREATE TABLE Productos OF ProductoType (
    PRIMARY KEY (idProducto)
) REF IS OID SYSTEM GENERATED;

CREATE TABLE Shorts OF short (
    PRIMARY KEY (idProducto)
) UNDER Productos;

CREATE TABLE Sueters OF sueter (
    PRIMARY KEY (idProducto)
) UNDER Productos;

CREATE TABLE Pantalones OF pantalon (
    PRIMARY KEY (idProducto)
) UNDER Productos;

CREATE TABLE Camisas OF camisa (
    PRIMARY KEY (idProducto)
) UNDER Productos;

CREATE TABLE Usuarios OF UsuarioType (
    PRIMARY KEY (idUsuario)
) REF IS OID SYSTEM GENERATED;

CREATE TABLE Ventas OF VentaType (
    PRIMARY KEY (folio)
) REF IS OID SYSTEM GENERATED;

--Inserts
INSERT INTO Direcciones VALUES ('Calle Principal', 'Centro', '123', 'Ciudad de México', 1, 'CDMX', '06000', 'México');
INSERT INTO Direcciones VALUES ('Avenida Revolución', 'San Ángel', '456', 'Ciudad de México', 2, 'CDMX', '01000', 'México');
INSERT INTO Direcciones VALUES ('Paseo de la Reforma', 'Juárez', '789', 'Ciudad de México', 3, 'CDMX', '06500', 'México');
INSERT INTO Direcciones VALUES ('Calle Morelos', 'Centro', '101', 'Guadalajara', 4, 'Jalisco', '44100', 'México');
INSERT INTO Direcciones VALUES ('Avenida Vallarta', 'Americana', '202', 'Guadalajara', 5, 'Jalisco', '44150', 'México');
INSERT INTO Direcciones VALUES ('Boulevard Díaz Ordaz', 'Del Valle', '303', 'Monterrey', 6, 'Nuevo León', '66220', 'México');
INSERT INTO Direcciones VALUES ('Calle Zaragoza', 'Centro', '404', 'Monterrey', 7, 'Nuevo León', '64000', 'México');
INSERT INTO Direcciones VALUES ('Avenida Benito Juárez', 'Narvarte', '505', 'Ciudad de México', 8, 'CDMX', '03020', 'México');
INSERT INTO Direcciones VALUES ('Calle 60', 'Centro', '606', 'Mérida', 9, 'Yucatán', '97000', 'México');
INSERT INTO Direcciones VALUES ('Avenida Universidad', 'Xalapa-Enríquez', '707', 'Xalapa', 10, 'Veracruz', '91000', 'México');

INSERT INTO Productos VALUES ('Producto Genérico 1', 199.99, 100, 'Rojo', 'M', 50);
INSERT INTO Productos VALUES ('Producto Genérico 2', 299.99, 101, 'Azul', 'L', 30);
INSERT INTO Productos VALUES ('Producto Genérico 3', 99.99, 102, 'Verde', 'S', 75);
INSERT INTO Productos VALUES ('Producto Genérico 4', 149.99, 103, 'Negro', 'XL', 20);
INSERT INTO Productos VALUES ('Producto Genérico 5', 249.99, 104, 'Blanco', 'M', 45);
INSERT INTO Productos VALUES ('Producto Genérico 6', 179.99, 105, 'Gris', 'L', 35);
INSERT INTO Productos VALUES ('Producto Genérico 7', 129.99, 106, 'Amarillo', 'S', 60);
INSERT INTO Productos VALUES ('Producto Genérico 8', 349.99, 107, 'Morado', 'XL', 15);
INSERT INTO Productos VALUES ('Producto Genérico 9', 89.99, 108, 'Rosa', 'M', 80);
INSERT INTO Productos VALUES ('Producto Genérico 10', 399.99, 109, 'Café', 'L', 25);

INSERT INTO Shorts VALUES ('Short Deportivo', 249.99, 200, 'Negro', '32', 40);
INSERT INTO Shorts VALUES ('Short Casual', 199.99, 201, 'Azul', '34', 35);
INSERT INTO Shorts VALUES ('Short de Baño', 179.99, 202, 'Azul Marino', '36', 50);
INSERT INTO Shorts VALUES ('Short de Mezclilla', 299.99, 203, 'Azul Claro', '38', 30);
INSERT INTO Shorts VALUES ('Short Deportivo', 229.99, 204, 'Rojo', '32', 45);
INSERT INTO Shorts VALUES ('Short de Pijama', 149.99, 205, 'Gris', '34', 60);
INSERT INTO Shorts VALUES ('Short de Vestir', 349.99, 206, 'Beige', '36', 25);
INSERT INTO Shorts VALUES ('Short Casual', 199.99, 207, 'Verde', '38', 40);
INSERT INTO Shorts VALUES ('Short de Baño', 159.99, 208, 'Amarillo', '32', 55);
INSERT INTO Shorts VALUES ('Short Deportivo', 279.99, 209, 'Blanco', '34', 35);

INSERT INTO Sueters VALUES ('Suéter de Lana', 499.99, 300, 'Café', 'M', 20);
INSERT INTO Sueters VALUES ('Suéter Casual', 399.99, 301, 'Gris', 'L', 25);
INSERT INTO Sueters VALUES ('Suéter Navideño', 349.99, 302, 'Rojo', 'S', 30);
INSERT INTO Sueters VALUES ('Suéter de Cuello Alto', 449.99, 303, 'Negro', 'XL', 15);
INSERT INTO Sueters VALUES ('Suéter Ligero', 299.99, 304, 'Azul', 'M', 35);
INSERT INTO Sueters VALUES ('Suéter de Algodón', 379.99, 305, 'Blanco', 'L', 20);
INSERT INTO Sueters VALUES ('Suéter con Capucha', 429.99, 306, 'Verde', 'S', 25);
INSERT INTO Sueters VALUES ('Suéter de Lana', 549.99, 307, 'Beige', 'XL', 10);
INSERT INTO Sueters VALUES ('Suéter Casual', 329.99, 308, 'Morado', 'M', 30);
INSERT INTO Sueters VALUES ('Suéter Navideño', 299.99, 309, 'Verde', 'L', 40);

INSERT INTO Pantalones VALUES ('Pantalón de Mezclilla', 599.99, 400, 'Azul', '32', 25);
INSERT INTO Pantalones VALUES ('Pantalón de Vestir', 699.99, 401, 'Negro', '34', 20);
INSERT INTO Pantalones VALUES ('Pantalón Casual', 499.99, 402, 'Gris', '36', 30);
INSERT INTO Pantalones VALUES ('Pantalón de Mezclilla', 549.99, 403, 'Negro', '38', 15);
INSERT INTO Pantalones VALUES ('Pantalón Deportivo', 399.99, 404, 'Azul Marino', '40', 35);
INSERT INTO Pantalones VALUES ('Pantalón de Vestir', 749.99, 405, 'Café', '42', 10);
INSERT INTO Pantalones VALUES ('Pantalón Casual', 449.99, 406, 'Beige', '44', 25);
INSERT INTO Pantalones VALUES ('Pantalón de Mezclilla', 649.99, 407, 'Azul Claro', '46', 20);
INSERT INTO Pantalones VALUES ('Pantalón Deportivo', 379.99, 408, 'Negro', '48', 30);
INSERT INTO Pantalones VALUES ('Pantalón de Vestir', 799.99, 409, 'Gris Oscuro', '50', 15);

INSERT INTO Camisas VALUES ('Camisa Formal', 399.99, 500, 'Blanco', 'M', 30);
INSERT INTO Camisas VALUES ('Camisa Casual', 299.99, 501, 'Azul', 'L', 40);
INSERT INTO Camisas VALUES ('Camisa de Vestir', 349.99, 502, 'Rosa', 'S', 25);
INSERT INTO Camisas VALUES ('Camisa Hawaina', 249.99, 503, 'Multicolor', 'XL', 50);
INSERT INTO Camisas VALUES ('Camisa Formal', 429.99, 504, 'Celeste', 'M', 20);
INSERT INTO Camisas VALUES ('Camisa Casual', 279.99, 505, 'Verde', 'L', 35);
INSERT INTO Camisas VALUES ('Camisa de Vestir', 379.99, 506, 'Lila', 'S', 30);
INSERT INTO Camisas VALUES ('Camisa Hawaina', 199.99, 507, 'Amarillo', 'XL', 45);
INSERT INTO Camisas VALUES ('Camisa Formal', 449.99, 508, 'Gris', 'M', 25);
INSERT INTO Camisas VALUES ('Camisa Casual', 329.99, 509, 'Rojo', 'L', 40);

INSERT INTO Usuarios VALUES (1, 'admin1', 'password123', 'Administrador', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 1));
INSERT INTO Usuarios VALUES (2, 'cliente1', 'cliente123', 'Cliente', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 2));
INSERT INTO Usuarios VALUES (3, 'vendedor1', 'vendedor123', 'Vendedor', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 3));
INSERT INTO Usuarios VALUES (4, 'cliente2', 'cliente456', 'Cliente', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 4));
INSERT INTO Usuarios VALUES (5, 'vendedor2', 'vendedor456', 'Vendedor', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 5));
INSERT INTO Usuarios VALUES (6, 'admin2', 'password456', 'Administrador', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 6));
INSERT INTO Usuarios VALUES (7, 'cliente3', 'cliente789', 'Cliente', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 7));
INSERT INTO Usuarios VALUES (8, 'vendedor3', 'vendedor789', 'Vendedor', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 8));
INSERT INTO Usuarios VALUES (9, 'cliente4', 'cliente012', 'Cliente', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 9));
INSERT INTO Usuarios VALUES (10, 'vendedor4', 'vendedor012', 'Vendedor', (SELECT REF(d) FROM Direcciones d WHERE d.idDireccion = 10));

INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 2),
    (SELECT REF(p) FROM Productos p WHERE p.idProducto = 100),
    1000, DATE '2023-01-15', 199.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 4),
    (SELECT REF(p) FROM Shorts p WHERE p.idProducto = 200),
    1001, DATE '2023-01-16', 249.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 7),
    (SELECT REF(p) FROM Sueters p WHERE p.idProducto = 300),
    1002, DATE '2023-01-17', 499.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 9),
    (SELECT REF(p) FROM Pantalones p WHERE p.idProducto = 400),
    1003, DATE '2023-01-18', 599.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 2),
    (SELECT REF(p) FROM Camisas p WHERE p.idProducto = 500),
    1004, DATE '2023-01-19', 399.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 4),
    (SELECT REF(p) FROM Productos p WHERE p.idProducto = 101),
    1005, DATE '2023-01-20', 299.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 7),
    (SELECT REF(p) FROM Shorts p WHERE p.idProducto = 201),
    1006, DATE '2023-01-21', 199.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 9),
    (SELECT REF(p) FROM Sueters p WHERE p.idProducto = 301),
    1007, DATE '2023-01-22', 399.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 2),
    (SELECT REF(p) FROM Pantalones p WHERE p.idProducto = 401),
    1008, DATE '2023-01-23', 699.99, 1
);
INSERT INTO Ventas VALUES (
    (SELECT REF(u) FROM Usuarios u WHERE u.idUsuario = 4),
    (SELECT REF(p) FROM Camisas p WHERE p.idProducto = 501),
    1009, DATE '2023-01-24', 299.99, 1
);
