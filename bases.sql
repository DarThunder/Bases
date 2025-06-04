-- Bloque anónimo para eliminar objetos si existen
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE DetallesVenta CASCADE CONSTRAINTS';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE Ventas CASCADE CONSTRAINTS';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE Camisas CASCADE CONSTRAINTS';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE Pantalones CASCADE CONSTRAINTS';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE Sueters CASCADE CONSTRAINTS';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE Shorts CASCADE CONSTRAINTS';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE Productos CASCADE CONSTRAINTS';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

-- Eliminación de tipos
BEGIN
  EXECUTE IMMEDIATE 'DROP TYPE DetallesVentaType FORCE';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TYPE VentaType FORCE';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TYPE CamisaType FORCE';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TYPE PantalonType FORCE';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TYPE SueterType FORCE';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TYPE ShortType FORCE';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP TYPE ProductoType FORCE';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

-- UDTs (User Defined Types)

-- Tipo Producto (clase padre)
CREATE TYPE ProductoType AS OBJECT (
    idProducto INTEGER,
    nombre VARCHAR(100),
    precio NUMBER(10,2),
    color VARCHAR(30),
    talla VARCHAR(10),
    categorias VARCHAR(50)
) NOT FINAL;
/

-- Clases heredadas de Producto (especializaciones por categoría)
CREATE TYPE ShortType UNDER ProductoType ();
/

CREATE TYPE SueterType UNDER ProductoType ();
/

CREATE TYPE PantalonType UNDER ProductoType ();
/

CREATE TYPE CamisaType UNDER ProductoType ();
/

-- Tipo Venta
CREATE TYPE VentaType AS OBJECT (
    idVenta INTEGER,
    fecha DATE,
    total NUMBER(12,2),
    usuario INTEGER
);
/

-- Tipo DetallesVenta
CREATE TYPE DetallesVentaType AS OBJECT (
    idDetalle INTEGER,
    cantidad INTEGER,
    subtotal NUMBER(10,2),
    venta REF VentaType,
    producto REF ProductoType
);
/

-- TABLAS

-- Tabla Productos (tabla padre)
CREATE TABLE Productos OF ProductoType (
  PRIMARY KEY(idProducto)
);

-- Tablas especializadas de Productos
CREATE TABLE Shorts OF ShortType;
CREATE TABLE Sueters OF SueterType;
CREATE TABLE Pantalones OF PantalonType;
CREATE TABLE Camisas OF CamisaType;

-- Tabla Ventas
CREATE TABLE Ventas OF VentaType (
  PRIMARY KEY(idVenta)
);

-- Tabla DetallesVenta
CREATE TABLE DetallesVenta OF DetallesVentaType;

-- INSERTS

-- Insertar Productos
INSERT INTO Productos VALUES (ProductoType(100, 'Producto Básico 1', 199.99, 'Rojo', 'M', 'General'));
INSERT INTO Productos VALUES (ProductoType(101, 'Producto Básico 2', 299.99, 'Azul', 'L', 'General'));
INSERT INTO Productos VALUES (ProductoType(102, 'Producto Básico 3', 99.99, 'Verde', 'S', 'General'));
INSERT INTO Productos VALUES (ProductoType(103, 'Producto Básico 4', 149.99, 'Negro', 'XL', 'General'));
INSERT INTO Productos VALUES (ProductoType(104, 'Producto Básico 5', 249.99, 'Blanco', 'M', 'General'));

-- Insertar Shorts
INSERT INTO Shorts VALUES (ShortType(200, 'Short Deportivo', 249.99, 'Negro', '32', 'Deportivo'));
INSERT INTO Shorts VALUES (ShortType(201, 'Short Casual', 199.99, 'Azul', '34', 'Casual'));
INSERT INTO Shorts VALUES (ShortType(202, 'Short de Baño', 179.99, 'Azul Marino', '36', 'Playa'));
INSERT INTO Shorts VALUES (ShortType(203, 'Short de Mezclilla', 299.99, 'Azul Claro', '38', 'Casual'));
INSERT INTO Shorts VALUES (ShortType(204, 'Short Deportivo Pro', 229.99, 'Rojo', '32', 'Deportivo'));

-- Insertar Sueters
INSERT INTO Sueters VALUES (SueterType(300, 'Suéter de Lana', 499.99, 'Café', 'M', 'Invierno'));
INSERT INTO Sueters VALUES (SueterType(301, 'Suéter Casual', 399.99, 'Gris', 'L', 'Casual'));
INSERT INTO Sueters VALUES (SueterType(302, 'Suéter Navideño', 349.99, 'Rojo', 'S', 'Festividad'));
INSERT INTO Sueters VALUES (SueterType(303, 'Suéter de Cuello Alto', 449.99, 'Negro', 'XL', 'Elegante'));
INSERT INTO Sueters VALUES (SueterType(304, 'Suéter Ligero', 299.99, 'Azul', 'M', 'Primavera'));

-- Insertar Pantalones
INSERT INTO Pantalones VALUES (PantalonType(400, 'Pantalón de Mezclilla', 599.99, 'Azul', '32', 'Casual'));
INSERT INTO Pantalones VALUES (PantalonType(401, 'Pantalón de Vestir', 699.99, 'Negro', '34', 'Formal'));
INSERT INTO Pantalones VALUES (PantalonType(402, 'Pantalón Casual', 499.99, 'Gris', '36', 'Casual'));
INSERT INTO Pantalones VALUES (PantalonType(403, 'Pantalón de Mezclilla Negro', 549.99, 'Negro', '38', 'Casual'));
INSERT INTO Pantalones VALUES (PantalonType(404, 'Pantalón Deportivo', 399.99, 'Azul Marino', '40', 'Deportivo'));

-- Insertar Camisas
INSERT INTO Camisas VALUES (CamisaType(500, 'Camisa Formal', 399.99, 'Blanco', 'M', 'Formal'));
INSERT INTO Camisas VALUES (CamisaType(501, 'Camisa Casual', 299.99, 'Azul', 'L', 'Casual'));
INSERT INTO Camisas VALUES (CamisaType(502, 'Camisa de Vestir', 349.99, 'Rosa', 'S', 'Elegante'));
INSERT INTO Camisas VALUES (CamisaType(503, 'Camisa Hawaiana', 249.99, 'Multicolor', 'XL', 'Playa'));


-- Insertar Ventas
INSERT INTO Ventas VALUES (VentaType(1000, DATE '2023-01-15', 199.99, 2));
INSERT INTO Ventas VALUES (VentaType(1001, DATE '2023-01-16', 249.99, 4));
INSERT INTO Ventas VALUES (VentaType(1002, DATE '2023-01-17', 499.99, 7));
INSERT INTO Ventas VALUES (VentaType(1003, DATE '2023-01-18', 599.99, 9));
INSERT INTO Ventas VALUES (VentaType(1004, DATE '2023-01-19', 399.99, 2));


-- Insertar DetallesVenta
INSERT INTO DetallesVenta
    SELECT 10005, 1, 399.99,REF(v), REF(p) FROM Ventas v, Productos p WHERE v.idVenta = 1000 AND p.idProducto = 100;
/

INSERT INTO DetallesVenta
    SELECT 10006, 1, 499.99,REF(v), REF(p) FROM Ventas v, Productos p WHERE v.idVenta = 1001 AND p.idProducto = 101;
/
INSERT INTO DetallesVenta
    SELECT 10007, 1, 599.99,REF(v), REF(p) FROM Ventas v, Productos p WHERE v.idVenta = 1002 AND p.idProducto = 102;
/
INSERT INTO DetallesVenta
    SELECT 10008, 1, 699.99,REF(v), REF(p) FROM Ventas v, Productos p WHERE v.idVenta = 1003 AND p.idProducto = 103;
/
INSERT INTO DetallesVenta
    SELECT 10009, 1, 799.99,REF(v), REF(p) FROM Ventas v, Productos p WHERE v.idVenta = 1004 AND p.idProducto = 104;
/
